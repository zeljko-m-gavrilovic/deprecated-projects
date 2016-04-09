package rs.os.checklist.adapter;

import java.text.DecimalFormat;
import java.util.List;

import rs.os.checklist.R;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.utility.ResourceUtility;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Defines adapter for CheckListActivity
 * @author zgavrilovic
 *
 */
public class ChecklistListAdapter extends ArrayAdapter<Checklist> {

	private List<Checklist> items;
	private LayoutInflater li;

	public ChecklistListAdapter(Context context, int textViewResourceId,
			List<Checklist> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Checklist checklist = items.get(position);
		View rowView = convertView;
		if (rowView == null) {
			rowView = li.inflate(R.layout.checklistrow, null);
		}
		
		TextView name = (TextView) rowView.findViewById(R.id.name);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		ImageView ico =(ImageView) rowView.findViewById(R.id.ico);
		TextView statistics = (TextView) rowView.findViewById(R.id.stats);
		
		name.setText(checklist.getName());
		description.setText(checklist.getDescription());
		
		String itemized = new String(checklist.getNumbOfCheckedItems() + "/" + checklist.getItems().size());
		String percentage = new DecimalFormat("#").format(checklist.getPercentage());
		String percentageFormatted = String.format(" (%1s %%)", percentage);
		statistics.setText(itemized + "\n" + percentageFormatted);
		
		ico.setImageBitmap(null);
		ResourceUtility.setImage(checklist, ico, getContext());
		return rowView;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}

}