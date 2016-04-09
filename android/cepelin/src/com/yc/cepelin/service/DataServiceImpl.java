package com.yc.cepelin.service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.yc.cepelin.model.AbstractItem;
import com.yc.cepelin.model.AbstractOverview;
import com.yc.cepelin.model.Cafe;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.model.Comment;
import com.yc.cepelin.model.Event;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.model.Restaurant;
import com.yc.cepelin.model.RestaurantOverview;

public class DataServiceImpl extends DataService {

	DataServiceImpl(Context context) {
		super(context);
	}

	@Override
	public void addComment(int itemId, double rating, String comment,
			final Callback<Integer> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				JSONObject json = result.optJSONObject(0);
				if (json != null) {
					callback.onFinish(json.optInt("id"));
				} else {
					callback.onFinish(-1);
				}
				
			}
		});
		service.execute(String.format("comment/add?venueId=%d&text=%s&rating=%f", itemId, URLEncoder.encode(comment), rating));		
	}

	@Override
	public void addPhotoForItem(int itemId, Drawable photo,
			Callback<Integer> callback) {		
		BitmapDrawable p = (BitmapDrawable) photo;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		p.getBitmap().compress(CompressFormat.JPEG, 95, stream);
		HttpPostPictureService ps = new HttpPostPictureService(itemId, stream, callback);
		ps.execute("photo/upload");
		//integer in callback can not be trusted, it is not populated in service!
	}

	@Override
	public void getCafe(int cafeId, final Callback<Cafe> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				Cafe cafe = new Cafe();
				fillAbstractItem(cafe, result.optJSONObject(0));
				callback.onFinish(cafe);
			}
		});
		service.execute("cafe/cafe?id=" + cafeId);		
	}

	@Override
	public void getCafeOverviews(final Callback<ArrayList<CafeOverview>> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				ArrayList<CafeOverview> array = new ArrayList<CafeOverview>();
				for (int i = 0; i < result.length(); i++) {
					JSONObject json = result.optJSONObject(i);
					CafeOverview co = new CafeOverview();
					fillAbstractOverview(co, json);
					array.add(co);
				}
				callback.onFinish(array);
			}
		});
		service.execute("cafe/cafeOverview");
	}

	@Override
	public void getComments(int itemId, final Callback<ArrayList<Comment>> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				ArrayList<Comment> array = new ArrayList<Comment>();
				for (int i = 0; i < result.length(); i++) {
					JSONObject json = result.optJSONObject(i);
					Comment comment = new Comment();
					comment.setId(json.optInt("id"));
					comment.setItemId(json.optInt("venueId"));
					comment.setText(json.optString("text"));
					comment.setRating(json.optDouble("rating"));
					array.add(comment);
				}
				callback.onFinish(array);
			}
		});
		service.execute("comment/listForVenue?venueId=" + itemId);		
	}

	@Override
	public void getEvent(int eventId, final Callback<Event> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				Event event = new Event();
				fillAbstractItem(event, result.optJSONObject(0));
				callback.onFinish(event);
			}
		});
		service.execute("event/event?id=" + eventId);
	}

	@Override
	public void getEventOverviews(final Callback<ArrayList<EventOverview>> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				ArrayList<EventOverview> array = new ArrayList<EventOverview>();
				for (int i = 0; i < result.length(); i++) {
					JSONObject json = result.optJSONObject(i);
					EventOverview eo = new EventOverview();
					fillAbstractOverview(eo, json);
					array.add(eo);
				}
				callback.onFinish(array);
			}
		});
		service.execute("event/eventOverview");		
	}

	@Override
	public void getPhotoForItem(int itemId, int photoId,
			final Callback<Drawable> callback) {
		HttpGetPictureService service = new HttpGetPictureService(new Callback<Bitmap>() {
			@Override
			public void onFinish(Bitmap result) {
				if (result == null) callback.onFinish(null);
				Drawable d = new BitmapDrawable(result);
				callback.onFinish(d);	
			}
		});
		service.execute("photo/getPhoto?id=" + photoId);
	}

	@Override
	public void getPhotoIdsForItem(int itemId,
			final Callback<ArrayList<Integer>> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				ArrayList<Integer> resultArray = new ArrayList<Integer>();
				for (int i = 0; i < result.length(); i++) {
					resultArray.add(result.optJSONObject(i).optInt("photoId"));
				}				
				callback.onFinish(resultArray);
			}
		});
		service.execute("venue/getPhotos?id=" + itemId);		
	}

	@Override
	public void getRestaurant(int restaurantId, final Callback<Restaurant> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				Restaurant restaurant = new Restaurant();
				fillAbstractItem(restaurant, result.optJSONObject(0));
				callback.onFinish(restaurant);
			}
		});
		service.execute("restaurant/restaurant?id=" + restaurantId);		
	}

	@Override
	public void getRestaurantOverviews(
			final Callback<ArrayList<RestaurantOverview>> callback) {
		HttpGetService service = new HttpGetService(new Callback<JSONArray>() {
			@Override
			public void onFinish(JSONArray result) {
				ArrayList<RestaurantOverview> array = new ArrayList<RestaurantOverview>();
				for (int i = 0; i < result.length(); i++) {
					JSONObject json = result.optJSONObject(i);
					RestaurantOverview ro = new RestaurantOverview();
					fillAbstractOverview(ro, json);
					array.add(ro);
				}
				callback.onFinish(array);
			}
		});
		service.execute("restaurant/restaurantOverview");
	}
	
	private void fillAbstractOverview(AbstractOverview ao, JSONObject json) {
		if (json == null) return;
		ao.setId(json.optInt("id"));
		ao.setTitle(json.optString("title"));
		ao.setShortDescription(json.optString("shortDescription"));
		ao.setLongitude(json.optDouble("longitude"));
		ao.setLatitude(json.optDouble("latitude"));
		ao.setRating(json.optDouble("rating"));		
	}

	private void fillAbstractItem(AbstractItem item, JSONObject json) {
		if (json == null) return;
		item.setId(json.optInt("id"));
		item.setTitle(json.optString("title"));
		item.setShortDescription(json.optString("shortDescription"));
		item.setLongDescription(json.optString("longDescription"));
		item.setPhoneNumber(json.optString("phoneNumber"));
		item.setLongitude(json.optDouble("longitude"));
		item.setLatitude(json.optDouble("latitude"));
		item.setRating(json.optDouble("rating"));		
	}

}
