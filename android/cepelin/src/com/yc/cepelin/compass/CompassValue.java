package com.yc.cepelin.compass;

/**
 * CompassValue holds the compass direction of compass value as well as compass
 * azimuth.
 * 
 * @author zgavrilovic
 * 
 */
public class CompassValue {

    private CompasDirectionEnum direction;

    /**
     * 
     * @param value
     *            is "real" compass value. This "real" value will be represented
     *            and replaced by compass direction and will not be taken into
     *            account for further calculations. Calculation to which compass
     *            direction this real value belongs is done using absolute
     *            distance from nearest compass direction.
     */
    public CompassValue(float value) {
        this.direction = CompasDirectionEnum.getDirection(value);
    }

    /**
     * 
     * @return one of predefined enum from CompassDirectionEnum for actual
     *         compass value
     */
    public CompasDirectionEnum getDirection() {
        return direction;
    }

    /**
     * 
     * @return azimuth(angle to north pole) of actual compass direction
     */
    public double getAzimuth() {
        return direction.getReferentValue();
    }

    @Override
    public String toString() {
        return "CompassValue [direction=" + direction + ", azimuth=" + getAzimuth() + "]";
    }
}