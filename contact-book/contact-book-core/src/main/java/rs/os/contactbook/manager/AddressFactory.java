package rs.os.contactbook.manager;

import org.springframework.stereotype.Service;

import rs.os.contactbook.model.address.Address;
import rs.os.core.factory.ObjectFactory;

@Service("addressFactory")
public class AddressFactory extends ObjectFactory<Address> {

	protected String[] states = { "serbia", "bulgaria", "romania", "hungary" };
	protected String[] cities = { "xyz city", "abc city", "uvw city",
			"opq city" };
	protected String[] streets = { "Vojvode Stepe 220",
			"bul. kralja Aleksandra 291", "Moravicka 20", "kralja Milana" };

	@Override
	public Address createObject() {
		Address result = new Address();
		result.setType(Address.Type.Home);
		result.setState((String) getRandomValue(states));
		result.setCity((String) getRandomValue(cities));
		result.setStreet((String) getRandomValue(streets));
		return result;
	}
}