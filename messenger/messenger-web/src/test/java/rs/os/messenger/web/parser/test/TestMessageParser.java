package rs.os.messenger.web.parser.test;

import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.common.domain.Message;

public class TestMessageParser {

	private Message testMessage;

	@Before
	public void setUp() {
		testMessage = SampleMessageData.getMessageKasnim();
	}

	@Test
	public void testToXml() throws TransformerException,
			UnsupportedEncodingException {
		String parsedMessage = Message.formEncode(testMessage);

		Assert.assertNotNull(parsedMessage);
		Assert.assertTrue(parsedMessage.length() > 0);
	}

	/*
	 * public void testToObject() throws TransformerException,
	 * ParserConfigurationException, SAXException, IOException { String
	 * xmlMessage = MessageParser.toXml(testMessage); Message parsedMessage =
	 * MessageParser.toObject(xmlMessage);
	 * 
	 * assertEquals(testMessage.getId(), parsedMessage.getId());
	 * assertEquals(testMessage.getFrom(), parsedMessage.getFrom());
	 * assertEquals(testMessage.getTo(), parsedMessage.getTo());
	 * assertEquals(testMessage.getSubject(), parsedMessage.getSubject());
	 * assertEquals(testMessage.getContent(), parsedMessage.getContent());
	 * assertEquals(testMessage.getTime(), parsedMessage.getTime());
	 * assertEquals(testMessage.getType(), parsedMessage.getType()); }
	 */

}
