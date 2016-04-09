package rs.os.messenger.mobile.screens;

import java.io.IOException;
import java.util.Date;

import javax.microedition.rms.RecordStoreException;

import rs.os.messenger.common.domain.Message;
import rs.os.messenger.common.domain.Preferences;
import rs.os.messenger.mobile.dao.MessagesDao;
import rs.os.messenger.mobile.dao.PreferencesDao;
import rs.os.messenger.mobile.http.HttpSender;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

public class EditForm extends Form {

	public static final String CONTENT = "content";
	public static final String SEND = "Send";
	public static final String SUBJECT = "subject";
	public static final String TO = "to";
	private static final String EDIT_FORM_TITLE = "Edit Message";
	private Command sendCommand;
	private TextField toField;
	private TextField subjectField;
	private TextField contentField;
	private HttpSender httpSender;
	private Message message;
	private Preferences preferences;
	private static EditForm singleton = new EditForm();

	public static EditForm getInstance(Command backCommand) {
		singleton.removeAllCommands();
		singleton.addCommand(backCommand);
		singleton.initCommands();
		return singleton;
	}

	private EditForm() {
		super(EDIT_FORM_TITLE);

		initPreferences();
		initScreen();
		initCommands();

		httpSender = new HttpSender(preferences.getServletUrl()) {

			protected void onMessageSendingFailed(String messageStr,
					int responseCode) {
				try {
					Message message = Message.unpack(messageStr);
					new MessagesDao("Faield").addMessage(message);
				} catch (Exception ex) {
					onUnexpectedException(
							"Error occured parsing received message...", ex);
				}
				NotifyDialog.notifyError("Sending message failed", messageStr);
			}

			protected void onMessageSendingSucced(String messageStr) {
				try {
					Message message = Message.unpack(messageStr);
					new MessagesDao("Inbox").addMessage(message);
				} catch (Exception ex) {
					onUnexpectedException(
							"Message sent but error occured while adding to rms...",
							ex);
				}
				NotifyDialog.notifyInfo("Sending message succeded", "");
			}

			protected void onUnexpectedException(String errorMessage,
					Exception ex) {
				NotifyDialog.notifyError(errorMessage, ex.getMessage());
			}
		};

		message = new Message();
		message.setFrom(preferences.getId());

		toScreen();
	}

	public void edit() {
		message = new Message();
		message.setFrom(preferences.getId());
		message.setTo("");
		message.setSubject("");
		message.setContent("");
		message.setType("text");
		message.setTime(new Date());

		setFocused(toField);

		toScreen();
		show();
	}

	public void reply(Message incomeMessage) {
		message = new Message();
		message.setFrom(preferences.getId());
		message.setTo(incomeMessage.getFrom());
		message.setSubject("reply on: " + incomeMessage.getSubject());
		message.setContent("");
		message.setType("text");
		message.setTime(new Date());

		setFocused(contentField);

		toScreen();
		show();
	}

	public void forward(Message incomeMessage) {
		message = new Message();
		message.setFrom(preferences.getId());
		message.setTo("");
		message.setSubject("forward on: " + incomeMessage.getSubject());
		message.setContent(incomeMessage.getContent());
		message.setType("text");
		message.setTime(new Date());

		setFocused(toField);

		toScreen();
		show();
	}

	private void initPreferences() {
		try {
			preferences = new PreferencesDao().getPreferences();
		} catch (RecordStoreException e) {
			NotifyDialog
					.notifyError(EDIT_FORM_TITLE,
							"new message screen failed to construct, preferences rms exception ");
		}
	}

	private void initScreen() {
		Label toLabel = new Label(TO);
		toField = new TextField();

		Label subjectLabel = new Label(SUBJECT);
		subjectField = new TextField();

		Label contentLabel = new Label(CONTENT);
		contentField = new TextField();

		BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		addComponent(toLabel);
		addComponent(toField);
		addComponent(subjectLabel);
		addComponent(subjectField);
		addComponent(contentLabel);
		addComponent(contentField);
	}

	private void initCommands() {
		sendCommand = new Command(SEND) {

			public void actionPerformed(ActionEvent evt) {
				sendMessage();
			}
		};
		addCommand(sendCommand);
	}

	private void toObject() {
		message.setFrom(preferences.getId());
		message.setTo(toField.getText());
		message.setSubject(subjectField.getText());
		message.setContent(contentField.getText());
		message.setTime(new Date());
		message.setType("text");
	}

	private void toScreen() {
		toField.setText(message.getTo());
		subjectField.setText(message.getSubject());
		contentField.setText(message.getContent());
	}

	private void sendMessage() {
		try {
			toObject();
			String encodedMessage = Message.formEncode(message);
			httpSender.sendMessageInNewThread(encodedMessage);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
