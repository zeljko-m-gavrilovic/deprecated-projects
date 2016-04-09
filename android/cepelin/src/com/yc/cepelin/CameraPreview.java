package com.yc.cepelin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class CameraPreview extends Activity 
//implements SensorListener 
{
	private static final String TAG = "CameraPreview";
	private static final boolean debug = false;

	private Preview mPreview;
	private int itemId;
	private boolean pictureTaken;

	private Button cameraRotate;
	private Button cameraSend;
	private Button cameraSave;
	private Button cameraShoot;
	private ImageView cameraImage;

	private Drawable drawable;
	private Bitmap bitmap;
	private ProgressBar progressBar;
	
//	private SensorManager sensorManager;
	private int orientation;
//	private int sensor = SensorManager.SENSOR_ORIENTATION;

	// *************************
	
	private OrientationEventListener mListener;
	
//	  /** Register for the updates when Activity is in foreground */
//	  @Override
//	  protected void onResume() {
//	    super.onResume();
//	    Log.d(TAG, "onResume");
//	    sensorManager.registerListener(this, sensor);
//	  }
//
//	  /** Stop the updates when Activity is paused */
//	  @Override
//	  protected void onPause() {
//	    super.onPause();
//	    Log.d(TAG, "onPause");
//	    sensorManager.unregisterListener(this, sensor);
//	  }
//
//	  public void onAccuracyChanged(int sensor, int accuracy) {
//	    Log.d(TAG, String.format("onAccuracyChanged  sensor: %d   accuraccy: %d",
//	        sensor, accuracy));
//	  }
//
//	  public void onSensorChanged(int sensorReporting, float[] values) {
//	    if (sensorReporting != sensor)
//	      return;
//
//	    float azimuth = Math.round(values[0]);
//	    float pitch = Math.round(values[1]);
//	    float roll = Math.round(values[2]);
//
//	    String out = String.format("Azimuth: %.2f <=> Pitch: %.2f <=> Roll: %.2f", azimuth, pitch, roll);
////		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + out);
//	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".onCreate()");

		super.onCreate(savedInstanceState);

        itemId = getIntent().getIntExtra("id", -1);

		// validate itemId value
		if (-1 == itemId) {
			Toast.makeText(this, getString(R.string.object_with_illegal_id), Toast.LENGTH_LONG);
			return;
		}

		Toast.makeText(CameraPreview.this, getString(R.string.tap_screen_to_take_picture), Toast.LENGTH_LONG).show();
		
		// Hide the window title.
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 

		// Create our Preview view and set it as the content of our activity.
		mPreview = new Preview(this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(mPreview);

//		// Locate the SensorManager using Activity.getSystemService
//		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		 
//		// Register your SensorListener
//		sensorManager.registerListener(this, sensor);
		
//		**********************************
		
		mListener = new OrientationEventListener (this) {
			public void onOrientationChanged (int orientation) {
				if (debug) Log.i (TAG, "onOrientationChanged, orientation: " + orientation);
				CameraPreview.this.orientation = orientation;
			}
		};
		
		mListener.enable ();
//		Sensor s = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//		sensorManager.registerListener(mListener, s, SensorManager.SENSOR_DELAY_UI);
	}

	private void progressEnabled(boolean enabled) {
		if (enabled) progressBar.setVisibility(View.VISIBLE);
		else progressBar.setVisibility(View.INVISIBLE);

		setProgressBarIndeterminateVisibility(enabled);
		setProgressBarVisibility(enabled);
	}

	private void save(String name) {
		try {
			if (null == name || name.equals("")) {
				// if user clear the text
				name = "cepelin_" + System.currentTimeMillis() + ".png";
			} else {
				// if user entered some text
				name = name + ".png";
			}
			
			// save to SD card
			MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, name, "");
			
			// set this flag to true if you want to prevent user from multiple saving the same image
			if (GalleryConstants.SAVE_ONCE_ENABLED) cameraSave.setEnabled(false);
			
			if (debug) Log.w(TAG, GalleryUtil.getLineDescription() + "Image is saved to SD card.");
			Toast toast = Toast.makeText(this, getString(R.string.picture_saved_1) + " " + name + "\n" + getString(R.string.picture_saved_2), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		} catch (Exception e) {
			// in case of some problems
			Log.w(TAG, GalleryUtil.getLineDescription() + "Can't save image to SD card! Error: " + e.getMessage(), e);
			Toast.makeText(this, getString(R.string.can_not_save_picture), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void rotate(int degrees) throws java.lang.OutOfMemoryError {
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".rotate(" + degrees + ")");
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		// createa matrix for the manipulation 
		Matrix matrix = new Matrix();

		// rotate the Bitmap
		matrix.postRotate(degrees); 

		// recreate the new Bitmap 
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true); 

		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		drawable = (Drawable) bitmapDrawable;
		cameraImage.setImageDrawable(drawable);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Handler.handleMessage(" + msg.arg1 + ")");

			setProgressBarIndeterminateVisibility(false);
			try {
				progressBar.setVisibility(View.INVISIBLE);
			} catch (Exception e) {
				Log.w(TAG, GalleryUtil.getLineDescription() + "ProgressBar not yet initialized.");
			}

			if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_TAKE_IMAGE){
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Handler je primio HANDLER_MESSAGE_TAKE_IMAGE poruku.");
				setContentView(R.layout.camera_activity);
	    		
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				progressBar = (ProgressBar) findViewById(R.id.cameraProgressBar);
				
				// rotate button
	    		cameraRotate = (Button) findViewById(R.id.cameraRotate);
	    		cameraRotate.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
			    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "cameraRotate.onClick()");
			    		progressEnabled(true);
			    		try {
			    			rotate(90);
			    		} catch (OutOfMemoryError e) {
			    			Log.e(TAG, GalleryUtil.getLineDescription() + "Not enough memory to complete rotate operation! Error: " + e.getMessage());
			    			Toast.makeText(CameraPreview.this, getString(R.string.not_enough_memory_to_rotate), Toast.LENGTH_LONG).show();
			    		}
			    		progressEnabled(false);
					}
				});
	    		
	    		// send button
				cameraSend = (Button) findViewById(R.id.cameraSend);
				cameraSend.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "cameraSend.onClick()");

			    		CameraPreview camera = CameraPreview.this;
			    		camera.setProgressBarIndeterminateVisibility(true);
			    		camera.progressBar.setVisibility(View.VISIBLE);
						camera.cameraSend.setEnabled(false);
						camera.setProgressBarIndeterminateVisibility(true);
						camera.setProgressBarVisibility(true);
			    		new CameraPreview.AddPhotoForItemTask().execute();
					}
				});
	    		
				// shoot button
	    		cameraShoot = (Button) findViewById(R.id.cameraShoot);
	    		cameraShoot.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
			    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "cameraShoot.onClick()");
						Intent intent = new Intent(CameraPreview.this, CameraPreview.class);
						intent.putExtra("id", itemId);
						CameraPreview.this.finish();
						CameraPreview.this.startActivity(intent);
					}
				});
	    		
	    		// save button
	    		cameraSave = (Button) findViewById(R.id.cameraSave);
	    		cameraSave.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
			    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "cameraSave.onClick()");
						final Dialog dialog = new Dialog(CameraPreview.this);
						dialog.setContentView(R.layout.image_name);
						dialog.setTitle("Naziv slike");
						final EditText imageName = (EditText) dialog.findViewById(R.id.imageNameEdit);
						imageName.setText("cepelin_" + System.currentTimeMillis());
						Button confirmButton = (Button) dialog.findViewById(R.id.imageNameAdd);
						confirmButton.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								CameraPreview.this.save(imageName.getText().toString());
								dialog.dismiss();
							}
						});
						dialog.show();
					}
				});
	    		
	    		cameraImage = (ImageView) findViewById(R.id.cameraImage);
	    		cameraImage.setImageDrawable(drawable);
	    		cameraImage.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
			    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "cameraImage.onClick()");
			    		CameraPreview.this.cameraRotate.performClick();
					}
				});
	    		// auto-rotate
    			if (orientation < 30 || orientation > 330) rotate (90);
    			else if (orientation > 60 && orientation < 120) rotate (180);
    			else if (orientation > 150 && orientation < 210) rotate (270);

        	} else if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_IMAGE_NOT_SENT) {
				Toast.makeText(CameraPreview.this, getString(R.string.picture_not_sent_try_later), Toast.LENGTH_SHORT).show();
        		if (GalleryConstants.RESEND_ENABLED) cameraSend.setEnabled(true); // omogućiti da korisnik može da pokuša opet da pošalje sliku u slučaju neuspeha
        		setProgressBarIndeterminateVisibility(false);
				setProgressBarVisibility(false);
			} else if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_IMAGE_SENT) {
				Toast.makeText(CameraPreview.this, getString(R.string.picture_sent), Toast.LENGTH_SHORT).show();
				setProgressBarIndeterminateVisibility(false);
				setProgressBarVisibility(false);
        	}

        }
	}; 

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + "Preview.onKeyDown(); event.getAction()=" + event.getAction());
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch(keyCode){
				case KeyEvent.KEYCODE_CAMERA: // take picture (if not taken already)
					if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "KEYCODE_CAMERA event!");
					if (null != mPreview) {
			    		mListener.disable();
						mPreview.takePicture();
					} else {
						finish();
					}
					return true; // this event is now consumed
				case KeyEvent.KEYCODE_BACK: // go back and remove toast if any
					if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "KEYCODE_BACK event!");
					finish();
					return true; // this event is now consumed
				}
			}
		return super.onKeyDown(keyCode, event);
	}

	class AddPhotoForItemTask extends AsyncTask<Void, Void, Void> {
	    protected Void doInBackground(Void... voids) {
			DataService dataService = DataService.getInstance(CameraPreview.this);
			dataService.addPhotoForItem(itemId, drawable, new Callback<Integer>() {
				@Override
				public void onFinish(Integer result) {
					if (CameraPreview.debug) Log.i(CameraPreview.TAG, GalleryUtil.getLineDescription() + "dataService.addPhotoForItem() > onFinish()");
					
					Message msg = new Message();

					if (null == result) {
						Log.w(CameraPreview.TAG, GalleryUtil.getLineDescription() + "Rezultat je null!");
						msg.arg1 = GalleryConstants.HANDLER_MESSAGE_IMAGE_NOT_SENT;
					} else if (!GalleryConstants.ADD_PHOTO_FOR_ITEM_CHECK_STATUS
							|| (GalleryConstants.ADD_PHOTO_FOR_ITEM_CHECK_STATUS && GalleryConstants.ADD_PHOTO_FOR_ITEM_RESULT_STATUS == result)) {
						if (CameraPreview.debug) Log.i(CameraPreview.TAG, GalleryUtil.getLineDescription() + "Slika je uspešno poslata na server. Status kod " + result);
						msg.arg1 = GalleryConstants.HANDLER_MESSAGE_IMAGE_SENT;
					} else {
						Log.w(CameraPreview.TAG, GalleryUtil.getLineDescription() + "Rezultat nije poslat na server. Status kod je " + result);
						msg.arg1 = GalleryConstants.HANDLER_MESSAGE_IMAGE_NOT_SENT;
					}
					handler.sendMessage(msg);
				}
			});
			return null;
	    }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "old rotation: " + getResources().getConfiguration().orientation + ", new rotation: " + newConfig.orientation);
	}
	
	class Preview extends SurfaceView implements SurfaceHolder.Callback {
		SurfaceHolder mHolder;
		Camera mCamera;
		
		Preview(Context context) {
			super(context);
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "new Preview()");

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Preview.surfaceCreated()");
			// The Surface has been created, acquire the camera and tell it where
			// to draw.
			mCamera = Camera.open();
			/*
			if (GalleryConstants.RESIZE_ENABLED_METHOD_1) {
				Camera.Parameters params = mCamera.getParameters();
				Camera.Size previewSize = params.getPreviewSize();
				params.setPictureSize(previewSize.width, previewSize.height);
				params.setPreviewFormat(PixelFormat.RGB_565);
				mCamera.setParameters(params);
			}
			
			// debug info
			if (debug) {
				Camera.Size cSize = mCamera.getParameters().getPictureSize();
				Log.i(TAG, GalleryUtil.getLineDescription() + "picture size width: " + cSize.width + ", height: " + cSize.height);
			
				Camera.Size pSize = mCamera.getParameters().getPreviewSize();
				Log.i(TAG, GalleryUtil.getLineDescription() + "preview size width: " + pSize.width + ", height: " + pSize.height);

				Log.i(TAG, GalleryUtil.getLineDescription() + "picture format: " + mCamera.getParameters().getPictureFormat() + " (constant values can be found in PixelFormat class javadoc)");
				Log.i(TAG, GalleryUtil.getLineDescription() + "preview format: " + mCamera.getParameters().getPreviewFormat() + " (constant values can be found in PixelFormat class javadoc)");
			}
*/
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
				// TODO: add more exception handling logic here
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Preview.surfaceDestroyed()");
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's very
			// important to release it when the activity is paused.
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Preview.surfaceChanged()");
			// Now that the size is known, set up the camera parameters and begin
			// the preview.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h);
			parameters.setPreviewFormat(PixelFormat.RGB_565);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

		public void takePicture() {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Preview.takePicture()");
			if (pictureTaken) {
				return;
			} else {
				pictureTaken = true;
				mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		}

		ShutterCallback shutterCallback = new ShutterCallback() {
			public void onShutter() {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "ShutterCallback.onShutter()");
			}
		};

		PictureCallback rawCallback = new PictureCallback() {
			public void onPictureTaken(byte[] _data, Camera _camera) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "PictureCallback.onPictureTaken() > rawCallback");
			}
		};

		PictureCallback jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] _data, Camera _camera) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "PictureCallback.onPictureTaken() > jpegCallback");

				// remove listener
//				CameraPreview.this.sensorManager.unregisterListener(CameraPreview.this, sensor);
				
				bitmap = null;
				try {
					bitmap = BitmapFactory.decodeByteArray(_data, 0, _data.length);
				} catch (Exception e) {
					Log.e(CameraPreview.TAG, "Greška pri kreiranju bitmape. Error: " + e.getMessage(), e);
					Toast.makeText(CameraPreview.this, getString(R.string.can_not_create_bitmap), Toast.LENGTH_LONG).show();
					finish();
				}

				// not needed to resize if picture size already set to preview size in surfaceCreated method
				if (GalleryConstants.RESIZE_ENABLED_METHOD_2) {
					int height = bitmap.getHeight();
					int width = bitmap.getWidth();
					float scaleFactorHeight = ((float)200)/height;
//					float scaleFactorWidth = ((float)480)/width;

					// createa matrix for the manipulation 
					Matrix matrix = new Matrix();

					// resize the bit map
					if (debug) {
						Log.i(TAG, GalleryUtil.getLineDescription() + "old height: " + width + "px, old width: " + height + " px");
						Log.i(TAG, GalleryUtil.getLineDescription() + "new height: " + ((float)width)*scaleFactorHeight + "px, new width: " + ((float)height)*scaleFactorHeight + " px");
					}
					matrix.postScale(scaleFactorHeight, scaleFactorHeight);

					// recreate the new Bitmap 
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true); 
//					bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width/scaleFactorHeight), (int)(height/scaleFactorHeight), false); // scale picture

		    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "picture before size: " + _data.length + " bytes, height: " + height + " px, width: " + width + " px");
				}

				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				drawable = (Drawable) bitmapDrawable;

				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "picture after height:  " + bitmap.getHeight() + " px, width: " + bitmap.getWidth() + " px");

				Message msg = new Message();
				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_TAKE_IMAGE;
				handler.sendMessage(msg);
			}
		};

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + "Preview.onTouchEvent(); event.getAction()=" + event.getAction());
			if (event.getAction() == MotionEvent.ACTION_UP) takePicture();
			return true; // this event is now consumed
		}
	}
}