package rs.os.checklist.adapter;

import java.util.List;

import rs.os.checklist.R;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryEditListAdapter extends CategoryBasedListAdapter {

	public CategoryEditListAdapter(Context context, int textViewResourceId,
			List<Named> items) {
		super(context, textViewResourceId, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Named item = items.get(position);
		View itemView = null;
		
		if(item instanceof Subject) {
			itemView = getItemView(position, convertView, parent);	
		} else {
			itemView = getCategoryView(position, convertView, parent);
		}
		
		return itemView;
	}

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		final Subject subject = (Subject) items.get(position);
		View rowView = convertView;
		if (rowView == null) {
			rowView = li.inflate(R.layout.subjectrow, null);
		}
		
		TextView name = (TextView) rowView.findViewById(R.id.name);
		name.setText(subject.getName());
		return rowView;
	}
	
}