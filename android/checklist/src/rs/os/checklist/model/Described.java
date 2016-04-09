package rs.os.checklist.model;

/**
 * Common object very often used previewing items which has name, description, image...
 * @author zgavrilovic
 *
 */
public class Described extends Named {
	private static final long serialVersionUID = 399704930642628226L;
	
	public static final String PROP_DESCR = "description";
	public static final String PROP_IMAGE_NAME = "imageName";
	
	protected String description;
	protected String imageName;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}