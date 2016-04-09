package com.yc.cepelin.compass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

import com.yc.cepelin.R;

/**
 * CompassView class is responsible for fetching compass sensor events and
 * render its changes in coordinates on the screen
 * 
 * Note: Image width is approximately three times wider then screen so user has
 * impression that image doesn't have bounds.
 * 
 * @author zgavrilovic
 * 
 */
public class CompassView extends View {

    private Bitmap bitmap;
    private int width;
    private int height;
    private int leftBitmapWidth;

    private float pixelCoeficient;
    protected CompassValue compassValue;
    private Paint azimuthLabelPaint;
    private float screenCenterX;
    
	public int getCompassHeight() {
		return height;
	}

	public CompassView(Context context) {
        super(context);
        init();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

	/**
     * Initialize properties and start listening for compass changes
     */
    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass3_black);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        leftBitmapWidth = (width / 3);

        pixelCoeficient = ((float) leftBitmapWidth) / 360f;
        compassValue = new CompassValue(CompasDirectionEnum.N.getReferentValue());

        initAzimuthLabelPaint();
        registerCompassSensorListener();
    }

    private void initAzimuthLabelPaint() {
        azimuthLabelPaint = new Paint();
        azimuthLabelPaint.setColor(Color.WHITE);
        azimuthLabelPaint.setTextSize(12);
        Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
        azimuthLabelPaint.setTypeface(typeface);
        azimuthLabelPaint.setTextAlign(Paint.Align.CENTER);
        azimuthLabelPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float compassAzimuth = (float) compassValue.getAzimuth();
        int centerImagePosition = -(calculateImageOffset(compassAzimuth) - (int) screenCenterX);
        canvas.drawBitmap(bitmap, centerImagePosition, 0, null);

        int azimuthKey = compassValue.getDirection().getKey();
        String azimuthLabel = getContext().getString(azimuthKey);
        canvas.drawText(azimuthLabel, screenCenterX, height - 8, azimuthLabelPaint);
    }

    /**
     * Calculate x coordinate offset for image. Value is between 0(zero) and
     * image width
     * 
     * @param compasssCoordinate
     *            is compass coordinate. Value is between 0 and 360.
     * @return offset for image according to compass coordinate.
     */
    private int calculateImageOffset(float compasssCoordinate) {
        return leftBitmapWidth + transformCoordinatesToPixels(compasssCoordinate);
    }

    /**
     * Transforms compass coordinates to pixels on the image
     * 
     * @param compasssCoordinate
     *            is compass coordinate
     * @return pixels transformed from compass coordinates
     */
    private int transformCoordinatesToPixels(float compasssCoordinate) {
        return Math.round(pixelCoeficient * compasssCoordinate);
    }

    /**
     * Register that value is changed and checks if compass value is different
     * than previous. If value is different than previous then view is updated
     * and invalidated in order to refresh the view.
     * 
     * @param newCompassValue
     *            is new value of compass coordinates
     */
    protected void valueChanged(CompassValue newCompassValue) {
    	if (!newCompassValue.getDirection().equals(compassValue.getDirection())) {
            compassValue = newCompassValue;
            update();
        }
    }

    /**
     * Refresh view on the screen. it can be overriden in the subclasses
     */
    protected void update() {
        invalidate();
    }

    /**
     * Registers compass sensor listener and handles compass changes.
     */
    private void registerCompassSensorListener() {
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        int rate = SensorManager.SENSOR_DELAY_NORMAL;

        SensorEventListener sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float azimuth = event.values[0];
                CompassValue compasssValue = new CompassValue(azimuth);
                valueChanged(compasssValue);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(sel, compassSensor, rate);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        screenCenterX = w * 0.5f; // remember the center of the screen
    }
}