package rs.os.contactbook.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import rs.os.contactbook.model.common.StringValueHolder;

@Entity
public class Website extends StringValueHolder {

	private static final long serialVersionUID = -5396555912461596395L;

	// constants
	public static String PROPERTY_TYPE = "type";
	
	public enum Type {
		Home, Work, HomePage, Ftp, Blog, Profile, Other
	};

	private Type type;

	public Website() {
		label = "Website";
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
		return "Website [type=" + type + "]";
	}

}