package rs.os.contactbook.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Celebration extends StringValueHolder {

	private static final long serialVersionUID = 1350420125329470774L;

	// constants
	public static String PROPERTY_TYPE = "type";
	
	public enum Type {
		Slava, Anniversary, Other
	};
	private Type type;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		Type oldValue = getType();
		this.type = type;
		
	}

	@Override
	public String toString() {
		return "Celebration [type=" + type + "]";
	}

}
