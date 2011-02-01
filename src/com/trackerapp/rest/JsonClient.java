package com.trackerapp.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonClient {

   /**
    * Sends a json GET request to a specified url and 
    * returns a JSONObject as response.
    * 
    * @param uri
    * @param data
    * @return json object
    */
   public JSONObject doGet(String uri, Map<String, Object> params) {
      HttpClient httpclient = new DefaultHttpClient();
      HttpGet httpget = new HttpGet(uri);
      httpget.addHeader("Content-Type", "application/json");
      
      // build params
      HttpParams paramContainer = new BasicHttpParams();
      Iterator it = params.keySet().iterator();

      while (it.hasNext()) {
         String key = (String) it.next();
         Object val = params.get(key);
         paramContainer.setParameter(key, val);
      }

      httpget.setParams(paramContainer);
      HttpResponse response = null;
      JSONObject jsonResponse = null;

      // send the request and get response
      try {
         response = httpclient.execute(httpget);
         HttpEntity entity = response.getEntity();

         // Build JSONObject from a string
         if (entity != null) {
            jsonResponse = buildJsonObjectFromStream(entity.getContent());
         }
      } catch (ClientProtocolException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      if (jsonResponse == null) {
         Log.i("JsonClient.doGet", "Couldn't build json response");
         return null;
      }

      return jsonResponse;
   }

   /**
    * Sends a json POST request to a specified url and returns
    * a JSONObject as response.
    * 
    * @param uri
    * @param data
    * @return
    */
   public JSONObject doPost(String uri, JSONObject data) {
      return null;
   }

   /**
    * Converts InputStream to String
    *
    * @param jsonData
    * @return JSONObject
    * @throws IOException
    */
   protected String convertStreamToString(InputStream jsonData) throws IOException {
      BufferedReader reader
            = new BufferedReader(new InputStreamReader(jsonData));

      String line = null;
      StringBuilder sb = new StringBuilder();

      while ((line = reader.readLine()) != null) {
         sb.append(line + "\n");
      }

      return sb.toString();
   }

   /**
    * Builds a json object from a stream.
    *
    * @param jsonData
    * @return JSONObject
    */
   protected JSONObject buildJsonObjectFromStream(InputStream jsonData) {
      JSONObject jsonObj = null;

      try {
         jsonObj = new JSONObject(convertStreamToString(jsonData));
      } catch (JSONException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      if (jsonObj == null) {
         Log.i("JsonClient.buildJsonObject", "Couldn't build a json object");
         return null;
      }

      return jsonObj;
   }
}
