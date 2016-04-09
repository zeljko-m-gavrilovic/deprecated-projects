package rs.esolutions.ecomm.android.service;

import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.domain.Callback;
import rs.esolutions.ecomm.android.domain.HttpResponseData;
import rs.esolutions.ecomm.android.domain.UploadData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Asynhronous task which upload data to server in the background thread
 * 
 * @author Gavrilovici
 *
 */
public class HttpUploadDataTask extends AsyncTask<Void, Void, HttpResponseData> {	

	protected final String tag = this.getClass().getSimpleName();
	protected final String applicationSuffix = "/androidUpload.upload.html";
	protected String uploadDataUrl;
	
	protected UploadData uploadData;
	protected ProgressDialog dialog;
	protected Context context;
	protected String uploadKey;
	protected Callback<Boolean> callback;
	
	public HttpUploadDataTask(Activity activity, UploadData uploadData, String uploadKey, String serverUrl, Callback<Boolean> callback) {
		this.uploadData = uploadData;
		this.context = activity;
		this.uploadKey = uploadKey;
		this.callback = callback;
		boolean urlOverriden = serverUrl != null && (serverUrl.length() > 0) && (!serverUrl.equalsIgnoreCase("null"));
		if(urlOverriden) {
			uploadDataUrl = serverUrl + applicationSuffix;
		} else {
			String defaultServerUrl = context.getString(R.string.server_url);
			uploadDataUrl = defaultServerUrl + applicationSuffix;
		}
	}
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
	    dialog.setMessage(context.getText(R.string.uploading_data));
	    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    dialog.setCancelable(false);
	    dialog.show();

	}

	protected HttpResponseData doInBackground(Void... nothing) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
        return new HttpPostService().postUploadData(uploadDataUrl, uploadData, imsi, uploadKey);
    }   
        
	protected void onPostExecute(HttpResponseData httpResponseData) {
		dialog.hide();
	    dialog = null;
	    if(httpResponseData.isResponseCodeOk() && 
	    		(httpResponseData.getKeyFromBody().equalsIgnoreCase("successfully.uploaded.data"))) {
	    	callback.onFinish(true);
	    	Toast.makeText(context, httpResponseData.getResult(context), Toast.LENGTH_LONG).show();
	    } else {
	    	showErrorDialog(httpResponseData.getResult(context));
	    }
	    
    }
	
	/**
	 * Show user phone settings where he/she can enable GPS device
	 */
	private void showErrorDialog(String reason) {
		String title = context.getText(R.string.error_uploading_data) + " "+ reason;
		String ok = context.getString(R.string.ok_ok);
		final AlertDialog alert = new AlertDialog.Builder(context)
		.setMessage(title)
				.setCancelable(false)
				.setPositiveButton(ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).create();
		alert.show();
	}
}