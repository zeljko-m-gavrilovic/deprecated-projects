package com.yc.cepelin.compass;

/**
 * Helper class which calculates if some angle(polar coordinate's angle of some
 * object) belongs to some visibility area bounded with left and right limit
 * angles
 * 
 * @author Zeljko
 * 
 */
public class VisibleArea {

    private static final int FULL_CIRCLE = 360;
    private static final double AZIMUTH_OFFSET = 2 * 22.5;
    private CompassValue compassValue;

    /**
     * 
     * @param compassValue
     *            property direction is used for creating visibility area with
     *            left and right limits angles
     */
    public VisibleArea(CompassValue compassValue) {
        this.compassValue = compassValue;
    }

    /**
     * Calculates if some angle belong to area or not
     * 
     * @param compassAzimuth
     *            to check if it belongs to area
     * @return true if angle belongs to area in other case it returns false
     */
    public boolean isAngleBetweenLimits(double compassAzimuth) {
        boolean leftLimitSatisfied = compassAzimuth >= getLeftLimitAngle();
        boolean rightLimitSatisfied = compassAzimuth <= getRightLimitAngle();
        if (leftLimitSatisfied && rightLimitSatisfied) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @return left limit angle according to compass direction
     */
    public double getLeftLimitAngle() {
        double leftLimitAngle = compassValue.getAzimuth() - AZIMUTH_OFFSET;
        if (leftLimitAngle < 0) {
            leftLimitAngle = CompasDirectionEnum.NNW.getReferentValue();
        }
        return leftLimitAngle;
    }

    /**
     * 
     * @return right limit angle according to compass direction
     */
    public double getRightLimitAngle() {
        double rightLimitAngle = compassValue.getAzimuth() + AZIMUTH_OFFSET;
        if (rightLimitAngle > FULL_CIRCLE) {
            rightLimitAngle = CompasDirectionEnum.NNE.getReferentValue();
        }
        return rightLimitAngle;
    }
}