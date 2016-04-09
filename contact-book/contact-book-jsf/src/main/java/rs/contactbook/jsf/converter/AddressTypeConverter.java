package rs.contactbook.jsf.converter;

import javax.faces.convert.EnumConverter;

import rs.os.contactbook.core.model.address.Address;

public class AddressTypeConverter extends EnumConverter {

	public AddressTypeConverter() {
		super(Address.Type.class);
	}
}
