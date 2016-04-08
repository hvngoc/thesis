package com.hvngoc.googlemaptest.helper;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 12125_000 on 3/3/2016.
 */
public class HTTPPostHelper {
    private String response;
    private String server_url;
    private JSONObject params;

    public HTTPPostHelper(String serverUrl, JSONObject params) {
        this.server_url = serverUrl;
        this.params = params;
    }

    public boolean sendHTTTPostRequest() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.server_url);
        try {
            // Add your data
            StringEntity se = new StringEntity(this.params.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            Log.i("DATA: ", this.params.toString());
            httppost.setEntity(se);
            // Execute HTTP Post Request
            HttpResponse res = httpclient.execute(httppost);
            StatusLine statusLine = res.getStatusLine();
            Log.i("Post:", "Posteddddddddddddddddddddddddddddddddddd");
            if (statusLine.getStatusCode() == 200) {
                try {
                    this.response = EntityUtils.toString(res.getEntity());
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        // send or receive request failed
        return false;
    }




    public boolean sendStringHTTTPostRequest(String json) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.server_url);
        try {
            // Add your data
            StringEntity se = new StringEntity(json);
            httppost.setEntity(se);
            // Execute HTTP Post Request
            HttpResponse res = httpclient.execute(httppost);
            StatusLine statusLine = res.getStatusLine();
            Log.i("Post:", "Posteddddddddddddddddddddddddddddddddddd");
            if (statusLine.getStatusCode() == 200) {
                try {
                    this.response = EntityUtils.toString(res.getEntity());
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        // send or receive request failed
        return false;
    }

    public String getResponse() {
        return this.response;
    }

}
