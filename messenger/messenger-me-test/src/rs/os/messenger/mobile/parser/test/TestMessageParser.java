package rs.os.messenger.mobile.parser.test;

import java.io.IOException;
import java.util.Date;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;

import org.xmlpull.v1.XmlPullParserException;

import rs.os.messenger.common.domain.Message;

public class TestMessageParser extends TestCase {

	private Message testMessage;

	public TestMessageParser() {
		super(2, "Test Message parsing");
	}

	public void setUp() {
		testMessage = new Message();
		testMessage.setId(new Long(1));
		testMessage.setFrom("+381641924244");
		testMessage.setTo("+381111222");
		testMessage.setSubject("kasnim 10min");
		testMessage.setContent("Ljudi kasniću 10ak minuta");
		testMessage.setTime(new Date());
		testMessage.setType("text");
	}

	public void testToString() throws AssertionFailedException, IOException,
			XmlPullParserException {
		String messageStr = Message.pack(testMessage);
		assertNotNull(messageStr);
		assertTrue(messageStr.length() > 0);
	}

	public void testToObject() throws AssertionFailedException, IOException,
			XmlPullParserException {
		String messageStr = Message.pack(testMessage);
		Message parsedMessage = Message.unpack(messageStr);
		assertEquals(testMessage.getId().longValue(), parsedMessage.getId()
				.longValue());
		assertEquals(testMessage.getFrom(), parsedMessage.getFrom());
		assertEquals(testMessage.getTo(), parsedMessage.getTo());
		assertEquals(testMessage.getTime(), parsedMessage.getTime());
		assertEquals(testMessage.getSubject(), parsedMessage.getSubject());
		assertEquals(testMessage.getContent(), parsedMessage.getContent());
		assertEquals(testMessage.getType(), parsedMessage.getType());
	}

	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testToObject();
			break;
		case 1:
			testToString();
			break;
		default:
			break;
		}
	}
}