package rs.os.contactbook.manager;

import rs.os.contactbook.model.address.Website;
import rs.os.core.factory.ObjectFactory;

public class WebsiteFactory extends ObjectFactory<Website> {

	protected String[] sites = { "www.youngculture.com", "www.google.com",
			"ftp.youngculture.com", "www.blic.rs" };

	@Override
	public Website createObject() {
		Website result = new Website();
		result.setType(Website.Type.Home);
		result.setValue((String) getRandomValue(sites));
		return result;
	}
}