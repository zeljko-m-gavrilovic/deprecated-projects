package rs.os.contactbook.manager;

import org.springframework.stereotype.Service;

import rs.os.contactbook.model.contact.Organization;
import rs.os.core.factory.ObjectFactory;

@Service("organizationFactory")
public class OrganizationFactory extends ObjectFactory<Organization> {

	protected String[] names = { "aritex", "gami", "fleshkomerc" };
	protected String[] pibs = { "82389117474-8238238", "77238239-32",
			"7327347-2462" };

	@Override
	public Organization createObject() {
		Organization organization = new Organization();
		organization.setName((String) getRandomValue(names));
		organization.setPib((String) getRandomValue(pibs));
		return organization;
	}
}
