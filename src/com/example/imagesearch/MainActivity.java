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

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<ImageModel>>{
	EditText searchQuery;
	Button searchButton;
	private ProgressDialog pDialog;
	JSONParser jp = new JSONParser();
	ImageListAdapter imageListAdapter;
	static ArrayList<HashMap<String,String>> imageList = new ArrayList<HashMap<String,String>>();

	static String urlImages = "http://ajax.googleapis.com/ajax/services/search/images";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageListAdapter = new ImageListAdapter(this);
		setListAdapter(imageListAdapter);
		
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
		getLoaderManager().initLoader(0, null, this);
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


	@Override
	public Loader<List<ImageModel>> onCreateLoader(int arg0, Bundle arg1) {
		return new DataListLoader(this);
	}


	@Override
	public void onLoadFinished(Loader<List<ImageModel>> arg0, List<ImageModel> data) {
		imageListAdapter.setData(data);
	}


	@Override
	public void onLoaderReset(Loader<List<ImageModel>> arg0) {
		imageListAdapter.setData(null);
		
	}
	
	public static class DataListLoader extends AsyncTaskLoader<List<ImageModel>> {
		List<ImageModel> mModels;
 
        public DataListLoader(Context context) {
            super(context);
        }
 
        @Override
        public List<ImageModel> loadInBackground() {
            System.out.println("DataListLoader.loadInBackground");
             
             // You should perform the heavy task of getting data from 
             // Internet or database or other source 
             // Here, we are generating some Sample data
 
            // Create corresponding array of entries and load with data.
            List<ImageModel> entries = new ArrayList<ImageModel>();
            for(int i = 0; i < imageList.size(); i++)
            {
            	entries.add(new ImageModel(imageList.get(i).get("title"),
            			imageList.get(i).get("url")));
            }
 
            return entries;
        }
         
        /**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
        @Override public void deliverResult(List<ImageModel> listOfData) {
            if (isReset()) {
                // An async query came in while the loader is stopped.  We
                // don't need the result.
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<ImageModel> oldApps = listOfData;
            mModels = listOfData;
 
            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                super.deliverResult(listOfData);
            }
 
            // At this point we can release the resources associated with
            // 'oldApps' if needed; now that the new result is delivered we
            // know that it is no longer in use.
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }
 
        /**
         * Handles a request to start the Loader.
         */
        @Override protected void onStartLoading() {
            if (mModels != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mModels);
            }
 
 
            if (takeContentChanged() || mModels == null) {
                // If the data has changed since the last time it was loaded
                // or is not currently available, start a load.
                forceLoad();
            }
        }
 
        /**
         * Handles a request to stop the Loader.
         */
        @Override protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }
 
        /**
         * Handles a request to cancel a load.
         */
        @Override public void onCanceled(List<ImageModel> apps) {
            super.onCanceled(apps);
 
            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }
 
        /**
         * Handles a request to completely reset the Loader.
         */
        @Override protected void onReset() {
            super.onReset();
 
            // Ensure the loader is stopped
            onStopLoading();
 
            // At this point we can release the resources associated with 'apps'
            // if needed.
            if (mModels != null) {
                onReleaseResources(mModels);
                mModels = null;
            }
        }
 
        /**
         * Helper function to take care of releasing resources associated
         * with an actively loaded data set.
         */
        protected void onReleaseResources(List<ImageModel> apps) {}
	}
}
