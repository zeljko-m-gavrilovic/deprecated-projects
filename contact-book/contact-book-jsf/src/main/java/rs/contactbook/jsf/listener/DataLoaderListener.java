package rs.contactbook.jsf.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import rs.os.contactbook.core.manager.ContactFactory;
import rs.os.contactbook.core.manager.ObjectFactory;
import rs.os.contactbook.core.manager.OrganizationFactory;
import rs.os.contactbook.core.manager.PersonFactory;
import rs.os.contactbook.core.model.contact.Contact;
import rs.os.contactbook.core.model.contact.Organization;
import rs.os.contactbook.core.model.contact.Person;

public class DataLoaderListener implements ServletContextListener {

	private List<Contact> contacts;
	private List<Person> persons;
	private List<Organization> organizations;
	private ObjectFactory<Contact> contactFactory;
	private ObjectFactory<Person> personFactory;
	private ObjectFactory<Organization> organizationFactory;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());

		// create factories
		contactFactory = (ContactFactory) ctx.getBean("contactFactory");
		personFactory = (PersonFactory) ctx.getBean("personFactoryProperties");
		organizationFactory = (OrganizationFactory) ctx
				.getBean("organizationFactory");

		// create simulation objects
		contacts = contactFactory.createObjects(2);
		persons = personFactory.createObjects(10);
		organizations = organizationFactory.createObjects(5);

		// store simulation objects in DB
		contacts = contactFactory.storeObjects(contacts);
		persons = personFactory.storeObjects(persons);
		organizations = organizationFactory.storeObjects(organizations);

		System.out.println("DataLoaderListener context initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// try to remove created data
		contactFactory.removeObjects(contacts);
		personFactory.removeObjects(persons);
		organizationFactory.removeObjects(organizations);
		System.out.println("DataLoaderListener context destroyed");
	}
}