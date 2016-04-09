package com.yc.cepelin.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;

public class HttpPostService extends AbstractHttpService {
	
	private ArrayList<NameValuePair> params;

    public HttpPostService(Callback<JSONArray> callback, ArrayList<NameValuePair> params) {
    	super(callback);
    	this.params = params;
	}

	@Override
	protected HttpUriRequest getRequest(String uri) throws UnsupportedEncodingException {
        HttpPost httppost = new HttpPost(uri); 
    	httppost.setEntity(new UrlEncodedFormEntity(params));
		return httppost;
	}    
}      

