package rs.os.checklist;

import rs.os.checklist.model.Named;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public abstract class NamedEditorActivity extends Activity {

	protected Named item;
	protected TextView nameTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_editor);
		
		nameTextView = (TextView) findViewById(R.id.name);
		setItem();
		nameTextView.setText(item.getName());
		
		View saveButton = findViewById(R.id.saveItem);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tryToSave();
			}
		});
		View cancelButton = findViewById(R.id.closeItemButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				close();
			}
		});
		setTitle(getTitle() + " \"" + item.getName() + "\"");
	}
	
	abstract protected void setItem();

	/**
	 * If item is transient then add it. If it is persistent then update the item
	 */
	abstract protected void save();
	
	protected void close() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.item_editor_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.saveItem:
			tryToSave();
			return true;
		case R.id.closeItem:
			close();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void tryToSave() {
		try { 
			save();
			close();
		} catch (SQLException sqle) {
			nameTextView.setBackgroundColor(Color.RED);
			Log.e(getClass().getName(), getString(R.string.uniqueNameViolation), sqle);
			new AlertDialog.Builder(NamedEditorActivity.this)
            .setTitle(getString(R.string.error)).setMessage(R.string.uniqueNameViolation)
            .setPositiveButton(getString(R.string.ok), null).create().show();
		}
	}
}