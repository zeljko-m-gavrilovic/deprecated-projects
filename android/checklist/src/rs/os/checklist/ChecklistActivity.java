package rs.os.checklist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import rs.os.checklist.adapter.ChecklistListAdapter;
import rs.os.checklist.dialog.DeleteObjectDialog;
import rs.os.checklist.model.CallbackMethod;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.service.ChecklistPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.utility.ImportExportUtility;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Activity for showing checklist objects
 * 
 * @author zgavrilovic
 * 
 */
public class ChecklistActivity extends ListActivity {

	private ChecklistListAdapter checklistListAdapter;
	private ProgressDialog progressDialog;
	private List<Checklist> checklists;
	private List<Checklist> checklistTemplates;
	private ChecklistPersistentManager checklistDbService;

	private final int SELECT_CHECKLIST_TYPE = 102;
	public static final String checklistNameParameter = "checklistName";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		Toast.makeText(this, "Hello world! :)", Toast.LENGTH_LONG);
		//ServiceRegistry.getFileLogger().logInfo("ChecklistActivity starting...");
		try {
		checklistDbService = ServiceRegistry.getChecklistDbManager(this);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int index,
					long arg) {
				Checklist checklist = checklists.get(index);
				Intent intent = new Intent(ChecklistActivity.this,
						ItemListCheckActivity.class);
				intent.putExtra(checklistNameParameter, checklist.getName());
				ChecklistActivity.this.startActivity(intent);
			}
		});

		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

		checklists = new ArrayList<Checklist>();
		checklistListAdapter = new ChecklistListAdapter(this,
				R.layout.checklistrow, checklists);
		getListView().setAdapter(checklistListAdapter);

		parseChecklistTemplates();
		//Toast.makeText(this, getString(R.string.use_long_press), Toast.LENGTH_LONG);
		} catch(Throwable e) {
			e.printStackTrace();
			/*ServiceRegistry.getFileLogger().logFatal("Fatal exception during the startup of " +
													 " ChecklistActivity", e);*/
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateList();
	}

	protected void updateList() {
		progressDialog = ProgressDialog.show(this,
				getText(R.string.loading_data), getText(R.string.loading_data),
				true, true);

		checklists = checklistDbService.getChecklists();
		checklistListAdapter.clear();
		for (Checklist checklist : checklists) {
			checklistListAdapter.add(checklist);
		}

		progressDialog.dismiss();
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case SELECT_CHECKLIST_TYPE:
			final CharSequence[] items = new CharSequence[checklistTemplates
					.size() + 1];
			int i = 0;
			items[i++] = getString(R.string.fromScratch);
			for (Checklist checklist : checklistTemplates) {
				items[i++] = getString(R.string.fromTemplate) + ": "
						+ checklist.getName();
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.use_as_template));
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Checklist checklist = null;
					if (item == 0) { // from scatch
						checklist = newChecklist(null, null);
					} else { // using template
						Checklist checklistTemplate = checklistTemplates
								.get(item - 1);
						checklist = newChecklist(checklistTemplate,
								getString(R.string.templateMark));
					}
					try {
						checklistDbService.addChecklistFromTemplate(checklist);
					} catch (SQLException sqle) {
						Log.e(getClass().getName(),
								getString(R.string.uniqueNameViolation), sqle);
						new AlertDialog.Builder(ChecklistActivity.this)
								.setTitle(getString(R.string.error))
								.setMessage(R.string.uniqueNameViolation)
								.setPositiveButton(getString(R.string.ok), null)
								.create().show();
					}
					editChecklist(checklist);
				}
			});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	// options menu of the application
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.checklists_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.new_checklist:
			showDialog(SELECT_CHECKLIST_TYPE);
			return true;
		case R.id.categories:
			categories();
			return true;
		case R.id.exportList:
			startExport();
			return true;
		case R.id.importList:
			startImport();
			return true;
			/*
			 * case R.id.quit: quit(); return true;
			 */
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// context menu of the list
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.checklists_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final Checklist checklist = checklists.get(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editChecklist(checklist);
			return true;
		case R.id.editItems:
			editItems(checklist);
			return true;
		case R.id.copy:
			Checklist copy = newChecklist(checklist,
					getString(R.string.copyMark));
			checklistDbService.addChecklist(copy);
			editChecklist(copy);
			return true;
		case R.id.delete:
			new DeleteObjectDialog().openDialog(this, new CallbackMethod() {
				@Override
				public void execute() {
					checklistDbService.deleteChecklist(checklist);
					updateList();
				}
			});
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Checklist newChecklist(Checklist checklistTemplate, String suffix) {
		Checklist checklist = new Checklist();
		String checklistName = null;
		if (checklistTemplate != null) {
			checklist = checklistTemplate.copy();
			if (suffix != null) {
				checklistName = checklistTemplate.getName() + suffix;
			}
		} else {
			checklistName = getString(R.string.new_checklist);
		}
		// because the name must be unique we must create unique name
		checklist.setName(checklistDbService.createUniqueName(checklistName));

		// checklist.setImageName(String.valueOf(R.drawable.checklist_default));
		return checklist;
	}

	private void editChecklist(Checklist checklist) {
		Intent intent = new Intent(this, ChecklistEditorActivity.class);
		intent.putExtra(checklistNameParameter, checklist.getName());
		this.startActivity(intent);
	}

	private void editItems(Checklist checklist) {
		Intent intent = new Intent(ChecklistActivity.this,
				ItemListEditActivity.class);
		intent.putExtra(checklistNameParameter, checklist.getName());
		ChecklistActivity.this.startActivity(intent);
	}

	private void categories() {
		Intent intent = new Intent(this, CategoryListEditActivity.class);
		this.startActivity(intent);
	}

	/*
	 * private void quit() { new QuitApplicationDialog().openDialog(this, new
	 * CallbackMethod() {
	 * 
	 * @Override public void execute() { finish(); } }); }
	 */

	private void parseChecklistTemplates() {
		//ServiceRegistry.getFileLogger().logInfo("parseChecklistTemplates started");
		InputStream is = getResources().openRawResource(R.raw.checklists);
		try {
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String jsontext = new String(buffer);
			// load checklist templates from file
			checklistTemplates = ServiceRegistry.getChecklistJsonManager()
					.importChecklists(jsontext);
		} catch (Exception e) {
			Log.e(getClass().getName(),
					getString(R.string.exceptionCreatingChecklistTemplates), e);
			new AlertDialog.Builder(ChecklistActivity.this)
					.setTitle(getString(R.string.error))
					.setMessage(
							getString(R.string.exceptionCreatingChecklistTemplates)
									+ "\n" + e.getMessage())
					.setPositiveButton(getString(R.string.ok), null).create()
					.show();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				//ServiceRegistry.getFileLogger().logFatal("parseChecklistTemplates fatal exception", e);
			}
		}
		//ServiceRegistry.getFileLogger().logInfo("parseChecklistTemplates ended");
	}

	private void startExport() {
		String exportedChecklists = null;
		try {
			exportedChecklists = ServiceRegistry.getChecklistJsonManager()
					.exportChecklists(checklists);
			ServiceRegistry.getImportExportService(this).exportObjects(
					exportedChecklists,
					ImportExportUtility.EXPORTED_CHECKLISTS_FILE);
		} catch (JSONException je) {
			Log.e(getClass().getName(),
					getString(R.string.parseChecklistsToJsonException));
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.error))
					.setMessage(
							getString(R.string.parseChecklistsToJsonException))
					.setPositiveButton(getString(R.string.ok), null).create()
					.show();
		}
	}

	private void startImport() {
		Intent intentBrowseFiles = new Intent(Intent.ACTION_GET_CONTENT);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		String type = "plain/txt";
		intentBrowseFiles.setDataAndType(startDir, type);
		intentBrowseFiles.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intentBrowseFiles,
				ImportExportUtility.PICK_FILE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		String contents = ServiceRegistry.getImportExportService(this)
				.onActivityResult(requestCode, resultCode, intent);
		try {
			List<Checklist> checklistsForImport = ServiceRegistry
					.getChecklistJsonManager().importChecklists(contents);
			ImportResultsReport importResults = checklistDbService
					.importChecklists(checklistsForImport);
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.importData))
					.setMessage(importResults.createSummary(this))
					.setPositiveButton(getString(R.string.ok), null).create()
					.show();

		} catch (JSONException je) {
			Log.e(getClass().getName(),
					getString(R.string.parseChecklistsToJsonException));
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.error))
					.setMessage(
							getString(R.string.parseChecklistsToJsonException))
					.setPositiveButton(getString(R.string.ok), null).create()
					.show();
		}
	}

}