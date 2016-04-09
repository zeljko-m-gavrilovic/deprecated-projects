package rs.os.contactbook.manager;

import rs.os.contactbook.model.address.Email;
import rs.os.core.factory.ObjectFactory;

public class EmailFactory extends ObjectFactory<Email> {

	protected String[] emails = { "xyz.abc@gmail.com", "def.jkl@yahoo.com",
			"opq.cde@youngculture.com", "efg.hij@lmn.com" };

	@Override
	public Email createObject() {
		Email result = new Email();
		result.setType(Email.Type.Home);
		result.setValue((String) getRandomValue(emails));
		return result;
	}
}