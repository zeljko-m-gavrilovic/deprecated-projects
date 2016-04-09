package com.yc.cepelin.compass;

/**
 * PolarCoordinate is point in two dimensional space represented by its angle
 * and length from coordinate center
 * 
 * @author Zeljko
 * 
 */
public class PolarCoordinate {

    private double r;
    private double angle;

    public PolarCoordinate(double r, double angle) {
        super();
        this.r = r;
        this.angle = angle;
    }

    /**
     * 
     * @return distance(length) from center of coordinate system 
     */
    public double getR() {
        return r;
    }

    /**
     * 
     * @return angle between x axe and point in polar coordinate system 
     */
    public double getAngle() {
        return angle;
    }

}
