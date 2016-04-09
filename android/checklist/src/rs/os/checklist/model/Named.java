package rs.os.checklist.model;

public class Named extends PersistentObject implements Comparable<Named> {
	private static final long serialVersionUID = -6028334328383777135L;
	public static final String PROP_ID = "id";
	public static final String PROP_NAME = "name";
	
	protected String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Named other) {
		if(name != null) {
			return name.compareTo(other.getName());
		} else {
			return 0; 
		}

	}
	
}