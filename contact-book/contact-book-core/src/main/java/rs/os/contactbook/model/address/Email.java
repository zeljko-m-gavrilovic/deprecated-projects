package rs.os.contactbook.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import rs.os.contactbook.model.common.StringValueHolder;

@Entity
public class Email extends StringValueHolder {
	private static final long serialVersionUID = -2514441035651396556L;
	
	// constants
	public static String PROPERTY_TYPE = "type";
	
	public enum Type {
		Home, Work, Other
	};
	private Type type;
	
	public Email() {
		label = "Email";
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		Type oldValue = getType();
		this.type = type;
		propertyChangeSupport.firePropertyChange(PROPERTY_TYPE, oldValue,
				type);
	}

	@Override
	public String toString() {
		return "Email [type=" + type + "]";
	}
	
}