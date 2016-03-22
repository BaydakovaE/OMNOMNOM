package com.example.lal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	private ProgressDialog pDialog;
 
    // JSON Node names
    private static final String TAG_DATA = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

 
    // contacts JSONArray
    JSONArray data = null;
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        String var="1"; 
 
     // URL to get contacts JSON
        String url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=subcategories/getCategories&id="+var;
        contactList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();
        // Calling async task to get json
        new GetContacts().execute(url);
    }
 
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<String, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
// 
//        @Override
        protected Void doInBackground(String... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(arg0[0], ServiceHandler.GET);
  
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    data = jsonObj.getJSONArray(TAG_DATA);
 
                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                         
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
 
                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
 
                        // adding contact to contact list
                        contactList.add(contact);
                    } 
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
               // Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.activity_main, new String[] { 
                    		//TAG_ID, 
                    		TAG_NAME
                            }, new int[] { 
                    		//R.id.id,
                            R.id.name
                            //, R.id.image_url 
                            });
 
            setListAdapter(adapter);
        }

    }
}
 