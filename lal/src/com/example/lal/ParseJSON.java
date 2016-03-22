//import java.util.ArrayList;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
////package com.example.lal;
////
////import java.util.ArrayList;
////
////import org.json.JSONArray;
////import org.json.JSONException;
////import org.json.JSONObject;
////
////public class ParseJSON {
////	
////	protected ArrayList <Ccatalog> catalog_parser(String json_string) throws JSONException
////	{
////		 
////		ArrayList <Ccatalog> array = new ArrayList <Ccatalog>();
////		
////		JSONObject jObject = new JSONObject(json_string);
////		JSONArray data  = jObject.getJSONArray("data");
////
////		try {
////		    for(int i = 0; i < data.length(); i++){
////		    	JSONObject jsonObject = data.getJSONObject(i);
////		    	Ccatalog ctlg = new Ccatalog();
////		     
////		    	ctlg.id = Integer.parseInt(jsonObject.optString("id").toString());
////		        ctlg.name = jsonObject.optString("name").toString();
////		        ctlg.image_url = jsonObject.optString("url").toString();
////		        array.add(ctlg);
////		    }
////		   
////		} catch (JSONException e) {
////		    e.printStackTrace();
////		}
////		return array;
////	}
////}
//
//	protected ArrayList <Ccatalog> catalog_parser(String json_string) throws JSONException
//	{
//		 
//		ArrayList <Ccatalog> array = new ArrayList <Ccatalog>();
//		JSONObject jObject = new JSONObject(json_string.substring(json_string.indexOf("{"), json_string.lastIndexOf("}") + 1));
//		//JSONObject jObject = new JSONObject(json_string);
//		JSONArray data  = jObject.getJSONArray("data");
//
//		try {
//		    for(int i = 0; i < data.length(); i++){
//		    	JSONObject jsonObject = data.getJSONObject(i);
//		    	Ccatalog ctlg = new Ccatalog();
//		     
//		    	ctlg.id = Integer.parseInt(jsonObject.optString("id").toString());
//		        ctlg.name = jsonObject.optString("name").toString();
//		        ctlg.image_url = jsonObject.optString("image_url").toString();
//		        array.add(ctlg);
//		    }
//		   
//		} catch (JSONException e) {
//		    e.printStackTrace();
//		}
//		return array;
//	}