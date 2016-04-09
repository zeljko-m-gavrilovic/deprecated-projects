package com.yc.cepelin.compass;

import android.graphics.Point;
import android.location.Location;

/**
 * Representation of polar coordinate system. It creates polar coordinate system
 * relative to global locating system(meridian, equador) using GPS point at
 * which user stands as referent value and center point of polar coordinate
 * system.
 * 
 * @author Zeljko
 * 
 */
public class PolarCoordinateSystem {

    private double referentLatitude;
    private double referentLongitude;

    /**
     * It takes GPS position at user stands
     * 
     * @param referentLatitude
     *            is latitude at user stands
     * @param referentLongitude
     *            is longitude at user stands
     */
    public PolarCoordinateSystem(double referentLatitude, double referentLongitude) {
        this.referentLatitude = referentLatitude;
        this.referentLongitude = referentLongitude;
    }

    /**
     * Returns polar coordinate in this relative polar coordinate system
     * 
     * @param latitude
     *            is absolute latitude of some GPS point
     * @param longitude
     *            is absolute longitude of some GPS point
     * @return
     */
    public PolarCoordinate getCoordinate(double latitude, double longitude) {
        float[] results = new float[3];
        Location.distanceBetween(latitude, longitude, referentLatitude, referentLongitude, results);
        double r = results[0];

        float angle = calculateBearing(latitude, longitude);

        PolarCoordinate polarCoordinate = new PolarCoordinate(r, angle);

        return polarCoordinate;
    }

    /**
     * Calculates the bearing of the two Locations supplied and returns the
     * Angle in the following (GPS-likely) manner: <br />
     * <code>N:0°, E:90°, S:180°, W:270°</code>
     * 
     * @param latitude
     *            of object for which we calculates bearing
     * @param longitude
     *            of object for which we calculates bearing
     */
    public float calculateBearing(double latitude, double longitude) {
        Point pReferent = location2Point(referentLatitude, referentLongitude);
        Point pObject = location2Point(latitude, longitude);

        float angle = -(float) (Math.atan2(pObject.y - pReferent.y, pObject.x - pReferent.x) * 180 / Math.PI) + 90.0f;
        if (angle < 0) {
            angle = angle + 360.0f;
        }
        return angle;
    }

    /**
     * Converts an <code>android.location.Location</code> to an
     * <code>android.graphics.Point</code>.
     */
    public static Point location2Point(double aLatitude, double aLongitude) {
        return new Point((int) (aLongitude * 1E6), (int) (aLatitude * 1E6));
    }
}