package rs.esolutions.ecomm.android.service;

import java.io.IOException;
import java.util.Map;

import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.domain.Callback;
import rs.esolutions.ecomm.android.domain.HttpResponseData;
import rs.esolutions.ecomm.android.domain.RegistrationData;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Asynhronous task which registers user in the background thread 
 * 
 * @author Gavrilovici
 *
 */
public class HttpRegistrationTask extends AsyncTask<Void, Void, HttpResponseData> {	

	protected final String tag = this.getClass().getSimpleName();
	protected final String applicationSuffix = "/publ/androidRegistration.register.html";
	protected String registrationUrl;
	
	protected RegistrationData registrationData;
	protected ProgressDialog dialog;
	protected Context context;
	protected Callback<Boolean> callback;
	
	public HttpRegistrationTask(Activity activity, RegistrationData registrationData, String applicationUrl, Callback<Boolean> callback) {
		this.registrationData = registrationData;
		this.context = activity;
		this.callback = callback;
		if(applicationUrl != null && (applicationUrl.length() > 0) && (!applicationUrl.equalsIgnoreCase("null"))) {
			registrationUrl = applicationUrl + applicationSuffix;
		} else {
			String defaultServerUrl = context.getString(R.string.server_url);
			registrationUrl = defaultServerUrl + applicationSuffix;
		}
	}
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
	    dialog.setMessage(context.getText(R.string.registering_user));
	    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    dialog.setCancelable(false);
	    dialog.show();

	}

	protected HttpResponseData doInBackground(Void... nothing) {    				
        return new HttpPostService().postRegistration(registrationUrl, registrationData);
    }   
        
	protected void onPostExecute(HttpResponseData httpResponseData) {
		dialog.hide();
	    dialog = null;
	    if(httpResponseData.isResponseCodeOk() && 
	    		(httpResponseData.getKeyFromBody().equals("registration.successfull"))) {
		   
	    	// save upload key in persistent properties file
	    	PersistentPropertiesService pps = new PersistentPropertiesService();
	    	Map<String, String> persistentProperties;
	    	try {
				persistentProperties = pps.readPersistentProperties();
				persistentProperties.put(RegistrationData.PROPERTY_UPLOAD_KEY, 
						httpResponseData.getCookie(RegistrationData.PROPERTY_UPLOAD_KEY));
				pps.writePersitentProperties(persistentProperties);
			} catch (IOException e) {
				pps.showNoPersistentPropertes(context);
				return;
			}
			callback.onFinish(true);
			
	    }
	    Toast.makeText(context, httpResponseData.getResult(context), Toast.LENGTH_LONG).show();
    } 

}
