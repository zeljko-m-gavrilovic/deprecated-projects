package com.yc.cepelin.service;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.yc.cepelin.model.Cafe;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.model.Comment;
import com.yc.cepelin.model.Event;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.model.Restaurant;
import com.yc.cepelin.model.RestaurantOverview;

public abstract class DataService {
	
	protected Context context;
	
	DataService(Context context) {
		super();
		this.context = context;
	}
	
	public static DataService getInstance(Context context) {
		return new DataServiceImpl(context);
	}

	/**
	 * 
	 * @param callback - Will return list of cafe overviews. Compass filtering should be done on client.
	 */
	public abstract void getCafeOverviews(Callback<ArrayList<CafeOverview>> callback);
	
	/**
	 * 
	 * @param callback - Will return list of restaurant overviews. Compass filtering should be done on client.
	 */
	public abstract void getRestaurantOverviews(Callback<ArrayList<RestaurantOverview>> callback);
		
	/**
	 * 
	 * @param callback - Will return list of event overviews. Compass filtering should be done on client.
	 */
	public abstract void getEventOverviews(Callback<ArrayList<EventOverview>> callback);	
	
	/**
	 * 
	 * @param cafeId - Unique id (database generated).
	 * @param callback - Will return cafe object or null if not found. Data within object should be enough to fill info tab and map tab.
	 */
	public abstract void getCafe(int cafeId, Callback<Cafe> callback);

	/**
	 * 
	 * @param restaurantId - Unique id (database generated).
	 * @param callback - Will return restaurant object or null if not found. Data within object should be enough to fill info tab and map tab.
	 */
	public abstract void getRestaurant(int restaurantId, Callback<Restaurant> callback);

	/**
	 * 
	 * @param eventId - Unique id (database generated).
	 * @param callback - Will return event object or null if not found. Data within object should be enough to fill info tab and map tab.
	 */	
	public abstract void getEvent(int eventId, Callback<Event> callback);	
	
	/**
	 * 
	 * @param itemId - Unique id (database generated). It may be cafe id, restaurant id or event id.
	 * @param callback - Will return list of comments.
	 */
	public abstract void getComments(int itemId, Callback<ArrayList<Comment>> callback);		
	
	/**
	 * 
	 * @param itemId - Unique id (database generated). It may be cafe id, restaurant id or event id.
	 * @param rating - Value from 0 to 5, in steps of 0.5
	 * @param comment - Short message (not longer than 160 chars) with user comment.
	 * @param callback - Will return database generated id for newly created comment, or -1 on error.
	 */
	public abstract void addComment(int itemId, double rating, String comment, Callback<Integer> callback);
	
	/**
	 * 
	 * @param itemId - Unique id (database generated). It may be cafe id, restaurant id or event id.
	 * @return List of image id's for given item. It is intended to be used together with getPhotoForItem() method. 
	 */
	public abstract void getPhotoIdsForItem(int itemId, Callback<ArrayList<Integer>> callback);
		
	/**
	 * 
	 * @param itemId - Unique id (database generated). It may be cafe id, restaurant id or event id.
	 * @param photoId - One of id's returned from getPhotoIdsForItem() method call.
	 * @param callback - Will return photo or null if photo for id is not found.
	 */
	public abstract void getPhotoForItem(int itemId, int photoId, Callback<Drawable> callback);
	
	/**
	 * 
	 * @param itemId - Unique id (database generated). It may be cafe id, restaurant id or event id.
	 * @param photo - Photography.
	 * @param callback - Will return database generated id for newly added photo, or -1 on error.
	 */
	public abstract void addPhotoForItem(int itemId, Drawable photo, Callback<Integer> callback);
	
}
