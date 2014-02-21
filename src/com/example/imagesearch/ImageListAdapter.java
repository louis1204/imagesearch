package com.example.imagesearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter{
    
	ImageModel item;
	private static LayoutInflater mInflater;
    ArrayList<HashMap<String, String>> data;
    public ImageListAdapter(Activity activity, ArrayList<HashMap<String,String>> data) {
    	this.data = data;
    	mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * Populate new items in the list.
     */
    @Override 
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        
        //We currently have no view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.image_list_row, null);	//set the view to a friend list row

        }
        
        TextView title = (TextView)view.findViewById(R.id.title);
        
        HashMap<String, String> pic = new HashMap<String, String>();
        
        title.setText(pic.get("title"));
        return view;
    }
}