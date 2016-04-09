package com.yc.cepelin.compass;

import com.yc.cepelin.R;

/**
 * Compass circle is divided into 16 equal parts. CompassDirection classifies
 * compass value into one of this equidistant 16 fields.
 * 
 * For example constructor takes compass value 25.8, and
 * getDirection().getReferenceValue() will return reference value of nearest
 * compass direction fields. In this case it will be 'NNE' field and its
 * reference value 22.5.
 * 
 * This enum can help us in compass view for taking care only for those compass
 * coordinate changes that cross over to next field...
 * 
 * @author zgavrilovic
 * 
 */
public enum CompasDirectionEnum {

    N(0f, R.string.COMPASS_N, "North"), 
    NNE(22.5f, R.string.COMPASS_NNE, "North North-East"), 
    NE(45f, R.string.COMPASS_NE, "North-East"), 
    ENE(67.5f, R.string.COMPASS_ENE, "East North-East"), 
    E(90f,R.string.COMPASS_E, "East"), 
    ESE(112.5f, R.string.COMPASS_ESE, "East South-East"), 
    SE(135f, R.string.COMPASS_SE, "South-East"), 
    SSE(157.5f, R.string.COMPASS_SSE, "South South-East"), 
    S(180f,R.string.COMPASS_S, "South"), 
    SSW(202.5f, R.string.COMPASS_SSW, "South South-West"), 
    SW(225,R.string.COMPASS_SW, "South-West"), 
    WSW(247.5f, R.string.COMPASS_WSW, "West South-West"), 
    W(270f,R.string.COMPASS_W, "West"), 
    WNW(292.5f, R.string.COMPASS_WNW, "West North-West"), 
    NW(315f,R.string.COMPASS_NW, "North West"), 
    NNW(337.5f, R.string.COMPASS_NNW, "North North-West");

    private float referentValue;
    private int key;
    private String description;

    private CompasDirectionEnum(float referentValue, int key, String description) {
        this.referentValue = referentValue;
        this.key = key;
        this.description = description;
    }

    public float getReferentValue() {
        return referentValue;
    }

    public int getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Calculate compass direction field to which this value belongs, in other
     * word it calculate nearest compass direction reference value it belongs.
     * 
     * @param value
     * @return
     */
    public static CompasDirectionEnum getDirection(float value) {
        CompasDirectionEnum result = null;
        float minDistance = Float.MAX_VALUE;
        for (CompasDirectionEnum compasDirection : values()) {
            float distance = value - compasDirection.getReferentValue();
            float absDistance = Math.abs(distance);
            if (absDistance < minDistance) {
                minDistance = absDistance;
                result = compasDirection;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "CompassDirection [key=" + key + ", description=" + description + ", referentValue=" + referentValue
                + "]";
    }
}