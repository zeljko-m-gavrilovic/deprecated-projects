package com.yc.cepelin.service;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.yc.cepelin.R;
import com.yc.cepelin.model.Cafe;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.model.Comment;
import com.yc.cepelin.model.Event;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.model.Restaurant;
import com.yc.cepelin.model.RestaurantOverview;

public class DataServiceMockImpl extends DataService {
	
	DataServiceMockImpl(Context context) {
		super(context);
	}

	@Override
	public void addComment(int itemId, double rating, String comment,
			Callback<Integer> callback) {
		// do nothing		
		if (callback != null) {
			callback.onFinish(456);
		}
	}

	@Override
	public void addPhotoForItem(int itemId, Drawable photo,
			Callback<Integer> callback) {
		// do nothing		
		if (callback != null) {
			callback.onFinish(456);
		}
	}

	@Override
	public void getCafe(int cafeId, Callback<Cafe> callback) {
		Cafe cafe = new Cafe();
		if (cafeId == 1) {
			cafe.setTitle("Akademija");			
		}
		if (cafeId == 2) {
			cafe.setTitle("2");			
		}
		if (cafeId == 3) {
			cafe.setTitle("3");			
		}
		
		cafe.setShortDescription("Lorem ipsum dolor sit amet.");
		cafe.setLongDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tempus, enim quis semper tempor, risus libero aliquam risus, at cursus enim nisl a est. Nullam malesuada dolor non tortor vulputate feugiat. Aliquam in lacus elit. Nunc eget justo non nulla luctus vestibulum ultrices non sem. Etiam convallis nisi eu magna consectetur eu sodales felis mattis. Nullam mattis metus quis purus consequat nec imperdiet quam elementum. Vestibulum nec iaculis odio. Nam consequat porta aliquet. Pellentesque in erat nulla. Nam odio lorem, gravida a luctus nec, adipiscing eget sapien. Phasellus eleifend lectus mattis massa blandit vestibulum. Nullam laoreet imperdiet justo eget suscipit.");
		cafe.setPhoneNumber("064789");
		cafe.setRating(4.5);
		cafe.setLongitude(20.4673845);
		cafe.setLatitude(44.8025677);
		if (callback != null) {
			callback.onFinish(cafe);
		}		
	}

	@Override
	public void getCafeOverviews(Callback<ArrayList<CafeOverview>> callback) {
		ArrayList<CafeOverview> result = new ArrayList<CafeOverview>();
		
		CafeOverview co = new CafeOverview();
		co.setId(1);
		co.setTitle("Akademija");
		co.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(co);

		co = new CafeOverview();
		co.setId(2);
		co.setTitle("Andergraund");
		co.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(co);
		
		co = new CafeOverview();
		co.setId(3);
		co.setTitle("Bar Baltazar");
		co.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(co);
		
		if (callback != null) {		
			callback.onFinish(result);
		}
	}

	@Override
	public void getComments(int itemId, Callback<ArrayList<Comment>> callback) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		
		Comment comment = new Comment();
		comment.setRating(3.5);
		comment.setText("Baš je super ovo!");
		
		result.add(comment);
		
		comment = new Comment();
		comment.setRating(4.5);
		comment.setText("Kako je ovo mesto lepo i patofnasto! Pozdrav svima, patofnica :)");
		
		result.add(comment);
		
		comment = new Comment();
		comment.setRating(5);
		comment.setText("Super smo se proveli. Svaka čast android ekipi koja je napravila ovaj cool program!");
		
		result.add(comment);

		if (callback != null) {		
			callback.onFinish(result);
		}		
	}

	@Override
	public void getEvent(int eventId, Callback<Event> callback) {
		Event event = new Event();
		event.setTitle("Ice age 3");
		event.setShortDescription("Lorem ipsum dolor sit amet.");
		event.setLongDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tempus, enim quis semper tempor, risus libero aliquam risus, at cursus enim nisl a est. Nullam malesuada dolor non tortor vulputate feugiat. Aliquam in lacus elit. Nunc eget justo non nulla luctus vestibulum ultrices non sem. Etiam convallis nisi eu magna consectetur eu sodales felis mattis. Nullam mattis metus quis purus consequat nec imperdiet quam elementum. Vestibulum nec iaculis odio. Nam consequat porta aliquet. Pellentesque in erat nulla. Nam odio lorem, gravida a luctus nec, adipiscing eget sapien. Phasellus eleifend lectus mattis massa blandit vestibulum. Nullam laoreet imperdiet justo eget suscipit.");
		event.setPhoneNumber("064789");
		event.setRating(4.5);
		event.setLongitude(20);
		event.setLatitude(45);
		
		if (callback != null) {		
			callback.onFinish(event);
		}				
	}

	@Override
	public void getEventOverviews(Callback<ArrayList<EventOverview>> callback) {
		// TODO Auto-generated method stub
		ArrayList<EventOverview> result = new ArrayList<EventOverview>();
		
		EventOverview eo = new EventOverview();
		eo.setId(1);
		eo.setTitle("Ice Age 3");
		eo.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(eo);

		eo = new EventOverview();
		eo.setId(2);
		eo.setTitle("Gamer");
		eo.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(eo);
		
		eo = new EventOverview();
		eo.setId(3);
		eo.setTitle("Night at the museum 2");
		eo.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(eo);
		
		if (callback != null) {		
			callback.onFinish(result);
		}						
	}

	@Override
	public void getPhotoForItem(int itemId, int photoId,
			Callback<Drawable> callback) {
		
		Drawable result = null;
		
		switch (photoId) {
		case 1:
			result = context.getResources().getDrawable(R.drawable.mock_1);
			break;
		case 2:
			result = context.getResources().getDrawable(R.drawable.mock_2);
			break;
		case 3:
			result = context.getResources().getDrawable(R.drawable.mock_3);
			break;
		}

		if (callback != null) {		
			callback.onFinish(result);
		}		
	}

	@Override
	public void getPhotoIdsForItem(int itemId,
			Callback<ArrayList<Integer>> callback) {

		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(1);
		result.add(2);
		result.add(3);

		if (callback != null) {		
			callback.onFinish(result);
		}		
	}

	@Override
	public void getRestaurant(int restaurantId, Callback<Restaurant> callback) {
		Restaurant restaurant = new Restaurant();
		restaurant.setTitle("Rest?");
		restaurant.setShortDescription("Lorem ipsum dolor sit amet.");
		restaurant.setLongDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tempus, enim quis semper tempor, risus libero aliquam risus, at cursus enim nisl a est. Nullam malesuada dolor non tortor vulputate feugiat. Aliquam in lacus elit. Nunc eget justo non nulla luctus vestibulum ultrices non sem. Etiam convallis nisi eu magna consectetur eu sodales felis mattis. Nullam mattis metus quis purus consequat nec imperdiet quam elementum. Vestibulum nec iaculis odio. Nam consequat porta aliquet. Pellentesque in erat nulla. Nam odio lorem, gravida a luctus nec, adipiscing eget sapien. Phasellus eleifend lectus mattis massa blandit vestibulum. Nullam laoreet imperdiet justo eget suscipit.");
		restaurant.setPhoneNumber("064789");
		restaurant.setRating(4.5);
		restaurant.setLongitude(20);
		restaurant.setLatitude(45);

		if (callback != null) {		
			callback.onFinish(restaurant);
		}				
	}

	@Override
	public void getRestaurantOverviews(
			Callback<ArrayList<RestaurantOverview>> callback) {
		
		ArrayList<RestaurantOverview> result = new ArrayList<RestaurantOverview>();
		
		RestaurantOverview ro = new RestaurantOverview();
		ro.setId(1);
		ro.setTitle("Stari pingvin");
		ro.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(ro);

		ro = new RestaurantOverview();
		ro.setId(2);
		ro.setTitle("Peking");
		ro.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(ro);
		
		ro = new RestaurantOverview();
		ro.setId(3);
		ro.setTitle("Burito bar");
		ro.setShortDescription("Lorem ipsum dolor sit amet.");
		
		result.add(ro);
		
		if (callback != null) {		
			callback.onFinish(result);
		}				
	}
}
