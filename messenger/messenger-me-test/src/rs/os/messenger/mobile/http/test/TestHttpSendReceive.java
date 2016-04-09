/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.http.test;

import java.io.IOException;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.common.domain.Preferences;
import rs.os.messenger.common.encode.UrlJMeEncoder;
import rs.os.messenger.mobile.http.HttpReceiver;
import rs.os.messenger.mobile.http.HttpSender;
import rs.os.messenger.mobile.listener.MessageChangesListener;

/**
 * 
 * @author zgavrilovic
 */
public class TestHttpSendReceive extends TestCase {

	private HttpSender httpSender;
	private HttpReceiver httpReceiver;
	private String encodedTestMessage;
	private String sendUrl;
	private String receiveUrl;
	private Preferences testPreferences;
	private boolean sendingSucced = false;
	private boolean receivingSucced = false;

	public void setUp() throws Throwable {
		super.setUp();

		Message testMessage = SampleMessageData.getMessageBanka();
		encodedTestMessage = Message.formEncode(testMessage);

		testPreferences = new Preferences();
		testPreferences.setId(testMessage.getTo());
		testPreferences.setServer("localhost");
		testPreferences.setPort("8081");
		testPreferences.setServlet("/messenger-web/message/"
				+ testPreferences.getId());

		sendUrl = testPreferences.getServletUrl();
		receiveUrl = testPreferences.getServletUrl() + "?inbox="
				+ UrlJMeEncoder.urlEncode(testPreferences.getId());

		httpSender = new HttpSender(sendUrl) {

			protected void onMessageSendingSucced(String messageStr) {
				sendingSucced = true;
			}

			protected void onMessageSendingFailed(String messageStr,
					int responseCode) {
				sendingSucced = false;
			}

		};

		httpReceiver = new HttpReceiver(receiveUrl);

		httpReceiver.addInboxChangesListener(new MessageChangesListener() {

			public void onNewMessage(String newMessage) {
				receivingSucced = true;
			}

			public void onMessageReceivingFailed(String errorMessage,
					int responseCode) {
				receivingSucced = false;
			}

			public void onUnexpectedException(String errorMessage, Exception ex) {
				receivingSucced = false;
			}
		});
	}

	public TestHttpSendReceive() {
		super(2, "TestHttpSendReceive");
	}

	public void tearDown() {
		super.tearDown();
	}

	public void testSucceedSendingMessage() throws AssertionFailedException,
			IOException {

		httpSender.sendMessage(encodedTestMessage);
		assertTrue(sendingSucced);

	}

	public void testSucceedReceivingMessage() throws IOException {
		httpSender.sendMessage(encodedTestMessage);
		assertTrue(sendingSucced);

		httpReceiver.receiveMessage();
		assertTrue(receivingSucced);
	}

	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testSucceedSendingMessage();
			break;
		case 1:
			testSucceedReceivingMessage();
			break;
		default:
			break;
		}
	}
}