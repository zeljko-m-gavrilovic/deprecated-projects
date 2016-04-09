package rs.os.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rs.os.checklist.adapter.ItemListEditAdapter;
import rs.os.checklist.dialog.DeleteObjectDialog;
import rs.os.checklist.model.CallbackMethod;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.ChecklistPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ItemListEditActivity extends CategoryBasedListActivity {

	protected Checklist checklist;
	protected ChecklistPersistentManager checklistDbService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checklistDbService = ServiceRegistry.getChecklistDbManager(this);
		String checklistName = getIntent().getStringExtra(ChecklistActivity.checklistNameParameter);
		checklist = checklistDbService.getChecklist(checklistName);
	}

	protected void createListAdapter() {
		itemListAdapter = new ItemListEditAdapter(this, R.layout.itemrow, items);		
	}
	
	protected void loadDataFromDb() {
		super.loadDataFromDb();
		checklist = checklistDbService.getChecklist(checklist.getName());
	}
	
	@Override
	protected void updateList() {
		checklist = checklistDbService.getChecklist(checklist.getName());
		int firstVisiblePosition = getListView().getFirstVisiblePosition();
		Map<Subject, Item> subjectsFromChecklist = new HashMap<Subject, Item>();
		for (Item item : checklist.getItems()) {
			subjectsFromChecklist.put(item.getSubject(), item);
		}

		items = new ArrayList<Named>();
		for (Category category : categories) {
			items.add(category);
			if (expandedCategories.contains(category)) {
				for (Subject subject : category.getSubjects()) {
					Item item = null;
					if (subjectsFromChecklist.containsKey(subject)) {
						item = subjectsFromChecklist.get(subject);
						item.setChecked(true);
					} else {
						item = new Item();
						item.setChecklist(checklist);
						item.setSubject(subject);
					}
					items.add(item);
				}
			}
		}

		itemListAdapter.clear();
		for (Named item : items) {
			itemListAdapter.add(item);
		}
		getListView().setSelection(firstVisiblePosition);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Named itemObject = items.get(info.position);
		if (itemObject instanceof Category) {
			return onCategoryContext(item);
		} else {
			return onSubjectContext(item);
		}
	}
	
	@Override
	protected boolean onSubjectContext(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Item itemObject =(Item) items.get(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editSubject(itemObject.getSubject());
			return true;
		case R.id.delete:
			new DeleteObjectDialog().openDialog(this, new CallbackMethod() {
				@Override
				public void execute() {
					deleteItem(itemObject);
					loadDataFromDb();
					updateList();
				}
			});
			return true;
		case R.id.move:
			subjectToBeMoved = itemObject.getSubject();
			showDialog(SELECT_CATEGORY);
			return true;	
		default:
			return super.onContextItemSelected(item);
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.items_edit_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_category:
			Category category = newCategory();
			editCategory(category);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	protected void deleteItem(Item item) {
		deleteSubject(item.getSubject());
	}
}