package rs.os.checklist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import rs.os.checklist.model.Category;
import rs.os.checklist.service.CategoryPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.utility.ImportExportUtility;
import android.app.AlertDialog;
import android.util.Log;

public class CategoryEditorActivity extends DescribedEditorActivity {
	private CategoryPersistentManager dataService;
	
	@Override
	protected void setItem() {
		dataService = ServiceRegistry.getCategoryDbManager(this);
		Category category = (Category) getIntent().getSerializableExtra(CategoryListEditActivity.categoryParameter);
		this.item = category;
	}

	protected void save() {
		this.item.setName(nameTextView.getText().toString());
		this.item.setDescription(descriptionTextView.getText().toString());
		if(item.isPersistent()) {
			dataService.updateCategory((Category) item);
		} else {
			dataService.addCategory((Category) item);
		}
	}
	
	protected void export() {
		List<Category> categories = new ArrayList<Category>();
		categories.add((Category) item);
		String exportedCategories = null;
		try {
			exportedCategories = ServiceRegistry.getCategoryJsonManager().exportCategoryList(categories);
			ServiceRegistry.getImportExportService(this).exportObjects(exportedCategories, 
					ImportExportUtility.EXPORTED_CATEGORIES_FILE);
		} catch (JSONException je) {
			Log.e(getClass().getName(), getString(R.string.exportOneCategoryException), je);
			new AlertDialog.Builder(this)
            .setTitle("Error").setMessage(R.string.exportOneCategoryException)
            .setPositiveButton(getString(R.string.ok), null).create().show();
		}
	}

}