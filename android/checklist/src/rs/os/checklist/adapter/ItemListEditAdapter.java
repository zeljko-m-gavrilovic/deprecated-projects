package rs.os.checklist.adapter;

import java.util.List;

import rs.os.checklist.R;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class ItemListEditAdapter extends CategoryBasedListAdapter {

	public ItemListEditAdapter(Context context, int textViewResourceId,
			List<Named> items) {
		super(context, textViewResourceId, items);
	}

	protected View getItemView(int position, View convertView, ViewGroup parent) {
		final Item item = (Item) items.get(position);
		View rowView = convertView;
		if (rowView == null) {
			rowView = li.inflate(R.layout.itemrow, null);
		}

		TextView name = (TextView) rowView.findViewById(R.id.name);
		CheckBox checked = (CheckBox) rowView.findViewById(R.id.checked);
		checked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCheckboxChange(item, (CheckBox) v);
			}
		});

		name.setText(item.getSubject().getName());
		checked.setChecked(item.isChecked());
		return rowView;
	}

	protected void onCheckboxChange(final Item item, CheckBox c) {
		if (c.isChecked()) {
			Long itemId = checklistDbService.addItem(item);
			item.setId(itemId);
			item.setChecked(c.isChecked());
		} else {
			checklistDbService.deleteItem(item);
		}
	}
}