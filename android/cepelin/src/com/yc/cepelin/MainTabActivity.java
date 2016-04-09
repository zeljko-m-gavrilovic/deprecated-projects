package com.yc.cepelin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {

    public static final String ITEM_TYPE = "itemType";
    public static final String CAFE = "cafe";
    public static final String FOOD = "food";
    public static final String EVENT = "event";
    public static final String ID = "id";

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);
        
        // create intent for every tab
        Intent intentCafe = new Intent(this, CafeListActivity.class);
		Intent intentFood = new Intent(this, RestaurantListActivity.class);
        Intent intentEvent = new Intent(this, EventListActivity.class);
		Intent intentMap = new Intent(this, AllOnMapActivity.class);

        // create tabs and them
        TabHost tabHost = getTabHost();
        tabHost.addTab(
        		tabHost.newTabSpec("cafe_tab")
        			.setIndicator(getText(R.string.cafe_tab), getResources().getDrawable(R.drawable.cafe_icon))
        			.setContent(intentCafe));
        tabHost.addTab(
        		tabHost.newTabSpec("food_tab")
	        		.setIndicator(getText(R.string.food_tab), getResources().getDrawable(R.drawable.restaurant_icon))
	        		.setContent(intentFood));
        tabHost.addTab(
        		tabHost.newTabSpec("events_tab")
    				.setIndicator(getText(R.string.events_tab), getResources().getDrawable(R.drawable.event_icon))
    				.setContent(intentEvent));
        tabHost.addTab(
        		tabHost.newTabSpec("mapall_tab")
    				.setIndicator(getText(R.string.mapall_tab), getResources().getDrawable(android.R.drawable.ic_dialog_map))
    				.setContent(intentMap));

        tabHost.setCurrentTab(0);        
    }

}