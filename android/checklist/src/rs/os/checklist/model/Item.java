package rs.os.checklist.model;


/**
 * Represents some item we need to bring with us or some operation we need to do.
 * 
 * @author zgavrilovic
 *
 */
public class Item extends Named {

	private static final long serialVersionUID = 4626815248987573495L;
	
	public static final String PROP_SUBJECT = "subject";
	public static final String PROP_CHECKLIST = "checklist";
	public static final String PROP_CHECKED = "checked";
	
	
	private Subject subject;
	private Checklist checklist;
	private boolean checked;
	
	public Item copy() {
		Item copy = new Item();
		copy.setSubject(subject);
		copy.setChecklist(checklist);
		copy.setChecked(false);
		return copy;
	}

	// getters and setters

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}