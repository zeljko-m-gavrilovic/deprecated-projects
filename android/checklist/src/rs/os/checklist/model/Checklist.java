package rs.os.checklist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps data about Checklist. It contains a list of items we need to manage.
 * 
 * @author zgavrilovic
 *
 */
public class Checklist extends Described {

	private static final long serialVersionUID = -2953918756493483993L;

	public static final String PROP_ITEMS = "items";
	
	private List<Item> items;

	public Checklist() {
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		item.setChecklist(this);
		this.items.add(item);
	}
	
	public Checklist copy() {
		Checklist copy = new Checklist();
		copy.setName(name);
		copy.setDescription(description);
		copy.setImageName(imageName);
		
		for(Item item : getItems()) {
			copy.addItem(item.copy());
		}
		return copy;
	}
	
	public Float getPercentage() {
		if(items.size() == 0) {
			return 0f;
		}
		int numOfChecked = getNumbOfCheckedItems();
		float percentage = (float) numOfChecked / (float) items.size() * 100;
		return percentage;
	}

	public int getNumbOfCheckedItems() {
		int numOfChecked = 0;
		for(Item item : items) {
			if(item.isChecked()) {
				numOfChecked ++;
			}
		}
		return numOfChecked;
	}
	
	// getters and setters
	public List<Item> getItems() {
		return items;
	}

}