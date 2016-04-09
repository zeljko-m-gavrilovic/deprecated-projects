
package rs.esolutions.ecomm.android.activity;

import rs.esolutions.ecomm.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AbstractCitiesActivity extends Activity {

	private static final int DIALOG_SELECT_CITY = 0;
	private static final String SHARED_PREFERENCES = "shared";
	private static final String PREF_CITY = "city";

	protected String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCity(readCity());
		updateTitle();
	}

	private void setCity(String city) {
		this.city = city;
	}

	private void updateTitle() {
		setTitle(getString(R.string.app_name) + " - " + city);
	}

	private String readCity() {
		SharedPreferences shared = getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
		return shared.getString(PREF_CITY, getString(R.string.default_city));
	}

	private void writeCity() {
		SharedPreferences shared = getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
		Editor e = shared.edit();
		e.putString(PREF_CITY, city);
		e.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.select_city);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (0 == item.getItemId())
			showDialog(DIALOG_SELECT_CITY);
		return super.onMenuItemSelected(featureId, item);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		if (0 == id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final String[] cities = getResources().getStringArray(R.array.cities);
			builder.setTitle(R.string.select_city);
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					setCity((String) cities[item]);
					writeCity();
					updateTitle();
				}
			});
			return builder.create();
		}
		return super.onCreateDialog(id);
	}
}
