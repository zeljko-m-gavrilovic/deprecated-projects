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
import com.yc.cepelin.compass.VisibleCafesListener;
import com.yc.cepelin.listadapter.CafeOverviewAdapter;
import com.yc.cepelin.model.CafeOverview;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class CafeListActivity extends Activity {
    
    private CafeOverviewAdapter co_adapter;
    private CompassGpsView compassGpsView;
    private VisibleCafesListener visibleCafesListener;
    private DataService dataService;
    private ProgressDialog progressDialog;
   
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafeoverviewlist);
        
        dataService = DataService.getInstance(CafeListActivity.this);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
            	TextView eid = (TextView)v.findViewById(R.id.eid);
                Intent intent = new Intent(CafeListActivity.this, ItemTabActivity.class);
                intent.putExtra(MainTabActivity.ITEM_TYPE, MainTabActivity.CAFE);
                intent.putExtra(MainTabActivity.ID, Integer.parseInt((String) eid.getText()));
                CafeListActivity.this.startActivity(intent);
            }
        });
        getListView().setTextFilterEnabled(true);
        
        compassGpsView = (CompassGpsView) findViewById(R.id.compassGpsView);
        visibleCafesListener = new VisibleCafesListener() {

            @Override
            public void onVisibleCafesChanged(List<CafeOverview> visibleCafes, Map<Integer, Integer> distances) {
                
                ArrayList<CafeOverview> result = new ArrayList<CafeOverview>();
                
                for (CafeOverview visibleCafe : visibleCafes) {
//                    visibleCafe.setDistance(visibleCafe.getId());
                	visibleCafe.setDistance(getDistance(visibleCafe, compassGpsView)); // dragan
                    result.add(visibleCafe);
                }

                CafeListActivity.this.co_adapter = new CafeOverviewAdapter(
                        CafeListActivity.this, 
                        R.layout.cafeoverviewrow, 
                        result); 
                getListView().setAdapter(CafeListActivity.this.co_adapter);  
            }
        };
        
        compassViewFilteringOff();
    }
    
    public static int getDistance(CafeOverview cafe, CompassGpsView compass) { // dragan
		return (int)(1000 * GeoUtil.distanceKm(cafe.getLatitude(), cafe.getLongitude(), compass.getUserLatitude(), compass.getUserLongitude()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassGpsView.removeVisibleCafesListener(visibleCafesListener);
        compassGpsView.unregisterGspSensorListener(); // dragan
    }

    protected void updateList() { // dragan
        progressDialog = ProgressDialog.show(this, getText(R.string.loading_title), getText(R.string.loading_message), true, true);
        dataService.getCafeOverviews(new GetCafeCallback());
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
        return (ListView) findViewById(R.id.cafList);
    }
        
    //Preferred syntax, to get a bit cleaner code
    class GetCafeCallback implements Callback<ArrayList<CafeOverview>> {
		@Override
    	public void onFinish(ArrayList<CafeOverview> result) {
    		for (CafeOverview resultItem:result) resultItem.setDistance(getDistance(resultItem, compassGpsView)); // dragan
    		CafeListActivity.this.co_adapter = new CafeOverviewAdapter(
    									CafeListActivity.this, 
    									R.layout.cafeoverviewrow, 
    									result); 
    		getListView().setAdapter(CafeListActivity.this.co_adapter);
    		progressDialog.dismiss();
    	}
    }
    
    private void compassViewFilteringOn() {
        compassGpsView.setVisibility(View.VISIBLE);
        compassGpsView.addVisibleCafesListener(visibleCafesListener);
    }

    private void compassViewFilteringOff() {
        compassGpsView.setVisibility(View.GONE);
        compassGpsView.removeVisibleCafesListener(visibleCafesListener);
        
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
