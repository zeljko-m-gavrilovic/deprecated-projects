package com.yc.cepelin.compass;

import java.util.List;
import java.util.Map;

import com.yc.cepelin.model.CafeOverview;

/**
 * Listener to changes in visibility of objects in front of the user. This class
 * has one method which is invoked when list of visible objects has been changed
 * 
 * @author Zeljko
 * 
 */
public interface VisibleCafesListener {

    /**
     * This method is called when list of visible objects in front of user has
     * been changed
     * 
     * @param visibleCafes
     *            is list of visible objects in front of user.
     * @param distances
     *            is map of pairs(objectId, distance [meter unit]) of visible
     *            objects from first input parameter.
     * 
     */
    public void onVisibleCafesChanged(List<CafeOverview> visibleCafes, Map<Integer, Integer> distances);
}
