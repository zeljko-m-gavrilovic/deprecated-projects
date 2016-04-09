package rs.os.messenger.mobile.main;

import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.xmlpull.v1.XmlPullParserException;

import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.common.domain.Preferences;
import rs.os.messenger.common.encode.UrlJMeEncoder;
import rs.os.messenger.mobile.dao.MessagesDao;
import rs.os.messenger.mobile.dao.PreferencesDao;
import rs.os.messenger.mobile.http.HttpReceiver;
import rs.os.messenger.mobile.listener.MessageChangesListener;
import rs.os.messenger.mobile.screens.MainForm;
import rs.os.messenger.mobile.screens.NotifyDialog;

import com.sun.lwuit.Command;
import com.sun.lwuit.Display;

public class MessengerMidlet extends MIDlet {

	private Command exitCommand = new Command("Exit");
	private MainForm mainScreen;

	protected void startApp() throws MIDletStateChangeException {
		Display.init(this);
		try {

			final MessagesDao inboxDao = new MessagesDao("Inbox");
			inboxDao.addMessage(SampleMessageData.getMessageBanka());
			inboxDao.addMessage(SampleMessageData.getMessageKasnim());

			MessagesDao sentDao = new MessagesDao("Sent");
			sentDao.addMessage(SampleMessageData.getMessageKasnim());

			MessagesDao failedDao = new MessagesDao("Failed");
			failedDao.addMessage(SampleMessageData.getMessageTelekom());

			PreferencesDao preferencesDao = new PreferencesDao();
			if (preferencesDao.isEmpty()) {
				preferencesDao.updatePreferences(Preferences
						.getDefaultPreferences());
			}

			Preferences preferences = preferencesDao.getPreferences();
			String receiveUrl = preferences.getServletUrl() + "?inbox="
					+ UrlJMeEncoder.urlEncode(preferences.getId());

			mainScreen = MainForm.getInstance(this);
			mainScreen.show();

			// startReceivingMessages(receiveUrl, inboxDao);

		} catch (Exception e) {
			NotifyDialog.notifyInfo(
					"Exception occured starting Messenger midlet ", e
							.getMessage());
		}
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}

	protected void pauseApp() {
	}

	private void startReceivingMessages(String receiveUrl,
			final MessagesDao inboxDao) {
		HttpReceiver httpReceiver = new HttpReceiver(receiveUrl);
		httpReceiver.addInboxChangesListener(new MessageChangesListener() {

			public void onNewMessage(String newMessage) {
				try {
					Message message = Message.unpack(newMessage);
					inboxDao.addMessage(message);
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (XmlPullParserException ex) {
					ex.printStackTrace();
				} catch (RecordStoreException ex) {
					ex.printStackTrace();
				}
			}

			public void onMessageReceivingFailed(String errorMessage,
					int responseCode) {
			}

			public void onUnexpectedException(String errorMessage, Exception ex) {
			}
		});

		httpReceiver.startReceivingMessagesInNewThread(10000);
	}
}
