package rs.os.checklist;

import rs.os.checklist.model.Described;
import rs.os.checklist.utility.ResourceUtility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Implement functionalities common to both checklist and category editors  
 * @author zgavrilovic
 *
 */
public abstract class DescribedEditorActivity extends Activity {

	private final int SELECT_IMAGE = 101;
	private final int APPLICATION_IMAGE = 102;
	private final int SD_CARD_IMAGE = 103;
	
	protected Described item;
	protected TextView nameTextView;
	protected TextView descriptionTextView;
	protected ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_desc_image_editor);
		
		nameTextView = (TextView) findViewById(R.id.name);
		descriptionTextView = (TextView) findViewById(R.id.description);
		imageView = (ImageView) findViewById(R.id.itemImage);
		
		setItem();
		
		nameTextView.setText(item.getName());
		descriptionTextView.setText(item.getDescription());
		updateImage();
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(SELECT_IMAGE);
			}
		});
		
		
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
	
	private void updateImage() {
		//imageView.setImageResource(R.drawable.icon);
		ResourceUtility.setImage(item, imageView, this);
	}
	
	abstract protected void setItem();
	abstract protected void export();

	/**
	 * If item is transient then add it. If it is persistent then update the item
	 */
	abstract protected void save();
	
	protected void close() {
		finish();
	}

	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    switch(id) {
	    case SELECT_IMAGE:
	    	final String[] items = getResources().getStringArray(R.array.pickImage);

		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle(getString(R.string.use_as_template));
		    builder.setItems(items, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int item) {
		            if(item == 0) {
		            	startActivityForResult(new Intent(DescribedEditorActivity.this, GalleryActivity.class),
		    					APPLICATION_IMAGE);
		            } else {
		            	startActivityForResult(new Intent(Intent.ACTION_PICK,
		    					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
		    					SD_CARD_IMAGE);
		            }
		        }
		    });
		    dialog = builder.create();
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == APPLICATION_IMAGE) {
			item.setImageName(String.valueOf(resultCode));
			save();
			updateImage();
		}
		if (requestCode == SD_CARD_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				item.setImageName(selectedImageUri.toString());
				save();
				updateImage();
			}
		}
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
		case R.id.exportItem:
			export();
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
			new AlertDialog.Builder(DescribedEditorActivity.this)
            .setTitle(getString(R.string.error)).setMessage(R.string.uniqueNameViolation)
            .setPositiveButton(getString(R.string.ok), null).create().show();
		}
	}
}