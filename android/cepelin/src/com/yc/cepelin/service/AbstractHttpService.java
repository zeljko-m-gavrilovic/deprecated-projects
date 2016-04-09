package com.yc.cepelin.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public abstract class AbstractHttpService extends AsyncTask<String, Void, JSONArray> {

	protected static final String baseUri = "http://75.127.100.163/cepelin-0.2/";
	//protected static final String baseUri = "http://192.168.10.189:8080/cepelin/";

	protected abstract HttpUriRequest getRequest(String uri) throws UnsupportedEncodingException;
			
	Callback<JSONArray> callback;
	
	public AbstractHttpService(Callback<JSONArray> callback) {
		this.callback = callback;
	}
	
	protected JSONArray doInBackground(String... urls) {    				
        try {
        	JSONArray jsonArray = pullJsonArrayFromServer(baseUri + urls[0]);                            
            return jsonArray;
        } catch (Exception e) {
            // think about it, toast maybe?
        } 
        return new JSONArray();
    }   
        
	protected void onPostExecute(JSONArray result) {
		callback.onFinish(result);
    } 

    private JSONArray pullJsonArrayFromServer(String url) throws ParseException, IOException, JSONException {
    	String jsonString = pullDataFromServer(url);
    	jsonString = cleanupResponse(jsonString);

    	//a small hack to handle single objects and arrays in same way
    	//it adds a little bit of runtime overhead, but this way we reuse a lot of code
        if (jsonString.startsWith("{")) {
        	jsonString = "[" + jsonString + "]";
        }
            	
        return new JSONArray(jsonString);    	
    }
        
    private String pullDataFromServer(String uri) throws ParseException, IOException {    	
        HttpClient httpclient = createHttpClient();
    	
    	HttpResponse response = httpclient.execute(getRequest(uri));

        HttpEntity httpEntity = response.getEntity();
    	
        if (httpEntity != null) { 
        	return EntityUtils.toString(httpEntity);
        }
        
        return "[]";
    }
		
    static protected HttpClient createHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 20000); //20 sec			
		HttpConnectionParams.setSoTimeout(httpParams, 20000);
    				
        return new DefaultHttpClient(httpParams);    	
    }
    
    protected String cleanupResponse(String rawResponse) {
    	int arrayStart = rawResponse.indexOf("[");
    	int objectStart = rawResponse.indexOf("{");
    	
    	if (arrayStart < 0) {
    		return rawResponse.substring(objectStart);
    	}
    	
    	if (objectStart < 0) {
    		return rawResponse.substring(arrayStart);
    	}
    	
    	if (objectStart < arrayStart) {
    		return rawResponse.substring(objectStart);    		
    	} else {
    		return rawResponse.substring(arrayStart);
    	}
    }    

}