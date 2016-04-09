package com.yc.cepelin;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import java.util.List;

/**
 * Utility that holds all data regarding Google Map, used in application.
 * Simple encapsulates all work with maps.
 * @version 2
 */
public class GooMap {

	private final MapView mapView;
	private final MapController mapCtrl;

	public GooMap(MapView mapView, int zoom, boolean isSatelliteView) {
		this.mapView = mapView;
		this.mapCtrl = mapView.getController();

		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.setEnabled(true);
		setSatellite(isSatelliteView);
		mapCtrl.setZoom(zoom);
		mapView.getOverlays().clear();
	}

	public MapView getMapView() {
		return mapView;
	}

	public int getZoom() {
		return mapView.getZoomLevel();
	}

	public void setZoom(int zoom) {
		mapCtrl.setZoom(zoom);
	}

	public GeoPoint getMapCenter() {
		return mapView.getMapCenter();
	}

	/**
	 * Centers and pans to position.
	 */
	public void centerTo(GeoPoint geoPoint) {
		mapCtrl.setCenter(geoPoint);
		mapCtrl.animateTo(geoPoint);
		redraw();
	}

	/**
	 * Pan to position.
	 */
	public void panTo(GeoPoint geoPoint) {
		mapCtrl.animateTo(geoPoint);
		redraw();
	}

	/**
	 * Redraws map by invalidating it.
	 */
	public void redraw() {
		mapView.invalidate();
	}

	public void addOverlay(Overlay overlay) {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.add(overlay);
	}

	public void setSatellite(boolean satellite) {
		if (mapView.isSatellite() == satellite) {
			return;
		}
		mapView.setSatellite(satellite);
		redraw();
	}

	public boolean isSatellite() {
		return mapView.isSatellite();
	}
}
