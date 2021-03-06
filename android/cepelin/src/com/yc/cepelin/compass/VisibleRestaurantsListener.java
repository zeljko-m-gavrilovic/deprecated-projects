package com.yc.cepelin.compass;

import java.util.List;
import java.util.Map;

import com.yc.cepelin.model.RestaurantOverview;

/**
 * Listener to changes in visibility of objects in front of the user. This class
 * has one method which is invoked when list of visible objects has been changed
 * 
 * @author Zeljko
 * 
 */
public interface VisibleRestaurantsListener {

    /**
     * This method is called when list of visible objects in front of user has
     * been changed
     * 
     * @param visibleRestaurants
     *            is list of visible objects in front of user.
     * @param distances
     *            is map of pairs(objectId, distance [meter unit]) of visible
     *            objects from first input parameter.
     * 
     */
    public void onVisibleRestaurantsChanged(List<RestaurantOverview> visibleRestaurants, Map<Integer, Integer> distances);
}