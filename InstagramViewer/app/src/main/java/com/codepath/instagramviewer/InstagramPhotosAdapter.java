package com.codepath.instagramviewer;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
    // Context and Data source

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if using a Recycled View, else Inflate
        if (convertView == null) {
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // Lookup the views for populating in the data: Image, Caption
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        RoundedImageView ivRounded = (RoundedImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);

        // Clear out the image view (in case we are recycling)
        ivPhoto.setImageResource(0);
        tvLikes.setText("");
        tvComments.setText("");
        tvCommentsCount.setText("0");
        tvLocation.setText("");

        // Insert the item data into each of the items.
        tvCaption.setText(photo.caption);
     //   TODO: Validate timestamp is not NULL. Write a method to check Timezone
        Date myDate = new java.util.Date((Long.parseLong(photo.created_time)*1000)-(1000*60*60*3)-3000);

        PrettyTime p = new PrettyTime(new Locale("en"));
        tvCreatedTime.setText(p.format(myDate));

        // Insert images using Picasso
        Picasso.with(getContext()).load(photo.profile_picture).placeholder(R.drawable.profileplaceholder).into(ivRounded);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.imageplaceholder).into(ivPhoto);

        // Username
        tvUsername.setText(photo.username);

        // Check location
        if (photo.location_name != "null") {
            tvLocation.setText(photo.location_name);
        }
        // Likes
        tvLikes.setText(getLikes(photo.likesCount));

        // Comments
        tvCommentsCount.setText(getComments(photo.comments_count));

        String commenting = null;
        String comment_from = null;
        String comment_date = null;

        try {
            // TODO: Get the newest comment based on timestamp
//            int newestComment = photo.comments_count;
//            commenting = photo.comments_data.getJSONObject(newestComment).getString("text");
//            comment_from = photo.comments_data.getJSONObject(newestComment).getJSONObject("from").getString("username");
//            comment_date = photo.comments_data.getJSONObject(newestComment).getString("created_time");

            commenting = photo.comments_data.getJSONObject(1).getString("text");
            comment_from = photo.comments_data.getJSONObject(1).getJSONObject("from").getString("username");
            comment_date = photo.comments_data.getJSONObject(1).getString("created_time");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //   TODO: Validate timestamp is not NULL.
       // Adjust for timezone
        myDate = new java.util.Date(((Long.parseLong(comment_date)*1000)-(1000*60*60*3)-3000));
        p = new PrettyTime(new Locale("en"));

        tvComments.setText(Html.fromHtml("<strong>" + comment_from+ "</strong>" + "&nbsp;&nbsp;" +
                "<small>" + commenting  + "</small> &nbsp;" +
                "<i>" + p.format(myDate) + "</i>"));

        // Return the created item as a view
        return convertView;
    }

    private String getComments(int comments_count) {
        if (comments_count > 1)
            return ("View all " + comments_count + " comments.");
        else if (comments_count == 1)
            return ("View comment.");
        else
            return ("Be the first one to comment.");
    }


    public String getLikes(int likes) {
        String strLikes = " " + format(likes) + " like";

        if (likes != 1)
            strLikes = strLikes + "s";

        return strLikes;
    }

    private static final NavigableMap<Integer, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000, "k");
        suffixes.put(1_000_000, "M");
        suffixes.put(1_000_000_000, "G");
        // More than this doesn't seem realistic.
    }

    public static String format(int value) {

        if (value < 1000) return Integer.toString(value);
        if (value < 1) return "ERROR: ";

        Map.Entry<Integer, String> e = suffixes.floorEntry(value);
        Integer divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
