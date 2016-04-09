package com.yc.cepelin;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;

public class GeoUtil {

	public static final int	EARTH_RADIUS_KM = 6371;
	public static final double SCALE = 1.0E6;

	public static GeoPoint point(double lat, double lon) {
		return new GeoPoint((int) (lat * SCALE), (int) (lon * SCALE));
	}

	public static CentralGeoPoint getCentralGeoPoint (double minLat, double maxLat, double minLng, double maxLng, int width, int height) {
		CentralGeoPoint cgp = new CentralGeoPoint((int) ((minLat + maxLat)/2 * 1E6), (int) ((minLng + maxLng)/2 * 1E6));

		final int offset = 52; // this is needed for title and status bar
		
		// longitudes can have value in range [0,360]; it's 360 degrees totally
		double zoomLevelLngDouble = Math.log(360*(width - offset)/256/(maxLng-minLng))/Math.log(2);
		int zoomLevelLng = new Double(Math.ceil(zoomLevelLngDouble)).intValue();
		
		// latitudes can have value in range [-90,90]; it's 180 degrees totally
		double zoomLevelLatDouble = Math.log(180*(height - offset)/256/(maxLat-minLat))/Math.log(2);
		int zoomLevelLat = new Double(Math.ceil(zoomLevelLatDouble)).intValue();
		
		// smaller zoom level must be selected in order to have all longitudes/latitudes shown on the same screen
		if (zoomLevelLat < zoomLevelLng) {
			cgp.setZoomLevel(zoomLevelLat);
		} else {
			cgp.setZoomLevel(zoomLevelLng);
		}
		return cgp;
	}

	public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
		double lat1Rad = Math.toRadians(lat1);
		double lat2Rad = Math.toRadians(lat2);
		double deltaLonRad = Math.toRadians(lon2 - lon1);
		return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
	}


	/**
	 * Return distance in metres
	 * @param lat1 Latitude of point A
	 * @param lon1 Longitude of point A
	 * @param lat2 Latitude of point B
	 * @param lon2 Longitude of point B
	 * @return Distance in metres between point A and B
	 */
	public static int getDistance(double lat1, double lon1, double lat2, double lon2) {
		return (int)(1000 * GeoUtil.distanceKm(lat1, lon1, lat2, lon2));
    }

	public static double[] getLastKnownLocation(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location l = null;

		for (int i = providers.size() - 1; i >= 0; i--) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}

		double[] gps = new double[2];
		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}
}