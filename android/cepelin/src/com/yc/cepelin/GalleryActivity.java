package com.yc.cepelin;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class GalleryActivity extends Activity {
	protected static final String TAG = "GalleryActivity";
	protected static final boolean debug = false;

	protected DataService dataService;
	protected int itemId;
	protected int photoId;
	protected ArrayList<Integer> ids;
	protected ImageView image;
	protected Button previous;
	protected Button next;
	protected Button shoot;
	protected TextView text;
	protected int currentImage;
	protected Drawable drawable;
	protected ProgressBar progressBar;
	protected boolean fullScreen;
	
	protected boolean isPressed;
	protected float xPressed;
	protected float yPressed;
	protected final static float X_THRESHOLD = 100; // in pixels; abs(x_start - x_end) must not be smaller then threshold value
	protected final static float Y_THRESHOLD = 150; // in pixels; abs(y_start - y_end) must not be greater then threshold value
	
	protected boolean firstClick = false;
	protected boolean secondClick = false;

	protected SharedPreferences shared;
	protected long timestamp;

	protected OnTouchListener imageOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "image > onTouch(" + event.getAction() + ")");
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
//				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "ACTION_DOWN");
				isPressed = true;
				xPressed = event.getX();
				yPressed = event.getY();
				if (fullScreen) {
					GalleryActivity.this.showButtons();
					GalleryActivity.this.hideAfterDelay();
				}
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
//				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "ACTION_UP");
				if (isPressed) {
//					if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "GESTURE");
					isPressed = false;
					float xDifference = event.getX() - xPressed;
					float yDifference = event.getY() - yPressed;
					if (Math.abs(yDifference) < Y_THRESHOLD) {
						if (xDifference > X_THRESHOLD) {
							if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "GESTURE_LEFT");
							if (previous.isShown()) previous.performClick();
						} else if (xDifference < -X_THRESHOLD) {
							if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "GESTURE_RIGHT");
							if (next.isShown()) next.performClick();
						} else {
							GalleryActivity.this.doubleClickCalculate();
						}
					}
				}
			} else if (MotionEvent.ACTION_CANCEL == event.getAction()) {
//				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "ACTION_CANCEL");
				isPressed = false;
			}
			return true;
		}
	};

	protected void hideAfterDelay() {
		timestamp = System.currentTimeMillis();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Log.w(TAG, GalleryUtil.getLineDescription() + "Can't sleep! Error: " + e.getMessage(), e);
					return;
				}
				if (System.currentTimeMillis() - timestamp > 2000) {
	 				Message msg = new Message();
	 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_HIDE_BUTTONS;
					GalleryActivity.this.handler.sendMessage(msg);
				}
			}
		}.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".onCreate()");

		super.onCreate(savedInstanceState);

		itemId = getIntent().getIntExtra("id", -1);
		fullScreen = getIntent().getBooleanExtra("fullscreen", false);

		// validate itemId value
		if (-1 == itemId) {
			Toast.makeText(this, getString(R.string.no_pictures_for_object), Toast.LENGTH_LONG).show();
			return;
		}

		// load current image id (in case going to/back from fullscrean)
		shared = getSharedPreferences("gallery", 0);

		if (fullScreen) {
			currentImage = shared.getInt("current_image", 0);

			// Hide the window title.
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
			setContentView(R.layout.gallery_fullscreen);
		} else {
			setContentView(R.layout.gallery_fullscreen);
			getParent().setProgressBarIndeterminateVisibility(true);
//			getParent().setProgressBarVisibility(true);
		}

		// find views only once in activity onCreate method
		image = (ImageView) findViewById(R.id.galleryImage);
		previous = (Button) findViewById(R.id.galleryPrevious);
		next = (Button) findViewById(R.id.galleryNext);
		shoot = (Button) findViewById(R.id.galleryShoot);
		text = (TextView) findViewById(R.id.galleryText);
		progressBar = (ProgressBar) findViewById(R.id.galleryProgressBar);

		previous.bringToFront();
		next.bringToFront();
		shoot.bringToFront();

		// set listeners
		image.setOnTouchListener(imageOnTouchListener);

		previous.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + "previous button > onClick()");
				GalleryActivity.this.previousButtonClick();
			}
		});

		next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + "next button > onClick()");
				GalleryActivity.this.nextButtonClick();
			}
		});

		shoot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + "shot button > onClick()");
				Intent intent = new Intent(GalleryActivity.this, CameraPreview.class);
				intent.putExtra("id", itemId);
				startActivity(intent);
			}
		});

		dataService = DataService.getInstance(GalleryActivity.this);
		new GetPhotoIdsTask().execute();
		
//		if (fullScreen) hideAfterDelay();
//		if (!fullScreen) updateButtons();
	}

	protected void doubleClickConsume() {
		// save current image id
		SharedPreferences.Editor editor = shared.edit();
		editor.putInt("current_image", currentImage);
		editor.commit();

		if (!fullScreen) GalleryActivity.this.startActivityForResult(new Intent(GalleryActivity.this, GalleryActivity.class).putExtra("id", itemId).putExtra("fullscreen", true),0);
		else {
			setResult(RESULT_OK, new Intent().putExtra("current_image", currentImage));
			finish();
		}
	}

	public void doubleClickCalculate() {
		if (!firstClick) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "first click");
			firstClick = !firstClick;
			new Thread() {
				public void run() {
					if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "1 new Thread().run()");
					try {
						Thread.sleep(500);
						firstClick = !firstClick;
						if (secondClick) {
							if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "double click event!");
							//TODO fullscreen event implementation
							GalleryActivity.this.doubleClickConsume();
						}
					} catch (InterruptedException e) {
						Log.w(TAG, GalleryUtil.getLineDescription() + "Can't run the 1st thread! Error: " + e.getMessage(), e);
					}
				}
			}.start();
		} else {
			if (!secondClick) {
				if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "second click");
				secondClick = !secondClick;
				new Thread() {
					public void run() {
						if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "2 new Thread().run()");
						try {
							Thread.sleep(500);
							secondClick = false;
						} catch (InterruptedException e) {
							Log.w(TAG, GalleryUtil.getLineDescription() + "Can't run the 2nd thread! Error: " + e.getMessage(), e);
						}
					}
				}.start();
			}
		}
	}

	protected void previousButtonClick(){
		if (!fullScreen) getParent().setProgressBarIndeterminateVisibility(true);
		progressBar.setVisibility(View.VISIBLE);
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".previousButtonClick()");
		if (0 == currentImage) return;
		currentImage--;
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "currentImage = " + currentImage + ", ids[" + currentImage + "] = " + ids.get(currentImage));
		photoId = ids.get(currentImage);
		disableButtons();
		new GetImageTask().execute();
		if (fullScreen) hideAfterDelay();
	}

	protected void nextButtonClick(){
		if (!fullScreen) getParent().setProgressBarIndeterminateVisibility(true);
		progressBar.setVisibility(View.VISIBLE);
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".nextButtonClick()");
		if (ids.size()-1 == currentImage) return;
		currentImage++;
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "currentImage = " + currentImage + ", ids[" + currentImage + "] = " + ids.get(currentImage));
		photoId = ids.get(currentImage);
		disableButtons();
		new GetImageTask().execute();
		if (fullScreen) hideAfterDelay();
	}

	protected void disableButtons() {
		previous.setEnabled(false);
		next.setEnabled(false);
	}

	protected void updateButtons() {
		previous.setEnabled(true);
		next.setEnabled(true);
		if (0 == currentImage) {
			previous.setVisibility(View.INVISIBLE);
		} else {
			previous.setVisibility(View.VISIBLE);
		}
		if (null==ids || ids.size()-1 == currentImage) {
			next.setVisibility(View.INVISIBLE);
		} else {
			next.setVisibility(View.VISIBLE);
		}
		if (!fullScreen) getParent().setProgressBarIndeterminateVisibility(false);
//		progressBar.setVisibility(View.INVISIBLE);
	}

	protected void populateImageHolder (Drawable drawable) {
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".populateImageHolder()");
		image.setImageDrawable(drawable);
	}

	protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
    		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Handler.handleMessage(" + msg.arg1 + ")");
        	if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_UPDATE_IMAGE){
        		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "Handler je primio HANDLER_MESSAGE_UPDATE_IMAGE poruku.");
	        	image.setVisibility(View.VISIBLE);
				populateImageHolder(drawable);
	        	updateButtons();
	        	if (fullScreen) hideAfterDelay();
	        	progressBar.setVisibility(View.INVISIBLE);
				text.setVisibility(View.INVISIBLE);
        	} else if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_SHOW_BUTTONS){
        		showButtons();
        	} else if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_HIDE_BUTTONS){
        		hideButtons();
        	} else if(msg.arg1 == GalleryConstants.HANDLER_MESSAGE_NO_IMAGES){
				text.setVisibility(View.VISIBLE);
				if (!fullScreen) {
					progressBar.setVisibility(View.INVISIBLE);
					getParent().setProgressBarIndeterminateVisibility(false);
				}
        	}
        }
	}; 

	//------------------------------------- GetPhotoIdsTask ------------------------------------

	protected class GetPhotoIdsTask extends AsyncTask<Void, Void, Boolean> {
		private GalleryActivity gallery;
		private boolean doUpdate;
	    protected Boolean doInBackground(Void... voids) {
	    	gallery = GalleryActivity.this;
	    	if (GalleryActivity.debug) Log.i(GalleryActivity.TAG, GalleryUtil.getLineDescription() + "GetPhotoIdsTask.doInBackground()");
	    	gallery.dataService.getPhotoIdsForItem(gallery.itemId, new Callback<ArrayList<Integer>>() {
				@Override
				public void onFinish(ArrayList<Integer> result) {
					if (GalleryActivity.debug) Log.i(GalleryActivity.TAG, GalleryUtil.getLineDescription() + "dataService.getPhotoIdsForItem() > onFinish()");

					if (null == result) {
						Log.w(GalleryActivity.TAG, GalleryUtil.getLineDescription() + "Lista id-eva je null!");

		 				Message msg = new Message();
		 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_NO_IMAGES;
		 				handler.sendMessage(msg);
					} else if (0 == result.size()) {
						Log.w(GalleryActivity.TAG, GalleryUtil.getLineDescription() + "Lista id-eva je prazna!");

		 				Message msg = new Message();
		 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_NO_IMAGES;
		 				handler.sendMessage(msg);
					} else {
						if (GalleryActivity.debug) Log.i(GalleryActivity.TAG, "Vraćeni objekat je OK.");

						gallery.ids = result;
						if (!fullScreen) gallery.photoId = gallery.ids.get(0);
						else  gallery.photoId = gallery.ids.get(currentImage);
						new GetImageTask().execute();
						doUpdate = true;
					}
				}
			});
			return doUpdate;
	    }
	}

	//------------------------------------- GetImageTask ------------------------------------

	protected class GetImageTask extends AsyncTask<Void, Void, Boolean> {
		private GalleryActivity gallery;
		private boolean doUpdate;
	    protected Boolean doInBackground(Void... voids) {
	    	gallery = GalleryActivity.this;
	    	if (GalleryActivity.debug) Log.i(GalleryActivity.TAG, GalleryUtil.getLineDescription() + "GetImageTask.doInBackground()");
	    	gallery.dataService.getPhotoForItem(gallery.itemId, gallery.photoId, new Callback<Drawable>() {
				public void onFinish(Drawable result) {
					if (GalleryActivity.debug) Log.i(TAG, GalleryUtil.getLineDescription() + "dataService.getPhotoForItem() > onFinish()");

					if (null == result) {
						Log.w(TAG, GalleryUtil.getLineDescription() + "Drawable objekat je null!");

		 				Message msg = new Message();
		 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_NO_IMAGES;
		 				handler.sendMessage(msg);
					} else {
						if (GalleryActivity.debug) Log.i(GalleryActivity.TAG, "Vraćeni objekat je OK.");
						gallery.drawable = result;
						doUpdate = true;
						
		 				Message msg = new Message();
		 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_UPDATE_IMAGE;
		 				handler.sendMessage(msg);
					}
				};
			});
			return doUpdate;
	    }

	    protected void onPostExecute(Boolean doUpdate) {
	    	if (doUpdate) {
 				Message msg = new Message();
 				msg.arg1 = GalleryConstants.HANDLER_MESSAGE_UPDATE_IMAGE;
 				handler.sendMessage(msg);
	    	}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item = menu.add(getText(R.string.full_screen));
		item.setIcon(android.R.drawable.ic_menu_view);

		item = menu.add(getText(R.string.normal_screen));
		item.setIcon(android.R.drawable.ic_menu_view);

		item = menu.add(getText(R.string.refresh));
		item.setIcon(android.R.drawable.ic_menu_rotate);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (null == drawable) {
			// no pictures
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(false);
			menu.getItem(2).setVisible(true);
		} else if (fullScreen) {
			// full screen
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(true);
			menu.getItem(2).setVisible(false);
		} else {
			// normal screen
			menu.getItem(0).setVisible(true);
			menu.getItem(1).setVisible(false);
			menu.getItem(2).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.refresh))) {
			// only in normal screen
			getParent().setProgressBarIndeterminateVisibility(true);
			progressBar.setVisibility(View.VISIBLE);
			text.setVisibility(View.INVISIBLE);
			currentImage = 0;
//			photoId = ids.get(currentImage);
			new GetPhotoIdsTask().execute();
		}
		else doubleClickConsume();
		return true;
	}
	
	public void showButtons() {
		updateButtons();
		shoot.setVisibility(View.VISIBLE);
	}

	public void hideButtons() {
		previous.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		shoot.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + TAG + ".onActivityResult()");
		if (resultCode == RESULT_OK) {
			if (debug) Log.i(TAG, GalleryUtil.getLineDescription() + "result is OK");
			currentImage = data.getIntExtra("current_image", currentImage);
			photoId = ids.get(currentImage);
			disableButtons();
			new GetImageTask().execute();
		}
	}
}