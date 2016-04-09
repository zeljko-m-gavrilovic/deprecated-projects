package com.yc.cepelin.service;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;

class HttpGetService extends AbstractHttpService {
			
	@Override
	protected HttpUriRequest getRequest(String uri) throws UnsupportedEncodingException {
		return new HttpGet(uri);
	}

	public HttpGetService(Callback<JSONArray> callback) {
		super(callback);
		
	}  	
	
}      
