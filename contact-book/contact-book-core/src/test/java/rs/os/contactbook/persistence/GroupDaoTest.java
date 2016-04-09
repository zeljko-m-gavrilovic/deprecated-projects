package rs.os.contactbook.persistence;

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
import rs.os.contactbook.model.contact.Group;
import rs.os.core.manager.db.LazyDbManagerIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/contact-book-core-context.xml",
		"/services-os-core-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class GroupDaoTest {

	@Autowired
	@Qualifier("lazyDbManager")
	private LazyDbManagerIF genericDbManager;

	@Autowired
	private PersonFactory personFactory;

	private Group group;

	@Before
	public void setUp() {
		group = new Group();
		group.addMember(genericDbManager.merge(personFactory.createObject()));
		group.addMember(genericDbManager.merge(personFactory.createObject()));
		group.addMember(genericDbManager.merge(personFactory.createObject()));
	}

	@Test
	public void testSaveGroup() {
		Assert.assertNotNull(group);
		Assert.assertFalse(group.persistent());
		Assert.assertEquals(3, group.getMembers().size());
		
		group = genericDbManager.merge(group);
		
		Assert.assertNotNull(group);
		Assert.assertTrue(group.persistent());
		Assert.assertEquals(3, group.getMembers().size());
	}
}