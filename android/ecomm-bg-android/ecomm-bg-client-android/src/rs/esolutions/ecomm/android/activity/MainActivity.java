package rs.esolutions.ecomm.android.activity;

import rs.esolutions.ecomm.android.Constants;
import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.domain.Callback;
import rs.esolutions.ecomm.android.domain.RegistrationData;
import rs.esolutions.ecomm.android.domain.UploadData;
import rs.esolutions.ecomm.android.service.HttpUploadDataTask;
import rs.esolutions.ecomm.android.service.PersistentPropertiesService;
import rs.esolutions.ecomm.android.utility.StreamUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AbstractCitiesActivity {

	// constants
	private final String tag = this.getClass().getSimpleName();
	private final String PHOTO_FILE_NAME = "/ecomm_android.jpg";
	private final int takeAPhotoActivity = 0;
	private final int findGpsLocation = 1;
	private final String PHOTO_TAKEN = "photo_taken";
	private final int TWO_SECONDS = 1000 * 2;
	
	// GUI components
	protected ImageView photoImageView;
	protected TextView commentTextView;
	protected Button takePhotoButton;
	protected Button removePhotoButton;
	protected Button uploadDataButton;
	
	// services
	protected PersistentPropertiesService pps;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected StreamUtils streamsUtil;
	
	// model
	protected String photoPath;
	protected boolean photoTaken;
	protected Bitmap bitmapPhoto;
	
	protected Map<String, String> persistentProperties;
	protected Location currentLocation; 
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PersistentPropertiesService pps = new PersistentPropertiesService();
		try {
			persistentProperties = pps.readPersistentProperties();
		} catch (IOException e) {
			pps.showNoPersistentPropertes(getApplicationContext());
		}
		
		// if user is not registered then go to the registration screen
		String uploadKey = persistentProperties.get(RegistrationData.PROPERTY_UPLOAD_KEY);
		if (!Constants.DEBUG)
		if(uploadKey == null || (uploadKey.length() == 0) || (uploadKey.equalsIgnoreCase("null"))) {
			registerActivity();
			finish();
		}
		
		// manage GUI stuff
		setContentView(R.layout.main);
		photoImageView = (ImageView) findViewById(R.id.photo);
		commentTextView = (TextView) findViewById(R.id.comment);
		
		takePhotoButton = (Button) findViewById(R.id.take_photo_button);
		takePhotoButton.setOnClickListener(new TakeAPhotoButtonClickHandler());
		removePhotoButton = (Button) findViewById(R.id.remove_photo_button);
		removePhotoButton.setOnClickListener(new RemovePhotoButtonClickHandler());
		removePhotoButton.setEnabled(false);
		
		uploadDataButton = (Button) findViewById(R.id.upload_button);
		uploadDataButton.setOnClickListener(new UploadDataButtonClickHandler());
		
		// initialize variables
		photoPath = Environment.getExternalStorageDirectory() + PHOTO_FILE_NAME;
		
		// manage location handling		
		streamsUtil = new StreamUtils();
		locationListener = new UserLocationListener();
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		if(gpsSensorExist() && !gpsIsEnabled()) {
			startGPSSettingsIntent();
		}
		
		startListenLocationUpdates();
	}
	
	/**
	 * 
	 * Handle take photo button is clicked
	 */
	public class TakeAPhotoButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(tag, "TakeAPhotoButtonClickHandler.onClick()");
			startCameraIntent();
		}
	}
	
	/**
	 * 
	 * Handle remove photo button is clicked
	 */
	public class RemovePhotoButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(tag, "RemovePhotoButtonClickHandler.onClick()");
			removeImage();
		}
	}
	
	/**
	 * 
	 * Handle upload data button is clicked
	 */
	public class UploadDataButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(tag, "UploadDataButtonClickHandler.onClick()");
			if (Constants.DEBUG) {
				registerActivity();return;
			}
			UploadData ud = fromGuiToModel();
			List<String> validationErrors = validate(ud);
			if(validationErrors.size() == 0) {
				String uploadKey = persistentProperties.get(RegistrationData.PROPERTY_UPLOAD_KEY);
				String applicationUrl = persistentProperties.get(PersistentPropertiesService.APPLICATION_URL);
				HttpUploadDataTask ps = new HttpUploadDataTask(MainActivity.this, ud, uploadKey, applicationUrl, new Callback<Boolean>() {
					@Override
					public void onFinish(Boolean result) {
						resetFormData();
					}
				});
				ps.execute();
			} else {
				showValidationErrors(validationErrors);
			}
		}

		private UploadData fromGuiToModel() {
			UploadData ud = new UploadData();
			// handle comment
			ud.setComment(commentTextView.getText().toString());
			// TODO handle city
//			ud.setCity("Beograd");
			ud.setCity(city);
			// handle photo
			if(bitmapPhoto != null) {
				ByteArrayOutputStream photoOutStream = new ByteArrayOutputStream();
				bitmapPhoto.compress(CompressFormat.JPEG, 95, photoOutStream);
				ByteArrayInputStream photoInStream = new ByteArrayInputStream(photoOutStream.toByteArray());
				try {
					ud.setContent(streamsUtil.readContentAsBytes(photoInStream));
				} catch (IOException e) {
					String errorMessage = getString(R.string.exception_reading_image_file) + e.toString();
					Log.e(tag, errorMessage, e);
					Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
				}
			}
			// handle location
			/*ud.setLatitude("");
			ud.setLongitude("");
			if(currentLocation != null ) {
				ud.setLatitude(String.valueOf(currentLocation.getLatitude()));
				ud.setLongitude(String.valueOf(currentLocation.getLongitude()));
			}*/
			manageGpsLocation(ud);
			return ud;
		}
	}
	
	private void resetFormData() {
		removeImage();
		commentTextView.setText("");
	}
	
	private void removeImage() {
		bitmapPhoto = null;
		photoTaken = false;
		photoImageView.setImageResource(android.R.drawable.ic_menu_camera);
		removePhotoButton.setEnabled(false);
	}
	
	/**
	 * Handle photo is taken or GPS device is enabled/disabled
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(tag, "requesttCode: " + requestCode);
		
		// Handle take a photo activity
		if (requestCode == takeAPhotoActivity) {
			switch (resultCode) {
				case RESULT_CANCELED:
					Log.i(tag, "User cancelled taking a photo");
					break;
				case RESULT_OK:
					onPhotoTaken();
					break;
			}
		}
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(tag, "onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean(PHOTO_TAKEN)) {
			onPhotoTaken();
			commentTextView.setText(savedInstanceState.getString(UploadData.PROPERTY_COMMENT));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(PHOTO_TAKEN, photoTaken);
		outState.putString(UploadData.PROPERTY_COMMENT, commentTextView.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		startListenLocationUpdates();
	}
	
/*	@Override
	protected void onStop() {
		stopListenLocationUpdates();
		super.onStop();
	}*/

	
	/**
	 * Vlidate upload data 
	 * 
	 * @param rd is object to be validated
	 * @return list of errors or empty list if there is no errors
	 */
	public List<String> validate(UploadData ud) {
		List<String> errorMessage = new ArrayList<String>();
		if((ud.getComment() == null) || (ud.getComment().length() == 0)) {
			errorMessage.add((String) getText(R.string.comment_mandatory));
		}
		return errorMessage;
	}

	
	public void manageGpsLocation(UploadData ud) {
		// handle location
		ud.setLatitude("");
		ud.setLongitude("");
		if(currentLocation != null ) {
			if((currentLocation.getLatitude() > 41d) && (currentLocation.getLatitude() < 47d)) {
				ud.setLatitude(String.valueOf(currentLocation.getLatitude()));	
			}
			if((currentLocation.getLongitude() > 18d) && (currentLocation.getLongitude() < 23d)) {
				ud.setLongitude(String.valueOf(currentLocation.getLongitude()));	
			}
		}
	}
	
	private void showValidationErrors(List<String> validationErrors) {
		String message = "";
		for(String validationError : validationErrors) {
			message += validationError; 
		}
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.ok_ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
					});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	// ----------------------------------------------------- Following section is Camera related code
	
	/**
	 * Starts camera activity
	 */
	protected void startCameraIntent() {
		Log.i(tag, "startCameraActivity()");

		File file = new File(photoPath);
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		
		startActivityForResult(intent, takeAPhotoActivity);
	}

	/**
	 * Handle photo is taken
	 */
	protected void onPhotoTaken() {
		Log.i(tag, "onPhotoTaken");
		photoTaken = true;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		bitmapPhoto = BitmapFactory.decodeFile(photoPath, options);
		photoImageView.setImageBitmap(bitmapPhoto);
		removePhotoButton.setEnabled(true);
	}
	
	// ----------------------------------------------------- Following section is GPS and Network related code
	
	/**
	 * Checks if phone has gps sensor
	 * 
	 * @return
	 */
	private boolean gpsSensorExist() {
		List<String> allProviders = locationManager.getAllProviders();
		boolean gpsSensorExist = false;
		for(String provider: allProviders) {
			if(provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
				gpsSensorExist = true;
				break;
			}
		}
		return gpsSensorExist;
	}

	/**
	 * Start listening location changes update
	 */
	private void startListenLocationUpdates() {
		if(gpsSensorExist() && gpsIsEnabled()) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	/**
	 * Stop listening location changes update
	 *//*
	private void stopListenLocationUpdates() {
		if(locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
	}*/

	/**
	 * 
	 * @return true if GPS device is enabled
	 */
	private boolean gpsIsEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	
	/**
	 * Show user phone settings where he/she can enable GPS device
	 */
	private void startGPSSettingsIntent() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(getText(R.string.gps_setting))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(callGPSSettingIntent, findGpsLocation);
							}
						});
		alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	/**
	 * 
	 * Listen for location changes
	 *
	 */
	class UserLocationListener implements LocationListener {
	    @Override
		public void onLocationChanged(Location location) {
	    	if(location != null) {
	    		if(isBetterLocation(location, currentLocation)) {
	    			currentLocation  = location;
	    		}
	    	} else {
	    		currentLocation = location;
	    	}
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }

	    public void onProviderEnabled(String provider) {
	    }

	    public void onProviderDisabled(String provider) {
	    }
	  }
	
	/**
	 * Determines whether is one location reading better than the current recorded location
	 * 
	 * @param location is new location that we want to evaluate
	 * @param currentBestLocation is current location to which we want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_SECONDS;
	    boolean isSignificantlyOlder = timeDelta < -TWO_SECONDS;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/**
	 * Checks whether two providers are the same
	 * 
	 * @param provider1
	 * @param provider2
	 * @return
	 */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	
	private void registerActivity() {
		Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
		MainActivity.this.startActivity(intent);
	}
}