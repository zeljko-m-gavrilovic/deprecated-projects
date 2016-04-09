package com.yc.cepelin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class OneOverlay extends ItemizedOverlay {

	private final OverlayItem item;
	private final Context context;

	public OneOverlay(Context context, Drawable marker, GeoPoint geoPoint, String title, String snippet) {
		super(boundCenter(marker));
		this.context = context;
		item = new OverlayItem(geoPoint, title, snippet);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean b) {
		if (b == true) {
			return;
		}
		super.draw(canvas, mapView, b);
	}

	@Override
	protected OverlayItem createItem(int index) {
		return item;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		Toast.makeText(context, item.getSnippet(), Toast.LENGTH_SHORT).show();
		return true;
	}
}
