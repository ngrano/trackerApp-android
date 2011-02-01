package com.trackerapp.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

public class JsonClient {

   /**
    * Sends a json GET request to a specified url and 
    * returns a JSONObject as response.
    * 
    * @param uri
    * @param data
    * @return json object
    */
   public JSONObject doGet(String uri, JSONObject data) {
      HttpClient httpclient = new DefaultHttpClient();
      HttpGet httpget = new HttpGet(uri);
      httpget.addHeader("Content-Type", "application/json");
      
      String json = data.toString();

      HttpResponse response = null;
      
      // send json and get response
      try {
         response = httpclient.execute(httpget);
      } catch (ClientProtocolException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return null;
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
}
