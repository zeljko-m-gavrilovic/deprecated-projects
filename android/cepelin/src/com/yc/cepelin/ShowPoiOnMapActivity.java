package com.yc.cepelin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.yc.cepelin.model.Cafe;
import com.yc.cepelin.model.Event;
import com.yc.cepelin.model.Restaurant;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class ShowPoiOnMapActivity extends MapActivity {

	private GooMap gooMap;
	private GeoPoint geoPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		// read parameters
		Bundle bundle = getIntent().getExtras();
		Drawable marker = ResUtil.getIcon(getResources(), R.drawable.poi);
		
		String itemType = (String) bundle.get(MainTabActivity.ITEM_TYPE);
		Integer itemId = bundle.getInt(MainTabActivity.ID);

		getItemData(itemType, itemId, marker);
/*
		double lat = bundle.getDouble(PARAM_GEO_LATITUDE);
		double lon = bundle.getDouble(PARAM_GEO_LONGITUDE);
		String title = bundle.getString(PARAM_TITLE);
		String snippet = bundle.getString(PARAM_SNIPPET);*/
		
	}

	private void getItemData(String itemType, Integer itemId, final Drawable marker) {

		DataService dataService = DataService.getInstance(this);

		if (MainTabActivity.CAFE.equals(itemType)) {
			dataService.getCafe(itemId, new Callback<Cafe>() {
				@Override
				public void onFinish(Cafe result) {
					double lat = result.getLatitude();
					double lon = result.getLongitude();
					String title = result.getTitle();
					String snippet = result.getShortDescription();

					// system components
					gooMap = new GooMap((MapView) findViewById(R.id.mapView), 17, false);
					geoPoint = GeoUtil.point(lat, lon);
					gooMap.addOverlay(new OneOverlay(ShowPoiOnMapActivity.this, marker, geoPoint, title, snippet));
					gooMap.centerTo(geoPoint);
				}
			});
		} else if (MainTabActivity.FOOD.equals(itemType)) {
			dataService.getRestaurant(itemId, new Callback<Restaurant>() {
				@Override
				public void onFinish(Restaurant result) {
					double lat = result.getLatitude();
					double lon = result.getLongitude();
					String title = result.getTitle();
					String snippet = result.getShortDescription();

					// system components
					gooMap = new GooMap((MapView) findViewById(R.id.mapView), 17, false);
					geoPoint = GeoUtil.point(lat, lon);
					gooMap.addOverlay(new OneOverlay(ShowPoiOnMapActivity.this, marker, geoPoint, title, snippet));
					gooMap.centerTo(geoPoint);
				}				
			});
		} else if (MainTabActivity.EVENT.equals(itemType)) {
			dataService.getEvent(itemId, new Callback<Event>() {
				@Override
				public void onFinish(Event result) {
					double lat = result.getLatitude();
					double lon = result.getLongitude();
					String title = result.getTitle();
					String snippet = result.getShortDescription();

					// system components
					gooMap = new GooMap((MapView) findViewById(R.id.mapView), 17, false);
					geoPoint = GeoUtil.point(lat, lon);
					gooMap.addOverlay(new OneOverlay(ShowPoiOnMapActivity.this, marker, geoPoint, title, snippet));
					gooMap.centerTo(geoPoint);
				}				
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (gooMap != null) {
			gooMap.centerTo(geoPoint);			
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
}
