package com.yc.cepelin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.yc.cepelin.model.AbstractOverview;

public class AllOverlay extends ItemizedOverlay {

	private final Context context;
	private ArrayList<OverlayItem> list;
	private int total;

	private Paint txtPaint;
	private Paint borderPaint;

	public AllOverlay(Context context, Drawable marker, List<? extends AbstractOverview> dataList) {
		super(boundCenter(marker));
		this.context = context;

		this.list = new ArrayList<OverlayItem>(dataList.size());
		for (AbstractOverview ao : dataList) {
			GeoPoint geoPoint = GeoUtil.point(ao.getLatitude(), ao.getLongitude());
			list.add(new OverlayItem(geoPoint, ao.getTitle(), ao.getShortDescription()));
		}
		total = list.size();
		populate();

		txtPaint = new Paint();
		txtPaint.setColor(0xEEFFFFFF);
		txtPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setColor(0xAA444444);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean b) {
		if (b == true) {
			return;
		}
		super.draw(canvas, mapView, b);
		for (int i = 0; i < total; i++) {
			OverlayItem oi = list.get(i);
			Point sp = new Point();
			mapView.getProjection().toPixels(oi.getPoint(), sp);
			GfxUtil.drawTextInRect(canvas, sp.x, sp.y + 12, 4, 3, 3, oi.getTitle(), txtPaint, borderPaint);
		}
	}


	@Override
	protected OverlayItem createItem(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return total;
	}

	@Override
	public boolean onTap(int i) {
		OverlayItem item = list.get(i);
		Toast.makeText(context, item.getSnippet(), Toast.LENGTH_SHORT).show();
		return true;
	}
}
