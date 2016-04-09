package rs.os.contactbook.persistence;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import rs.os.contactbook.manager.PersonFactory;
import rs.os.contactbook.model.contact.Person;
import rs.os.core.manager.db.LazyDbManagerIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/contact-book-core-context.xml",
		"/services-os-core-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class PersonDaoTest {

	@Autowired
	@Qualifier("lazyDbManager")
	private LazyDbManagerIF lazyDbManager;

	@Autowired
	private PersonFactory personFactory;

	private Person person;

	@Before
	public void setUp() {
		person = personFactory.createObject();
		Assert.assertNotNull(person);
		Assert.assertFalse(person.persistent());
	}

	@Test
	public void testSavePerson() {
		person = lazyDbManager.merge(person);
		Assert.assertNotNull(person);
		Assert.assertTrue(person.persistent());
	}

	@Test
	public void testUpdatePerson() {
		// persist person
		person = lazyDbManager.merge(person);
		Assert.assertNotNull(person);
		Assert.assertTrue(person.persistent());
		
		// take already persisted person and update it
		Person personForUpdate = lazyDbManager.get(Person.class, person.getId());
		String testComment = "should be updated";
		personForUpdate.setComment(testComment);
		personForUpdate = lazyDbManager.merge(personForUpdate);
		
		// load updated user and check if it is updated
		Person updatedPerson = lazyDbManager.get(Person.class, person.getId());
		Assert.assertEquals(testComment, updatedPerson.getComment());

	}

	@Test
	public void testSavePersons() {
		List<Person> list = personFactory.createObjects(2);
		lazyDbManager.merge(list);
	}

}