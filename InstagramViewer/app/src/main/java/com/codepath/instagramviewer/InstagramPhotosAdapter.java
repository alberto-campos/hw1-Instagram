package com.codepath.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

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
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        // Clear out the image view (in case we are recycling)
        ivPhoto.setImageResource(0);

        // Insert the item data into each of the items.
        tvCaption.setText(photo.caption);
        // Insert image using Picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        // Return the created item as a view
        return convertView;

    }
}
