package com.yc.cepelin;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationActivity extends Activity {

	protected double latitude, longitude;
	protected LocationManager locationManager;
	protected LocationListener locationListener;

	protected void init() {
		// initialize geo coordinates
		double[] geoCoordinates = GeoUtil.getLastKnownLocation(this);
		latitude = geoCoordinates[0];
		longitude = geoCoordinates[1];

		// start manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// start listener
		locationListener = new MyLocationListener();
		
		// request updates
		requestLocationUpdates();
	}
    
	protected class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				latitude = loc.getLatitude();
				longitude = loc.getLongitude();
			}
		}
		
		public void onProviderDisabled(String provider) {
		}
		
		public void onProviderEnabled(String provider) {
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	protected void requestLocationUpdates() {
		if (null != locationManager) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 20f, locationListener);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 20f, locationListener);
		}
	}
	
	protected void removeUpdates() {
		if (null != locationManager) locationManager.removeUpdates(locationListener);
	}

	@Override
    protected void onResume() {
    	super.onResume();
    	requestLocationUpdates();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	removeUpdates();
    }
}