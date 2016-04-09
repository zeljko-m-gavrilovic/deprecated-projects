package rs.os.checklist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keeps subjects organized into categorizes.
 * 
 * @author Gavrilovici
 *
 */
public class Category extends Described {

	private static final long serialVersionUID = 2989161419236021339L;
	public static final String PROP_SUBJECTS = "subjects";
	
	private List<Subject> subjects;

	public Category() {
		subjects = new ArrayList<Subject>();
	}
	
	public void addSubject(Subject subject) {
		subject.setCategory(this);
		this.subjects.add(subject);
	}
	
	public Category copy() {
		Category copy = new Category();
		copy.setName(name);
		copy.setDescription(description);
		copy.setImageName(imageName);
		
		for(Subject subject : getSubjects()) {
			copy.addSubject(subject.copy());
		}
		return copy;
	}

	//getters and setters
	public List<Subject> getSubjects() {
		Collections.sort(subjects);
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
}