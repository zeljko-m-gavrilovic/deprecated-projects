package com.yc.cepelin;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yc.cepelin.listadapter.EventOverviewAdapter;
import com.yc.cepelin.model.EventOverview;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class EventListActivity extends LocationActivity {

    private EventOverviewAdapter co_adapter;
    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventoverviewlist);

        super.init();
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
                TextView eid = (TextView) v.findViewById(R.id.eid);
                Intent intent = new Intent(EventListActivity.this, ItemTabActivity.class);
                intent.putExtra(MainTabActivity.ITEM_TYPE, MainTabActivity.EVENT);
                intent.putExtra(MainTabActivity.ID, Integer.parseInt((String) eid.getText()));
                EventListActivity.this.startActivity(intent);
            }
        });

        getListView().setTextFilterEnabled(true);
        updateList();
    }

    protected void updateList() { // dragan
        progressDialog = ProgressDialog.show(this, getText(R.string.loading_title), getText(R.string.loading_message), true, true);
        DataService dataService = DataService.getInstance(EventListActivity.this);        		
		dataService.getEventOverviews(new GetEventCallback());
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	private ListView getListView() {
        return (ListView) findViewById(R.id.evList);
    }

    // Preferred syntax, to get a bit cleaner code
    class GetEventCallback implements Callback<ArrayList<EventOverview>> {
        @Override
        public void onFinish(ArrayList<EventOverview> result) {
    		for (EventOverview resultItem:result) resultItem.setDistance(GeoUtil.getDistance(resultItem.getLatitude(), resultItem.getLongitude(), latitude, longitude)); // dragan
            EventListActivity.this.co_adapter = new EventOverviewAdapter(EventListActivity.this,
                    R.layout.cafeoverviewrow, result);
            getListView().setAdapter(EventListActivity.this.co_adapter);
    		progressDialog.dismiss();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(getText(R.string.refresh));
		item.setIcon(android.R.drawable.ic_menu_rotate);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.refresh))) {
			updateList();
			return true;
		}
        return false;
    }

}
