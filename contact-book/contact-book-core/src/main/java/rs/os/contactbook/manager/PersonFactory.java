package rs.os.contactbook.manager;

import org.springframework.stereotype.Service;

import rs.os.contactbook.model.contact.Person;
import rs.os.core.factory.ObjectFactory;
import rs.os.core.util.DateUtil;

@Service("personFactory")
public class PersonFactory extends ObjectFactory<Person> {

	protected String[] lastNameList;
	protected String[] maleNameList;
	protected String[] femaleNameList;
	protected String[] jmbgList;
	protected String[] dateOfBirthList;

	private AddressFactory addressFactory;
	private PhoneFactory phoneFactory;
	private WebsiteFactory websiteFactory;
	private EmailFactory emailFactory;

	public PersonFactory() {
		loadProperties();

		addressFactory = new AddressFactory();
		phoneFactory = new PhoneFactory();
		websiteFactory = new WebsiteFactory();
		emailFactory = new EmailFactory();
	}

	protected void loadProperties() {
		lastNameList = new String[] { "Ivanovic", "Jovanovic", "Milanovic" };
		maleNameList = new String[] { "Zeljko", "Ivan", "Nebojsa" };
		femaleNameList = new String[] { "Dragana", "Milica, Jovana" };
		jmbgList = new String[] { "1410981790046", "2134234324543",
				"1324324432443" };
		dateOfBirthList = new String[] { "23.01.1980", "14.10.1981",
				"25.12.1973" };
	}

	@Override
	public Person createObject() {
		Person person = new Person();

		// randomly select gender (title and first name depend on the gender)
		boolean randomGenderMale = randomNumbersGenerator.nextBoolean();
		String firstName = null;
		String middleName = null;
		String title = null;
		if (randomGenderMale) {
			firstName = (String) getRandomValue(maleNameList);
			title = "Mr.";
			middleName = (String) getRandomValue(femaleNameList);
		} else {
			firstName = (String) getRandomValue(femaleNameList);
			title = "Mrs.";
			middleName = (String) getRandomValue(maleNameList);
		}

		// set other properties randomly
		person.setFirstName(firstName);
		person.setTitle(title);
		person.setMiddleName(middleName);
		person.setLastName((String) getRandomValue(lastNameList));
		person.setJmbg((String) getRandomValue(jmbgList));
		String dateOfBirthStr = (String) getRandomValue(dateOfBirthList);
		person.setDateOfBirth(DateUtil.convertStringToDate(dateOfBirthStr,
				DateUtil.DEFAULT_DATE_PATTERN));

		// create collections
		person.setAddresses(addressFactory.createObjects(2));
		person.setPhones(phoneFactory.createObjects(3));
		person.setWebsites(websiteFactory.createObjects(1));
		person.setEmails(emailFactory.createObjects(2));
		return person;
	}

}