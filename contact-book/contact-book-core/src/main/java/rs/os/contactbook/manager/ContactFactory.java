package rs.os.contactbook.manager;

import org.springframework.stereotype.Service;

import rs.os.contactbook.model.contact.Contact;
import rs.os.core.factory.ObjectFactory;

@Service("contactFactory")
public class ContactFactory extends ObjectFactory<Contact> {

	protected String[] names = { "google", "linuxquestions", "gami",
			"salondragana" };

	@Override
	public Contact createObject() {
		Contact contact = new Contact();
		contact.setName((String) getRandomValue(names));
		contact.setAddresses(new AddressFactory().createObjects(3));
		contact.setPhones(new PhoneFactory().createObjects(3));
		contact.setEmails(new EmailFactory().createObjects(2));
		contact.setWebsites(new WebsiteFactory().createObjects(4));
		contact.setCelebrations(new CelebrationFactory().createObjects(6));
		return contact;
	}

}