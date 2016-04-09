package rs.os.checklist;

import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.CategoryPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import android.view.Menu;
import android.view.MenuInflater;

public class SubjectEditorActivity extends NamedEditorActivity {

	private CategoryPersistentManager dataService;
	
	@Override
	protected void setItem() {
		dataService = ServiceRegistry.getCategoryDbManager(this);
		this.item = (Named) getIntent().getSerializableExtra(CategoryListEditActivity.subjectParameter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.item_editor_option, menu);
		// disable export functionality
		menu.getItem(1).setEnabled(false);
		return true;
	}

	@Override
	protected void save() {
		this.item.setName(nameTextView.getText().toString());
		if(item.isPersistent()) {
			dataService.updateSubject((Subject) item);
		} else {
			dataService.addSubject((Subject) item);
		}
	}

}