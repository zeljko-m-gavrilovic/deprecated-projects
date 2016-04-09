package com.yc.cepelin.compass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.model.RestaurantOverview;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

/**
 * CompassGpsView extends functionality of compass view enabling third party
 * objects to register listener on visible objects(cafes, restaurants...etc)in
 * front of the user.
 * 
 * Note: this class uses GPS positioning and compass azimuth in order to
 * calculate visibility of objects in front of user. When visibility of objects
 * change, list of new objects is sent to registered visibleCafesListeners.
 * 
 * @author Zeljko
 * 
 */
public class CompassGpsView extends CompassView {

    private double userLatitude;
    private double userLongitude;

    private List<VisibleCafesListener> visibleCafesListeners;
    private List<VisibleRestaurantsListener> visibleRestaurantsListeners;
    private List<VisibleEventsListener> visibleEventsListeners;
    private List<CafeOverview> cafeOverviews;
    private List<RestaurantOverview> restaurantOverviews;

    private double AZIMUTH_OFFSET_LIMIT = 2;
    private int azimuthOffset;
    private LocationManager lm; // dragan
    private LocationListener locationListener; // dragan

    public CompassGpsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGps();
    }

    public CompassGpsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGps();
    }

    public CompassGpsView(Context context) {
        super(context);
        initGps();
    }

    /**
     * Initialize visibleCafesListeners, cafe list from data service and
     * register gps sensor listener
     */
    private void initGps() {
        visibleCafesListeners = new ArrayList<VisibleCafesListener>();
        visibleRestaurantsListeners = new ArrayList<VisibleRestaurantsListener>();
        visibleEventsListeners = new ArrayList<VisibleEventsListener>();

        DataService dataService = DataService.getInstance(getContext());

        // load cafes list
        dataService.getCafeOverviews(new Callback<ArrayList<CafeOverview>>() {
            @Override
            public void onFinish(ArrayList<CafeOverview> result) {
                cafeOverviews = result;
            }
        });

        // load restaurants list
        dataService.getRestaurantOverviews(new Callback<ArrayList<RestaurantOverview>>() {
            @Override
            public void onFinish(ArrayList<RestaurantOverview> result) {
                restaurantOverviews = result;
            }
        });

        registerGspSensorListener();
    }

    /**
     * Registers gps sensor listener and handles gps changes.
     */
    public void registerGspSensorListener() {
        if (null != locationListener) return;
        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                    CompassGpsView.super.update();
                    updateVisibleObjects();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        // takes GPS/WiFi/GSM data if user change position more than 20m and sleep 3s
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 20f, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 20f, locationListener); // dragan
    }

    /**
     * Unregisters location sensor listener.
     */
    public void unregisterGspSensorListener() { // dragan
    	if (null != lm) lm.removeUpdates(locationListener);
        locationListener = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.yc.android.compass.CompassView#valueChanged(com.yc.android.compass
     * .CompassValue)
     */
    protected void valueChanged(CompassValue newCompassValue) {
    	 if (!newCompassValue.getDirection().equals(compassValue.getDirection())) {
            handleAzimuthOffset(newCompassValue);
            compassValue = newCompassValue;
            update();
        }
    }

    /**
     * Handle azimuth offset need for updating visible objects
     * 
     * @param newCompassValue
     *            is new compass value to be handled
     */
    private void handleAzimuthOffset(CompassValue newCompassValue) {
        int deltaOffset = (int) ((newCompassValue.getAzimuth() - compassValue.getAzimuth()) * 10);
        if ((deltaOffset == 225) || (deltaOffset == -(3600 - 225))) {
            azimuthOffset++;
        } else {
            azimuthOffset--;
        }
    }

    @Override
    protected synchronized void update() {
        super.update();
        boolean visibilityChanged = (Math.abs(azimuthOffset) % AZIMUTH_OFFSET_LIMIT) == 0;
        if (visibilityChanged) {
            azimuthOffset = 0;
            updateVisibleObjects();
        }
    }

    /**
     * Updates listeners that changes in visibility of objects occurred
     */
    private void updateVisibleObjects() {
        if (!visibleCafesListeners.isEmpty()) {
            updateVisibleCafes();
        }
        if (!visibleRestaurantsListeners.isEmpty()) {
            updateVisibleRestaurants();
        }
        if (!visibleEventsListeners.isEmpty()) {
            updateVisibleRestaurants();
        }        
    }

    /**
     * Recalculates visible cafes in front of user and notify registered
     * listeners
     */
    private void updateVisibleCafes() {
        List<CafeOverview> visibleCafes = new ArrayList<CafeOverview>();
        Map<Integer, Integer> distancesCafes = new HashMap<Integer, Integer>();

        for (ObjectWithDistance visibleCafe : calculateVisibleCafes()) {
            CafeOverview cafeOverview = (CafeOverview) visibleCafe.getObject();
            visibleCafes.add(cafeOverview);

            Integer cafeDistance = new Integer(visibleCafe.getDistance());
            distancesCafes.put(cafeOverview.getId(), cafeDistance);
        }
        notifyVisibileCafesListeners(visibleCafes, distancesCafes);
    }

    /**
     * Recalculates visible restaurants in front of user and notify registered
     * listeners
     */
    private void updateVisibleRestaurants() {
        List<RestaurantOverview> visibleRestaurants = new ArrayList<RestaurantOverview>();
        Map<Integer, Integer> distancesRestaurants = new HashMap<Integer, Integer>();

        for (ObjectWithDistance visibleRestaurant : calculateVisibleRestaurants()) {
            RestaurantOverview restaurantOverview = (RestaurantOverview) visibleRestaurant.getObject();
            visibleRestaurants.add(restaurantOverview);

            Integer cafeDistance = new Integer(visibleRestaurant.getDistance());
            distancesRestaurants.put(restaurantOverview.getId(), cafeDistance);
        }
        notifyVisibleRestaurantsListeners(visibleRestaurants, distancesRestaurants);
    }

    /**
     * Recalculates visible cafes in front of user. Filtering is done in polar
     * coordinate system using GPS location and compass azimuth
     * 
     * @return list of visible cafes wrapped with data about distance from user
     *         point of view in meter unit
     */
    private List<ObjectWithDistance> calculateVisibleCafes() {
        List<ObjectWithDistance> visibleCafes = new ArrayList<ObjectWithDistance>();

        PolarCoordinateSystem polarCoordSys = new PolarCoordinateSystem(userLatitude, userLongitude);
        VisibleArea visibleArea = new VisibleArea(compassValue);

		if (cafeOverviews == null) {
			return visibleCafes;
		}
        for (CafeOverview cafeOverview : cafeOverviews) {

            PolarCoordinate cafeCoordinate = polarCoordSys.getCoordinate(cafeOverview.getLatitude(), cafeOverview
                    .getLongitude());

            if (visibleArea.isAngleBetweenLimits(cafeCoordinate.getAngle())) {
                int distance = (int) cafeCoordinate.getR();
                ObjectWithDistance cowd = new ObjectWithDistance(cafeOverview, distance);
                visibleCafes.add(cowd);
            }
        }
        return visibleCafes;
    }

    /**
     * Recalculates visible restaurants in front of user. Filtering is done in
     * polar coordinate system using GPS location and compass azimuth
     * 
     * @return list of visible restaurants wrapped with data about distance from
     *         user point of view in meter unit
     */
    private List<ObjectWithDistance> calculateVisibleRestaurants() {
        List<ObjectWithDistance> visibleRestaurants = new ArrayList<ObjectWithDistance>();

        PolarCoordinateSystem polarCoordSys = new PolarCoordinateSystem(userLatitude, userLongitude);
        VisibleArea visibleArea = new VisibleArea(compassValue);

        if (restaurantOverviews == null) {
			return visibleRestaurants;
		}
		for (RestaurantOverview restaurantOverview : restaurantOverviews) {

            PolarCoordinate cafeCoordinate = polarCoordSys.getCoordinate(restaurantOverview.getLatitude(),
                    restaurantOverview.getLongitude());

            if (visibleArea.isAngleBetweenLimits(cafeCoordinate.getAngle())) {
                int distance = (int) cafeCoordinate.getR();
                ObjectWithDistance cowd = new ObjectWithDistance(restaurantOverview, distance);
                visibleRestaurants.add(cowd);
            }
        }
        return visibleRestaurants;
    }

    /**
     * Notifies registered listener that visibility of objects is changed
     * 
     * @param visibleCafes
     *            is list of visible objects in front of user.
     * @param distances
     *            is map of pairs(objectId, distance [meter unit]) of visible
     *            objects from first input parameter.
     */
    private void notifyVisibileCafesListeners(List<CafeOverview> visibleCafes, Map<Integer, Integer> distances) {
        for (VisibleCafesListener listener : visibleCafesListeners) {
            listener.onVisibleCafesChanged(visibleCafes, distances);
        }
    }

    /**
     * Notifies registered listener that visibility of objects is changed
     * 
     * @param visibleCafes
     *            is list of visible objects in front of user.
     * @param distances
     *            is map of pairs(objectId, distance [meter unit]) of visible
     *            objects from first input parameter.
     */
    private void notifyVisibleRestaurantsListeners(List<RestaurantOverview> visibleRestaurants,
            Map<Integer, Integer> distances) {
        for (VisibleRestaurantsListener listener : visibleRestaurantsListeners) {
            listener.onVisibleRestaurantsChanged(visibleRestaurants, distances);
        }
    }

    /**
     * Notifies registered listener that visibility of objects is changed
     * 
     * @param visibleCafes
     *            is list of visible objects in front of user.
     * @param distances
     *            is map of pairs(objectId, distance [meter unit]) of visible
     *            objects from first input parameter.
     */
    private void notifyVisibleEventsListeners(List<EventOverview> visibleEvents,
            Map<Integer, Integer> distances) {
        for (VisibleEventsListener listener : visibleEventsListeners) {
            listener.onVisibleEventsChanged(visibleEvents, distances);
        }
    }
    
    /**
     * Register new listener to visible cafes in front of user
     * 
     * @param visibleCafesListener
     *            is visible cafe changes listener
     */
    public void addVisibleCafesListener(VisibleCafesListener visibleCafesListener) {
        visibleCafesListeners.add(visibleCafesListener);
    }

    /**
     * Unregister visible cafes listener
     * 
     * @param visibleCafesListener
     *            is visible cafe changes listener
     */
    public void removeVisibleCafesListener(VisibleCafesListener visibleCafesListener) {
        visibleCafesListeners.remove(visibleCafesListener);
    }

    /**
     * Register new listener to visible restaurants in front of user
     * 
     * @param visibleRestaurantsListener
     *            is visible restaurants listener
     */
    public void addVisibleRestaurantsListener(VisibleRestaurantsListener visibleRestaurantsListener) {
        visibleRestaurantsListeners.add(visibleRestaurantsListener);
    }

    /**
     * Unregister visible restaurants listener
     * 
     * @param visibleRestaurantsListener
     *            is visible restaurants changes listener
     */
    public void removeVisibleRestaurantsListener(VisibleRestaurantsListener visibleRestaurantsListener) {
        visibleRestaurantsListeners.remove(visibleRestaurantsListener);
    }

    /**
     * Register new listener to visible restaurants in front of user
     * 
     * @param visibleEventsListener
     *            is visible restaurants listener
     */
    public void addVisibleEventsListener(VisibleEventsListener visibleEventsListener) {
        visibleEventsListeners.add(visibleEventsListener);
    }

    /**
     * Unregister visible restaurants listener
     * 
     * @param visibleEventsListener
     *            is visible restaurants changes listener
     */
    public void removeVisibleEventsListener(VisibleEventsListener visibleEventsListener) {
        visibleEventsListeners.remove(visibleEventsListener);
    }
    
    
    /**
     * ObjectWithDistance wraps CafeOverview adding additional property distance
     * (in meter unit)
     * 
     * @author Zeljko
     * 
     */
    class ObjectWithDistance {

        private Object object;
        private int distance;

        /**
         * Wraps cafe overview object
         * 
         * @param object
         *            is object for which we calculates distance
         * @param distance
         *            is distance of cafe to the user in meter unit
         */
        public ObjectWithDistance(Object object, int distance) {
            this.object = object;
            this.distance = distance;
        }

        public Object getObject() {
            return object;
        }

        public int getDistance() {
            return distance;
        }
    }

	public double getUserLatitude() {
		return userLatitude;
	}

	public double getUserLongitude() {
		return userLongitude;
	}
}