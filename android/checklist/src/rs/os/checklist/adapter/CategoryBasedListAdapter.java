package rs.os.checklist.adapter;

import java.util.List;

import rs.os.checklist.R;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Named;
import rs.os.checklist.service.ChecklistPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.utility.ResourceUtility;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class CategoryBasedListAdapter extends ArrayAdapter<Named> {

	enum ItemType {Category, Item}
	protected List<Named> items;
	protected ChecklistPersistentManager checklistDbService;
	protected LayoutInflater li;
	
	public CategoryBasedListAdapter(Context context, int textViewResourceId, List<Named> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checklistDbService = ServiceRegistry.getChecklistDbManager(getContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Named item = items.get(position);
		View itemView = null;
		
		if(item instanceof Category) {
			itemView = getCategoryView(position, convertView, parent);
		} else {
			itemView = getItemView(position, convertView, parent);
		}
		
		return itemView;
	}
	
	protected abstract View getItemView(int position, View convertView, ViewGroup parent);
	
	protected View getCategoryView(int position, View convertView, ViewGroup parent) {
		final Category category = (Category) items.get(position);
		View rowView = convertView;
		if (rowView == null) {
			rowView = li.inflate(R.layout.categoryrow, null);
		}
		
		TextView name = (TextView) rowView.findViewById(R.id.name);
		TextView description = (TextView) rowView.findViewById(R.id.desription);
		ImageView ico =(ImageView) rowView.findViewById(R.id.ico);
		name.setText(category.getName());
		description.setText(category.getDescription());
		ico.setImageBitmap(null);
		ResourceUtility.setImage(category, ico, getContext());
		return rowView;
	}

	@Override
	public int getViewTypeCount() {
		return ItemType.values().length;
	}


	@Override
	public int getItemViewType(int position) {
		Named item = items.get(position);
		if(item instanceof Category) {
			return ItemType.Category.ordinal();
		} else {
			return ItemType.Item.ordinal();
		}
	}
	
}