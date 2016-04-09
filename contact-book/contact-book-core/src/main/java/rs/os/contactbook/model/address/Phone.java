package rs.os.contactbook.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import rs.os.contactbook.model.common.StringValueHolder;

@Entity
public class Phone extends StringValueHolder {

	private static final long serialVersionUID = 2881637165705839057L;

	// constants
	public static String PROPERTY_TYPE = "type";
	
	public enum Type {
		Home, Work, Mobile, HomeFax, WorkFax, Pager, Other
	};

	private Type type;

	public Phone() {
		label = "Phone";
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
		return "Phone [type=" + type + "]";
	}

}