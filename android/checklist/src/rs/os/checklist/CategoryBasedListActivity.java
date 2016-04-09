package rs.os.checklist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rs.os.checklist.adapter.CategoryEditListAdapter;
import rs.os.checklist.dialog.DeleteObjectDialog;
import rs.os.checklist.model.CallbackMethod;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.CategoryPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class CategoryBasedListActivity extends ListActivity {
	public static final int SELECT_CATEGORY = 105;
	
    protected ArrayAdapter<Named> itemListAdapter;
	
    protected CategoryPersistentManager categoryDbService;
	
    protected List<Category> categories;
    protected List<Named> items;
    protected Set<Category> expandedCategories;
	
	public static final String categoryParameter = "category";
	public static final String subjectParameter = "subject";
	
	protected Subject subjectToBeMoved;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        expandedCategories = new HashSet<Category>();
        categoryDbService = ServiceRegistry.getCategoryDbManager(this);
        
        items = new ArrayList<Named>();
		createListAdapter();
		setListAdapter(itemListAdapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
				onItemClicked(v, index);
			}
		});
		
        
        getListView().setTextFilterEnabled(true);

        registerForContextMenu(getListView());
        //Toast.makeText(this, getString(R.string.use_long_press), Toast.LENGTH_LONG);
    }

	@Override
	protected void onResume() {
		super.onResume();
		loadDataFromDb();
		updateList();
	}
	
	protected void loadDataFromDb() {
		categories = categoryDbService.getCategories();
	}
	protected void createListAdapter() {
		itemListAdapter = new CategoryEditListAdapter(this, R.layout.subjectrow, items);		
	}
	
	protected void updateList() {
		// note that following two statements need to be called in subclasses
		int firstVisiblePosition = getListView().getFirstVisiblePosition();
		getListView().setSelection(firstVisiblePosition);
	}
	
	protected void onItemClicked(View v, int index) {
		int firstVisiblePosition = getListView().getFirstVisiblePosition();
		Named item = items.get(index);
		if (item instanceof Category) {
			Category category = (Category) item;
			if (expandedCategories.contains(category)) {
				expandedCategories.remove(category);
			} else {
				expandedCategories.add(category);
			}
			updateList();
			getListView().setSelection(firstVisiblePosition);
		}
	}
	
	// context menu of the list
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.categories_context, menu);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Named item = items.get(info.position);
		if (item instanceof Category) {
			menu.getItem(2).setEnabled(true);
			menu.getItem(3).setEnabled(false);
		} else {
			menu.getItem(2).setEnabled(false);
			menu.getItem(3).setEnabled(true);
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Named itemObject = items.get(info.position);
		if(itemObject instanceof Category) {
			return onCategoryContext(item);
		} else {
			return onSubjectContext(item);
		}
		
	}
	
	protected boolean onCategoryContext(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Category category = (Category) items.get(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editCategory(category);
			return true;
		case R.id.delete:
			new DeleteObjectDialog().openDialog(this, new CallbackMethod() {
				@Override
				public void execute() {
					deleteCategory(category);
				}
			});
			return true;
		case R.id.addSubject:
			Subject subject = newSubject(category);
			editSubject(subject);
			return true;	
		default:
			return super.onContextItemSelected(item);
		}	
	}

	protected boolean onSubjectContext(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Subject subject = (Subject) items.get(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editSubject(subject);
			return true;
		case R.id.delete:
			new DeleteObjectDialog().openDialog(this, new CallbackMethod() {
				@Override
				public void execute() {
					deleteSubject(subject);
				}
			});
			return true;
		case R.id.move:
			subjectToBeMoved = subject;
			showDialog(SELECT_CATEGORY);
			return true;
		default:
			return super.onContextItemSelected(item);
		}	
	}

	protected Category newCategory() {
		Category category = new Category();
		String categoryName = getString(R.string.new_category);
		// because the name must be unique we must create unique name
		category.setName(categoryDbService.createUniqueCategoryName(categoryName));
		//category.setImageName(String.valueOf(R.drawable.checklist_default));
		
		Long categoryId = categoryDbService.addCategory(category);
		category.setId(categoryId);
		return category;
	}

	protected void editCategory(Category category) {
		Intent intent = new Intent(this, CategoryEditorActivity.class);
		intent.putExtra(categoryParameter, category);
		this.startActivity(intent);
	}
	
	protected void deleteCategory(Category category) {
		if(category.getSubjects().isEmpty()) {
			categoryDbService.deleteCategory(category);
			categories = categoryDbService.getCategories();
			updateList();
		} else {
			new AlertDialog.Builder(CategoryBasedListActivity.this)
			.setTitle(getString(R.string.deleteCategory))
			.setMessage(getString(R.string.deleteCategoryViolation))
			.setPositiveButton(getString(R.string.ok),null).create().show();
		}
		
	}
	
	protected Subject newSubject(Category category) {
		Subject subject = new Subject();
		String subjectName = getString(R.string.new_subject);
		// because the name must be unique we must create unique name
		subject.setName(categoryDbService.createUniqueSubjectName(subjectName));
		category.addSubject(subject);
		
		Long subjectId = categoryDbService.addSubject(subject);
		subject.setId(subjectId);
		return subject;
	}
	
	protected void editSubject(Subject subject) {
		Intent intent = new Intent(this, SubjectEditorActivity.class);
		intent.putExtra(subjectParameter, subject);
		this.startActivity(intent);
	}
	
	protected void deleteSubject(Subject subject) {
		List<Checklist> checklists = ServiceRegistry.getChecklistDbManager(this).getChecklists(subject);
		if(checklists.isEmpty()) {
			categoryDbService.deleteSubject(subject);
			categories = categoryDbService.getCategories();
			updateList();
		} else {
			StringBuilder sb = new StringBuilder();
			for(Checklist checklist : checklists) {
				sb.append(checklist.getName());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			new AlertDialog.Builder(CategoryBasedListActivity.this)
			.setTitle(getString(R.string.delete))
			.setMessage(getString(R.string.deleteSubjectViolation) + sb.toString())
			.setPositiveButton(getString(R.string.ok), null)
			.create().show();
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case SELECT_CATEGORY:
			final CharSequence[] items = new CharSequence[categories.size()];
			int i = 0;
			for (Category category : categories) {
				items[i++] = category.getName();
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.move_to_category));
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					subjectToBeMoved.setCategory(categories.get(item));
					categoryDbService.updateSubject(subjectToBeMoved);
					loadDataFromDb();
					updateList();
				}
			});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
}