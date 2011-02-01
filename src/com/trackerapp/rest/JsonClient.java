package com.trackerapp.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
            InputStream stream = entity.getContent();
            jsonResponse = buildJsonObjectFromStream(stream);
            stream.close();
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
      String jsonData = data.toString();
      HttpClient httpclient = new DefaultHttpClient();
      HttpPost httppost = new HttpPost(uri);
      httppost.addHeader("Content-Type", "application/json");
      HttpEntity entity = null;
      JSONObject jsonResponse = null;

      try {
         entity = new StringEntity(jsonData, "UTF-8");
         httppost.setEntity(entity);

         try {
            HttpResponse res = httpclient.execute(httppost);
            HttpEntity resEntity = res.getEntity();

            if (entity != null) {
               InputStream stream = resEntity.getContent();
               jsonResponse = buildJsonObjectFromStream(stream);
               stream.close();
            }
         } catch (ClientProtocolException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      if (jsonResponse == null) {
         Log.i("JsonClient.doPost", "Couldn't do a post");
         return null;
      }

      return jsonResponse;
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
