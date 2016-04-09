package com.yc.cepelin.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.ParseException;

import android.os.AsyncTask;

public class HttpPostPictureService extends AsyncTask<String, Void, Void> {	

	Callback<Integer> callback;
	private ByteArrayOutputStream stream;
	private int venueId;
	
	public HttpPostPictureService(int venueId, ByteArrayOutputStream stream, Callback<Integer> callback) {
		this.venueId = venueId;
		this.stream = stream;
		this.callback = callback;
	}
	
	protected Void doInBackground(String... urls) {    				
        try {
        	pushPictureToServer(AbstractHttpService.baseUri + urls[0]);
        } catch (Exception e) {
            // think about it, toast maybe?
        } 
        return null;
    }   
        
	protected void onPostExecute(Void result) {
		callback.onFinish(0);
    } 

    private void pushPictureToServer(String uri) throws ParseException, IOException {    	
    	HttpPostPicture2.post(uri, venueId, "image/jpeg", new ByteArrayInputStream(stream.toByteArray()));
        return;
    }

}