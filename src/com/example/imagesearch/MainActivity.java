package com.example.imagesearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	EditText searchQuery;
	Button searchButton;
	private ProgressDialog pDialog;
	JSONParser jp = new JSONParser();
	ImageListAdapter imageListAdapter;
	static ArrayList<HashMap<String,String>> imageList = new ArrayList<HashMap<String,String>>();
	ListView list;
	static String urlImages = "http://ajax.googleapis.com/ajax/services/search/images";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		searchQuery = (EditText) findViewById(R.id.search_field);
		searchButton = (Button) findViewById(R.id.search_button);	
		searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try{
					if(searchQuery.getText().toString()!=null){
						new AttemptSearch().execute();
						initLoader();
					}
				}catch(Exception ex){
					Log.v("gsearch", "Error : " + ex.getMessage());
				}
			}
			
		});
		
	}
	
	void initLoader()
	{
		list = (ListView)findViewById(R.id.list);
		imageListAdapter = new ImageListAdapter(this, imageList);
		list.setAdapter(imageListAdapter);
	}
	

	class AttemptSearch extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;
		
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(MainActivity.this);
           pDialog.setMessage("Searching...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }
		
		@Override
		protected String doInBackground(String... args) {
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("v", "1.0"));
               params.add(new BasicNameValuePair("q", searchQuery.getText().toString()));

               Log.d("request!", "starting");
               // getting product details by making HTTP request
               JSONObject json = jp.makeHttpRequest(
            		   urlImages, "GET", params);

               // check your log for json response
               Log.d("Search attempt", json.toString());

               // json success tag
               Log.d("Search Successful!", json.toString());
               JSONObject result = json.getJSONObject("responseData");
               JSONArray ja = result.getJSONArray("results");
               imageList = new ArrayList<HashMap<String,String>>();
               for(int i = 0; i < ja.length(); i++)
               {
            	   JSONObject jo = ja.getJSONObject(i);
            	   HashMap<String, String> hm = new HashMap<String,String>();
            	   hm.put("url", jo.getString("url"));
            	   hm.put("title", jo.getString("title"));
               }
               //parse json here
               return null;
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;
			
		}
		
		/**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();
       }	
	}
}
