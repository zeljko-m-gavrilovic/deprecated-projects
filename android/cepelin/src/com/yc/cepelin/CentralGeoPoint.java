package com.yc.cepelin;

import com.google.android.maps.GeoPoint;

public class CentralGeoPoint extends GeoPoint {

	private int zoomLevel;
	
	public CentralGeoPoint (int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6);
	}
	
	public CentralGeoPoint (int latitudeE6, int longitudeE6, int zoomLevel) {
		super(latitudeE6, longitudeE6);
		this.zoomLevel = zoomLevel;
	}
	
	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
}