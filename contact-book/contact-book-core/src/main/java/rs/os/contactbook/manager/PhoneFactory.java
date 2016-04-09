package rs.os.contactbook.manager;

import org.springframework.stereotype.Service;

import rs.os.contactbook.model.address.Phone;
import rs.os.core.factory.ObjectFactory;

@Service("phoneFactory")
public class PhoneFactory extends ObjectFactory<Phone> {

	protected String[] phoneNumbers = { "064 1924244", "011 3981926",
			"+381 63 625838", "+381 63 8885893" };

	@Override
	public Phone createObject() {
		Phone result = new Phone();
		result.setType(Phone.Type.Home);
		result.setValue((String) getRandomValue(phoneNumbers));
		return result;
	}
}