package rs.os.checklist.adapter;

import java.util.List;

import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import android.content.Context;
import android.widget.CheckBox;


/**
 * Defines adapter for ItemListCheckActivity
 * @author zgavrilovic
 *
 */
public class ItemListCheckAdapter extends ItemListEditAdapter {

	public ItemListCheckAdapter(Context context, int textViewResourceId,
			List<Named> items) {
		super(context, textViewResourceId, items);
	}

	@Override
	protected void onCheckboxChange(Item item, CheckBox c) {
		item.setChecked(c.isChecked());
		checklistDbService.updateItem(item);
	}
	
}