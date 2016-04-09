package com.yc.cepelin;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.model.RestaurantOverview;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class AllOnMapActivity extends MapActivity {

	private GooMap gooMap;
	private MyLocationOverlay myLocationOverlay;
	protected SharedPreferences shared;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allonmap);
		gooMap = new GooMap((MapView) findViewById(R.id.allMapView), 17, false);

		DataService.getInstance(this).getEventOverviews(new Callback<ArrayList<EventOverview>>() {
			@Override
			public void onFinish(ArrayList<EventOverview> result) {
				gooMap.addOverlay(new AllOverlay(AllOnMapActivity.this, ResUtil.getIcon(getResources(), R.drawable.event_m), result));
			}
		});
		DataService.getInstance(this).getCafeOverviews(new Callback<ArrayList<CafeOverview>>() {
			@Override
			public void onFinish(ArrayList<CafeOverview> result) {
				gooMap.addOverlay(new AllOverlay(AllOnMapActivity.this, ResUtil.getIcon(getResources(), R.drawable.cafe_m), result));
			}
		});
		DataService.getInstance(this).getRestaurantOverviews(new Callback<ArrayList<RestaurantOverview>>() {
			@Override
			public void onFinish(ArrayList<RestaurantOverview> result) {
				gooMap.addOverlay(new AllOverlay(AllOnMapActivity.this, ResUtil.getIcon(getResources(), R.drawable.restaurant_m), result));
			}
		});

		myLocationOverlay = new MyLocationOverlay(this, gooMap.getMapView());
		gooMap.addOverlay(myLocationOverlay);
		gooMap.centerTo(GeoUtil.point(44.81608, 20.460535));
		shared = getSharedPreferences("allmap", 0);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		gooMap.setZoom(shared.getInt("zoom", 17));
		gooMap.redraw();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
		SharedPreferences.Editor editor = shared.edit();
		editor.putInt("zoom", gooMap.getZoom());
		editor.commit();
	}
}
