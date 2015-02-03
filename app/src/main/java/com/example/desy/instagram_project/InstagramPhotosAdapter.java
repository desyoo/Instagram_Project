package com.example.desy.instagram_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by desy on 2/2/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhotos> {
    //what data do we need from the activity
    //Context, Data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhotos> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    //what our item looks like
    //use the template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        InstagramPhotos photo = getItem(position);
        //check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            //create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
        }
        //lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        //Insert the model data into each of the view item
        tvCaption.setText(photo.caption);
        //clear out the imageview if it was recycled (right away)
        ivPhoto.setImageResource(0);
        //insert the image using picasso (send out async)
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        //Return the created item as a view
        return convertView;
    }
}
