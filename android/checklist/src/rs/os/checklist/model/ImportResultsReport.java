package rs.os.checklist.model;

import java.util.ArrayList;
import java.util.List;

import rs.os.checklist.R;
import android.content.Context;

/**
 * Holds info about imported data 
 * 
 * @author zgavrilovic
 *
 */
public class ImportResultsReport {
	private List<Named> inserted;
	private List<Named> updated;
	private List<Named> ignored;

	// default constructor
	public ImportResultsReport() {
		inserted = new ArrayList<Named>();
		updated = new ArrayList<Named>();
		ignored = new ArrayList<Named>();
	}
	
	public String createSummary(Context context) {
		StringBuilder sb = new StringBuilder();
		sb.append(context.getString(R.string.inserted)).append("(" + getInserted().size()+ "):");
		sb.append("\n\t");
		listToString(inserted, sb);
		sb.append("\n");
		sb.append(context.getString(R.string.updated)).append("(" + getUpdated().size()+ "):");
		sb.append("\n\t");
		listToString(updated, sb);
		sb.append("\n");
		sb.append(context.getString(R.string.ignored)).append("(" + getIgnored().size()+ "):");
		sb.append("\n\t");
		listToString(ignored, sb);
		return sb.toString();
	}

	private void listToString(List<Named> named, StringBuilder sb) {
		for(Named name : named) {
			sb.append(name.getName()).append(',');
		}
		if (!named.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
	}
	
	// getters and setters
	public List<Named> getInserted() {
		return inserted;
	}

	public void addInserted(Named object) {
		inserted.add(object);	
	}
	
	public List<Named> getUpdated() {
		return updated;
	}
	
	public void addUpdated(Named object) {
		updated.add(object);	
	}
	
	public List<Named> getIgnored() {
		return ignored;
	}
	
	public void addIgnored(Named object) {
		ignored.add(object);	
	}
	
}