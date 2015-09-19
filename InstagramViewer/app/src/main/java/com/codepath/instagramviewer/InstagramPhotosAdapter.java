package com.codepath.instagramviewer;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

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
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);



        // Clear out the image view (in case we are recycling)
        ivPhoto.setImageResource(0);
        tvLikes.setText("");

        // Insert the item data into each of the items.
        tvCaption.setText(photo.caption);
        tvCreatedTime.setText("Posted on: " + photo.created_time);

        // Insert image using Picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        Picasso.with(getContext()).load(photo.profile_picture).into(ivProfile);

        // Likes
        tvLikes.setText(getLikes(photo.likesCount));

        // Return the created item as a view
        return convertView;

    }


    public String getLikes(int likes) {
        String strLikes = format(likes) + " like";

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
