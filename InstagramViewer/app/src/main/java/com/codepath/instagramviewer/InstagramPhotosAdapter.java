package com.codepath.instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.ocpsoft.prettytime.PrettyTime;

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

        // Clear out the image view (in case we are recycling)
        ivPhoto.setImageResource(0);
        tvLikes.setText("");

        // Insert the item data into each of the items.
        tvCaption.setText(photo.caption);
     //   TODO: Validate timestamp is not NULL. if ((photo.created_time) != "") {
            Date myDate = new java.util.Date((Long.parseLong(photo.created_time)*1000));

        PrettyTime p = new PrettyTime(new Locale("en"));
        tvCreatedTime.setText(p.format(myDate));

        // Insert images using Picasso
        Picasso.with(getContext()).load(photo.profile_picture).into(ivRounded);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        // Username
        tvUsername.setText(photo.username);

        // Likes
        tvLikes.setText(getLikes(photo.likesCount));

        // Comments
        tvCommentsCount.setText(getComments(photo.comments_count));

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
