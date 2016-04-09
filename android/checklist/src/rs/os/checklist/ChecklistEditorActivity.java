package rs.os.checklist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import rs.os.checklist.model.Checklist;
import rs.os.checklist.service.ChecklistPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.utility.ImportExportUtility;
import android.app.AlertDialog;
import android.util.Log;

/**
 * Editor for Checklist object
 * @author zgavrilovic
 *
 */
public class ChecklistEditorActivity extends DescribedEditorActivity {

	private ChecklistPersistentManager checklistDbService;
	
	@Override
	protected void setItem() {
		checklistDbService = ServiceRegistry.getChecklistDbManager(this);
		String checklistName = getIntent().getStringExtra(
				ChecklistActivity.checklistNameParameter);
		this.item = checklistDbService.getChecklist(checklistName);
	}

	protected void save() {
		this.item.setName(nameTextView.getText().toString());
		this.item.setDescription(descriptionTextView.getText().toString());
		if(item.isPersistent()) {
			checklistDbService.updateChecklist((Checklist) item);
		} else {
			checklistDbService.addChecklist((Checklist) item);
		}
	}
	
	protected void export() {
		List<Checklist> checklists = new ArrayList<Checklist>();
		checklists.add((Checklist) item);
		String exportedChecklists = null;
		try {
			exportedChecklists = ServiceRegistry.getChecklistJsonManager().exportChecklists(checklists);
			ServiceRegistry.getImportExportService(this).exportObjects(exportedChecklists, 
					ImportExportUtility.EXPORTED_CHECKLISTS_FILE);
		} catch (JSONException je) {
			Log.e(getClass().getName(), getString(R.string.exportOneChecklistException), je);
			new AlertDialog.Builder(this)
            .setTitle("Error").setMessage(R.string.exportOneChecklistException)
            .setPositiveButton(getString(R.string.ok), null).create().show();
		}
	}

}