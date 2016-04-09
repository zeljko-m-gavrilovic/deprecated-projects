package rs.os.checklist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.model.Named;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.utility.ImportExportUtility;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CategoryListEditActivity extends CategoryBasedListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    protected void updateList() {
    	int firstVisiblePosition = getListView().getFirstVisiblePosition();
    	items = new ArrayList<Named>();
		for (Category category : categories) {
			items.add(category);
			if(expandedCategories.contains(category)) {
				items.addAll(category.getSubjects());
			}
		}
		
		itemListAdapter.clear();
		for (Named item : items) {
			itemListAdapter.add(item);
		}
		getListView().setSelection(firstVisiblePosition);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.categories_option, menu);
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_category:
			Category category = newCategory();
			editCategory(category);
			return true;
		case R.id.exportList:
			export();
			return true;
		case R.id.importList:
			importCategories();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	private void export() {
		String exportedCategories = null;
		try {
			exportedCategories = ServiceRegistry.getCategoryJsonManager().exportCategoryList(categories);
			ServiceRegistry.getImportExportService(this).exportObjects(exportedCategories, 
					ImportExportUtility.EXPORTED_CATEGORIES_FILE);
		} catch (JSONException je) {
			Log.e(getClass().getName(), getString(R.string.parseCategoriesToJsonException));
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.error))
					.setMessage(getString(R.string.parseCategoriesToJsonException))
					.setPositiveButton(getString(R.string.ok), null).create()
					.show();
		}
	}

	private void importCategories() {
		Intent intentBrowseFiles = new Intent(Intent.ACTION_GET_CONTENT);
		Uri startDir = Uri.fromFile(new File("/sdcard"));
		String type = "plain/txt";
		intentBrowseFiles.setDataAndType(startDir, type);
		intentBrowseFiles.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intentBrowseFiles, ImportExportUtility.PICK_FILE_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String contents = ServiceRegistry.getImportExportService(this).onActivityResult(requestCode, resultCode, intent);	
		List<Category> categoriesForImport = new ArrayList<Category>();
		try {
			categoriesForImport = ServiceRegistry.getCategoryJsonManager().importCategoryList(contents);
			ImportResultsReport importResults = categoryDbService.importCategories(categoriesForImport);

			new AlertDialog.Builder(this)
			.setTitle(getString(R.string.importData))
			.setMessage(importResults.createSummary(this))
			.setPositiveButton(getString(R.string.ok), null)
			.create().show();
			
		} catch(JSONException je) {	
			Log.e(getClass().getName(), getString(R.string.parseCategoriesToJsonException));
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.error))
					.setMessage(getString(R.string.parseCategoriesToJsonException))
					.setPositiveButton(getString(R.string.ok), null)
					.create().show();
		}
	}

}