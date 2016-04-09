package com.yc.cepelin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yc.cepelin.model.AbstractItem;
import com.yc.cepelin.model.Cafe;
import com.yc.cepelin.model.Event;
import com.yc.cepelin.model.Restaurant;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class InfoActivity extends Activity {
	AbstractItem item = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_info);

		Button buttonCall = (Button)findViewById(R.id.buttonCall);
		
		View.OnClickListener phoneNoTouchListener = new PhoneNoTouchListener(buttonCall);
		buttonCall.setOnClickListener(phoneNoTouchListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String itemType = (String) intent.getExtras().get(
				MainTabActivity.ITEM_TYPE);
		Integer itemId = intent.getExtras().getInt(MainTabActivity.ID);

		getItemData(itemType, itemId);
	}

	private void getItemData(String itemType, Integer itemId) {
		DataService dataService = DataService.getInstance(this);

		if (MainTabActivity.CAFE.equals(itemType)) {
			dataService.getCafe(itemId, new CafeItemCallback());
		} else if (MainTabActivity.FOOD.equals(itemType)) {
			dataService.getRestaurant(itemId, new RestaurantItemCallback());
		} else if (MainTabActivity.EVENT.equals(itemType)) {
			dataService.getEvent(itemId, new EventItemCallback());
		} else {
			// some default type... cafe :)
			dataService.getCafe(itemId, new CafeItemCallback());
		}

	}

	private void onFinish(AbstractItem result) {
		item = result;

		TextView itemName = (TextView) findViewById(R.id.itemName);
		itemName.setText(result.getTitle());

		Button buttonCall = (Button)findViewById(R.id.buttonCall);
		if (result.getPhoneNumber() == null || "".equals(result.getPhoneNumber())) {
			buttonCall.setVisibility(View.GONE);
		} else {
			buttonCall.setText(result.getPhoneNumber());
			buttonCall.setVisibility(View.VISIBLE);
		}

		TextView itemDesc = (TextView) findViewById(R.id.itemDesc);
		itemDesc.setText(result.getLongDescription().replaceAll("\r\n", "\n"));

		double rating = result.getRating();
		RatingBar itemCurrentRating = (RatingBar) findViewById(R.id.itemCurrentRating);
		itemCurrentRating.setRating((float) rating);
	}

	private class PhoneNoTouchListener implements View.OnClickListener {
		private TextView phoneNo;

		public PhoneNoTouchListener(TextView phoneNo) {
			this.phoneNo = phoneNo;
		}

		@Override
		public void onClick(View v) {
			makeCall(phoneNo.getText().toString());
		}

		private void makeCall(String number) {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + number));
			startActivity(intent);
		}

	}

	private class CafeItemCallback implements Callback<Cafe> {
		@Override
		public void onFinish(Cafe result) {
			InfoActivity.this.onFinish(result);
		}
	}

	private class RestaurantItemCallback implements Callback<Restaurant> {
		@Override
		public void onFinish(Restaurant result) {
			InfoActivity.this.onFinish(result);
		}
	}

	private class EventItemCallback implements Callback<Event> {
		@Override
		public void onFinish(Event result) {
			InfoActivity.this.onFinish(result);
		}
	}

}