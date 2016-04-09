package rs.os.contactbook.manager;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("personFactoryUsingPropertiesFile")
public class PersonFactoryUsingPropertiesFile extends PersonFactory {

	private Properties personLoadDataProperties;

	@Autowired(required = true)
	public PersonFactoryUsingPropertiesFile(
			@Qualifier("personLoadDataProperties") Properties personLoadDataProperties) {
		this.personLoadDataProperties = personLoadDataProperties;
		loadPropertiesFromFile();
	}

	protected void loadPropertiesFromFile() {
		String lastNames = personLoadDataProperties.getProperty("lastName");
		String males = personLoadDataProperties.getProperty("males");
		String females = personLoadDataProperties.getProperty("females");
		String jmbg = personLoadDataProperties.getProperty("jmbg");
		String dateOfBirth = personLoadDataProperties
				.getProperty("dateOfBirth");

		lastNameList = lastNames.split(",");
		maleNameList = males.split(",");
		femaleNameList = females.split(",");
		jmbgList = jmbg.split(",");
		dateOfBirthList = dateOfBirth.split(",");
	}
}