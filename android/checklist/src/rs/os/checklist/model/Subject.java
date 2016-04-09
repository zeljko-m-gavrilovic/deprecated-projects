package rs.os.checklist.model;

/*
 * Represent an subject which is referenced from checklist item
 */
public class Subject extends Named {

	private static final long serialVersionUID = -4355111933070976980L;
	public static final String PROP_CATEGORY = "category";
	
	private Category category;

	public Subject copy() {
		Subject copy = new Subject();
		copy.setName(name);
		return copy;
	}
	
	// getters and setters
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}