package rs.os.contactbook.manager;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rs.os.contactbook.manager.PersonFactory;
import rs.os.contactbook.model.contact.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/contact-book-core-context.xml" })
public class PersonFactoryTest {

	@Autowired
	private PersonFactory personFactory;

	@Test
	public void testCreatePerson() {
		Person person = personFactory.createObject();
		Assert.assertNotNull(person);
	}

	@Test
	public void testCreatePersons() {
		List<Person> persons = personFactory.createObjects(20);
		Assert.assertNotNull(persons);
		Assert.assertEquals(20, persons.size());
		/*for(Person person : persons) {
			System.out.println(person.toString());
		}*/
	}
	
}