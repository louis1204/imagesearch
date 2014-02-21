package com.example.imagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ImageListAdapter extends ArrayAdapter<ImageModel>{
    
	ImageModel item;
	private final LayoutInflater mInflater;
    
    public ImageListAdapter(Context context) {
    	super(context, R.layout.image_list_row);
    	mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setData(List<ImageModel> data) {
        clear();
        if (data != null) {
            for (ImageModel appEntry : data) {
                add(appEntry);
            }
        }
    }
    
    static class ViewHolder {
        protected Button chat;
        protected Button profile;
        protected Button delete;
        protected Button cancel;
      }
    
    /**
     * Populate new items in the list.
     */
    @Override 
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = null;
        item = getItem(position);
        
        //We currently have no view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.image_list_row, parent, false);	//set the view to a friend list row

            if(item.getTitle() != "null")
            	((TextView)view.findViewById(R.id.title)).setText(item.getTitle());	//set the mood if it's not null
            else
            	((TextView)view.findViewById(R.id.title)).setText("");
        
        } 
        
        else {
        	//return the same view if it's not selected
        	view = convertView;
        }
        
        return view;
    }
}