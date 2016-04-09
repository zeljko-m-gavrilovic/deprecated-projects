package com.yc.cepelin.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class HttpGetPictureService extends AsyncTask<String, Void, Bitmap> {	

	Callback<Bitmap> callback;
	
	public HttpGetPictureService(Callback<Bitmap> callback) {
		this.callback = callback;
	}
	
	protected Bitmap doInBackground(String... urls) {    				
        try {
        	return pullPictureFromServer(AbstractHttpService.baseUri + urls[0]);                            
        } catch (Exception e) {
            // think about it, toast maybe?
        } 
        return null;
    }   
        
	protected void onPostExecute(Bitmap result) {
		callback.onFinish(result);
    } 

    private Bitmap pullPictureFromServer(String uri) throws ParseException, IOException {    	
        HttpClient httpclient = AbstractHttpService.createHttpClient();
    	
    	HttpResponse response = httpclient.execute(new HttpGet(uri));

        HttpEntity httpEntity = response.getEntity();        
        if (httpEntity != null) {
        	return BitmapFactory.decodeStream(httpEntity.getContent());        	
        }
        
        return null;
    }

}