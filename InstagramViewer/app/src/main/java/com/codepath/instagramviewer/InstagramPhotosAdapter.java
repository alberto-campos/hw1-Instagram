package com.codepath.instagramviewer;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{


    public InstagramPhotosAdapter(Context context, int resource, List<InstagramPhoto> objects) {
        super(context, resource, objects);
    }
}
