package com.codepath.instagramviewer;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Photos extends AppCompatActivity {

    public static final String CLIENT_ID = "58d0aa6d7b7e46b28abb26217eaf3ff1";
    public ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        photos = new ArrayList<>();
        // Create the adapter linking it to the source.
        aPhotos = new InstagramPhotosAdapter(this, photos);

        // Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Fetch popular photos
        fetchPopularPhotos();

    }

    private void refreshContent() {
        Context context = getApplicationContext();
        CharSequence text = "Retrieving new images...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        aPhotos.clear();
        fetchPopularPhotos();
        aPhotos.addAll();
        aPhotos.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchPopularPhotos() {

        AsyncHttpClient client;

        // Create HTTP Client
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        client = new AsyncHttpClient();
        // Trigger to GET request
        client.get(url, null, new JsonHttpResponseHandler(){
            // on Success handler 200 code

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

               // Log.i("DEBUG", response.toString());
                // Iterate the response and move each picture into a Java object
                JSONArray photosJSON = null;

                try {

                    photosJSON = response.getJSONArray("data");
                    // Iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {

                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.profile_picture = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.created_time = photoJSON.getJSONObject("caption").getString("created_time");

                        String tmp = photoJSON.getString("location");

                        if (tmp != "null") {
                            //photo.location_name = photoJSON.getJSONObject("location").getString("name");
                            photo.location_name = tmp;
                        }
                        else
                        {
                            photo.location_name = tmp;
                        }

                        //photo.location_name = photoJSON.getJSONObject("location").getString("name");
                        photo.comments_count = photoJSON.getJSONObject("comments").getInt("count");
                        photo.comments_data = photoJSON.getJSONObject("comments").getJSONArray("data");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        // add each photo to the general "photos" array
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPhotos.notifyDataSetChanged();
            }

            // on Failure handler

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
