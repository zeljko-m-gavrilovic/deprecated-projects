package rs.os.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.os.checklist.adapter.ItemListCheckAdapter;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

/**
 * Activity for checking items in checklist
 * @author zgavrilovic
 *
 */
public class ItemListCheckActivity extends ItemListEditActivity {
	public static final String itemParameter = "item";
	private boolean groupByCategory = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.checklist) + " \"" + checklist.getName() + "\"");
	}

	
	protected void createListAdapter() {
		itemListAdapter = new ItemListCheckAdapter(this, R.layout.itemrow, items);		
	}

    @Override
    protected void updateList() {
    	int firstVisiblePosition = getListView().getFirstVisiblePosition();
    	Map<Category, List<Item>> categoriesMap = new HashMap<Category, List<Item>>();
		for(Item item : checklist.getItems()) {
			Subject subject = categoryDbService.getSubject(item.getSubject().getName());
			if(!categoriesMap.containsKey(subject.getCategory())) {
				categoriesMap.put(subject.getCategory(), new ArrayList<Item>());
			}
			List<Item> items = categoriesMap.get(subject.getCategory());
			items.add(item);
		}
    	
    	// merge categories and items into one list
		items = new ArrayList<Named>();
		for (Category category : categoriesMap.keySet()) {
			if (groupByCategory) {
				items.add(category);
				if (expandedCategories.contains(category)) {
					items.addAll(categoriesMap.get(category));
				} 
			} else { 
				items.addAll(categoriesMap.get(category));
			}
		}
		
		itemListAdapter.clear();
		for (Named item : items) {
			itemListAdapter.add(item);
		}
		getListView().setSelection(firstVisiblePosition);
    }
    
    @Override
	protected void onItemClicked(View v, int index) {
		super.onItemClicked(v, index);
		Named named = items.get(index);
		if(!(named instanceof Category)) {
			Item item = (Item) named;
			item.setChecked(!item.isChecked());
			checklistDbService.updateItem(item);
			CheckBox checkBox = (CheckBox) v.findViewById(R.id.checked);
			checkBox.setChecked(item.isChecked());
		}
	}

    // options menu of the application
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.items_check_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.groupItems:
			String groupByCategoryTitle = getString(R.string.groupItems);
			if((item.getTitle().equals(groupByCategoryTitle))) {
				groupByCategory = true;
				item.setTitle(R.string.dontGroupItems);
			} else {
				groupByCategory = false;
				item.setTitle(R.string.groupItems);
			}
			expandedCategories.clear();
			updateList();
			return true;
		case R.id.checkAll:
			for(Item t : checklist.getItems()) {
				t.setChecked(true);
			}
			checklistDbService.updateItems(checklist.getItems());
			loadDataFromDb();
			updateList();
			return true;
		case R.id.uncheckAll:
			for(Item t : checklist.getItems()) {
				t.setChecked(false);
			}
			checklistDbService.updateItems(checklist.getItems());
			loadDataFromDb();
			updateList();
			return true;
		case R.id.new_category:
			Category category = newCategory();
			newSubject(category);
			editCategory(category);
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		
		}
	}

	@Override
	protected Subject newSubject(Category category) {
		Subject newSubject = super.newSubject(category);
		Item newItem = new Item();
		newItem.setChecklist(checklist);
		newItem.setSubject(newSubject);
		checklistDbService.addItem(newItem);
		return newSubject;
	}


	@Override
	protected void deleteItem(Item item) {
		checklistDbService.deleteItem(item);
	}
	
}