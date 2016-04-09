package rs.os.messenger.web.manager.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.web.manager.InboxesManager;

public class TestInboxesManager {

	private InboxesManager inboxesManager;

	@Before
	public void setUp() throws Exception {
		inboxesManager = new InboxesManager();
	}

	@Test
	public void testAddToDifferentInboxes() {
		inboxesManager.addMessage(SampleMessageData.getMessageTelekom());
		inboxesManager.addMessage(SampleMessageData.getMessageZurka());
		inboxesManager.addMessage(SampleMessageData.getMessageRodjendan());
		Assert.assertEquals(3, inboxesManager.getNumberOfInboxes());
	}

	@Test
	public void testAddTwoMessagesToTheSameInbox() {
		inboxesManager.addMessage(SampleMessageData.getMessageTelekom());
		inboxesManager.addMessage(SampleMessageData.getMessageTelekom());
		Assert.assertEquals(1, inboxesManager.getNumberOfInboxes());
	}

}