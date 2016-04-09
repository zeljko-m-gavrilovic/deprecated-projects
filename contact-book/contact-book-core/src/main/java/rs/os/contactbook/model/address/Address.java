package rs.os.contactbook.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import rs.os.core.model.common.Persistable;

@Entity
public class Address extends Persistable {

	private static final long serialVersionUID = 1350420125329470774L;

	// constants
	public static String PROPERTY_STATE = "state";
	public static String PROPERTY_CITY = "city";
	public static String PROPERTY_STREET = "street";
	public static String PROPERTY_ZIP_CODE = "zipCode";
	public static String PROPERTY_TYPE = "type";

	// properties
	private String state;
	private String city;
	private String street;
	private String zipCode;

	private Type type;

	public enum Type {
		Home, Work, Other
	};

	@NotNull
	@NotEmpty
	@Size(min=1)
	@Column(nullable = false)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		String oldValue = getState();
		this.state = state;
		propertyChangeSupport.firePropertyChange(PROPERTY_STATE, oldValue,
				state);
	}

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		String oldValue = getCity();
		this.city = city;
		propertyChangeSupport.firePropertyChange(PROPERTY_CITY, oldValue, city);
	}

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		String oldValue = getStreet();
		this.street = street;
		propertyChangeSupport.firePropertyChange(PROPERTY_STREET, oldValue,
				street);
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		String oldValue = getZipCode();
		this.zipCode = zipCode;
		propertyChangeSupport.firePropertyChange(PROPERTY_ZIP_CODE, oldValue,
				zipCode);
	}

	/*@Valid
	@NotNull
	@NotEmpty*/
	@Enumerated(EnumType.STRING)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		Type oldValue = getType();
		this.type = type;
		propertyChangeSupport.firePropertyChange(PROPERTY_TYPE, oldValue, type);
	}

	@Override
	public String toString() {
		return "Address [city=" + city + ",  state=" + state + ", street="
				+ street + ", type=" + type + ", zipCode=" + zipCode + "]";
	}

}