package rs.os.messenger.web.servlet.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.web.util.HttpSendReceiveUtil;

public class TestServlet {

	private static final String SERVLET_URL = "http://localhost:8081/messenger-web/app/message/";
	private URL urlSendMessage;
	private URL urlReceiveMessage;
	private Message testMessage;

	@Before
	public void setUp() throws Exception {
		testMessage = SampleMessageData.getMessageBanka();
		urlSendMessage = new URL(SERVLET_URL);
		String UTF_8 = "UTF-8";
		String encodedInboxId = URLEncoder.encode(testMessage.getTo(), UTF_8);
		urlReceiveMessage = new URL(SERVLET_URL + '/' + encodedInboxId);
	}

	@Test
	public void testSendReceiveMessage() throws InterruptedException,
			TransformerException, ParserConfigurationException, SAXException,
			IOException {
		String testXmlMessage = Message.formEncode(testMessage);
		HttpSendReceiveUtil.doPost(urlSendMessage, testXmlMessage);
		Thread.sleep(5000);

		String receivedXmlMessage = HttpSendReceiveUtil
				.doGet(urlReceiveMessage);
		Assert.assertNotNull(receivedXmlMessage);
		Assert.assertTrue(receivedXmlMessage.length() > 0);

		Message receivedMessage = Message.unpack(receivedXmlMessage);
		Assert.assertEquals(testMessage, receivedMessage);

	}
}