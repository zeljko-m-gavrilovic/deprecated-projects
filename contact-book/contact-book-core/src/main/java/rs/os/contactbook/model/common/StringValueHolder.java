package rs.os.contactbook.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import rs.os.core.model.common.Persistable;

@MappedSuperclass
public abstract class StringValueHolder extends Persistable {
	private static final long serialVersionUID = -1588454178090323974L;

	// constants
	public static String PROPERTY_LABEL = "label";
	public static String PROPERTY_VALUE = "value";
	
	protected String label;
	private String value;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		String oldValue = getLabel();
		this.label = label;
		propertyChangeSupport.firePropertyChange(PROPERTY_LABEL, oldValue,
				label);
	}

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		String oldValue = getValue();
		this.value = value;
		propertyChangeSupport.firePropertyChange(PROPERTY_VALUE, oldValue,
				value);
	}
	
}