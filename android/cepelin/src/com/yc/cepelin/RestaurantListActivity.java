package com.yc.cepelin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yc.cepelin.compass.CompassGpsView;
import com.yc.cepelin.compass.VisibleRestaurantsListener;
import com.yc.cepelin.listadapter.RestaurantOverviewAdapter;
import com.yc.cepelin.model.RestaurantOverview;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class RestaurantListActivity extends Activity {

    // private DataService dataService;

    private RestaurantOverviewAdapter co_adapter;
    private CompassGpsView compassGpsView;
    private VisibleRestaurantsListener visibleRestaurantsListener;
    private DataService dataService;
    private ProgressDialog progressDialog;
    
    // private Runnable viewRestaurantss;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantoverviewlist);

        dataService = DataService.getInstance(RestaurantListActivity.this);        

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
                TextView eid = (TextView) v.findViewById(R.id.eid);
                Intent intent = new Intent(RestaurantListActivity.this, ItemTabActivity.class);
                intent.putExtra(MainTabActivity.ITEM_TYPE, MainTabActivity.FOOD);
                intent.putExtra(MainTabActivity.ID, Integer.parseInt((String) eid.getText()));
                RestaurantListActivity.this.startActivity(intent);
            }
        });
        getListView().setTextFilterEnabled(true);

        compassGpsView = (CompassGpsView) findViewById(R.id.compassGpsView);
        visibleRestaurantsListener = new VisibleRestaurantsListener() {

            @Override
            public void onVisibleRestaurantsChanged(List<RestaurantOverview> visibleRestaurants,
                    Map<Integer, Integer> distances) {

                ArrayList<RestaurantOverview> result = new ArrayList<RestaurantOverview>();

                for (RestaurantOverview visibleRestaurant : visibleRestaurants) {
                    visibleRestaurant.setDistance(distances.get(visibleRestaurant.getId()));
                    visibleRestaurant.setDistance(getDistance(visibleRestaurant, compassGpsView)); // dragan
                    result.add(visibleRestaurant);
                }

                RestaurantListActivity.this.co_adapter = new RestaurantOverviewAdapter(RestaurantListActivity.this,
                        R.layout.restaurantoverviewrow, result);
                getListView().setAdapter(RestaurantListActivity.this.co_adapter);
            }
        };

        compassViewFilteringOff();
    }

    public static int getDistance(RestaurantOverview restaurant, CompassGpsView compass) { // dragan
		return (int)(1000 * GeoUtil.distanceKm(restaurant.getLatitude(), restaurant.getLongitude(), compass.getUserLatitude(), compass.getUserLongitude()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassGpsView.removeVisibleRestaurantsListener(visibleRestaurantsListener);
        compassGpsView.unregisterGspSensorListener(); // dragan
    }

    protected void updateList() { // dragan
        progressDialog = ProgressDialog.show(this, getText(R.string.loading_title), getText(R.string.loading_message), true, true);
        dataService.getRestaurantOverviews(new GetRestaurantsCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (compassGpsView.getVisibility() == View.VISIBLE) {
            compassViewFilteringOn();
        }
        compassGpsView.registerGspSensorListener(); // dragan
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.resList);
    }

    // Preferred syntax, to get a bit cleaner code
    class GetRestaurantsCallback implements Callback<ArrayList<RestaurantOverview>> {
        @Override
        public void onFinish(ArrayList<RestaurantOverview> result) {
    		for (RestaurantOverview resultItem:result) resultItem.setDistance(getDistance(resultItem, compassGpsView)); // dragan
            RestaurantListActivity.this.co_adapter = new RestaurantOverviewAdapter(RestaurantListActivity.this,
                    R.layout.cafeoverviewrow, result);
            getListView().setAdapter(RestaurantListActivity.this.co_adapter);
    		progressDialog.dismiss();
        }
    }

    private void compassViewFilteringOn() {
        compassGpsView.setVisibility(View.VISIBLE);
        compassGpsView.addVisibleRestaurantsListener(visibleRestaurantsListener);
    }

    private void compassViewFilteringOff() {
        compassGpsView.setVisibility(View.GONE);
        compassGpsView.removeVisibleRestaurantsListener(visibleRestaurantsListener);

        updateList();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (compassGpsView.getVisibility() == View.GONE) {
            menu.findItem(R.id.compass_filter_on).setEnabled(true);
            menu.findItem(R.id.compass_filter_off).setEnabled(false);
        } else {
            menu.findItem(R.id.compass_filter_on).setEnabled(false);
            menu.findItem(R.id.compass_filter_off).setEnabled(true);
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compass_filter_menu, menu);

		MenuItem item = menu.add(getText(R.string.refresh)); // dragan
		item.setIcon(android.R.drawable.ic_menu_rotate);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.compass_filter_on:
            compassViewFilteringOn();
            return true;
        case R.id.compass_filter_off:
            compassViewFilteringOff();
            return true;
        }
		if (item.getTitle().equals(getString(R.string.refresh))) { // dragan
			updateList();
			return true;
		}
        return false;
    }

}
