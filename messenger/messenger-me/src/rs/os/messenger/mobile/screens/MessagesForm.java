package rs.os.messenger.mobile.screens;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;

import rs.os.messenger.common.domain.Message;
import rs.os.messenger.common.util.DateFormatter;
import rs.os.messenger.mobile.dao.DateDescComparator;
import rs.os.messenger.mobile.dao.MessagesDao;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.DefaultListModel;

public class MessagesForm extends Form implements RecordListener {

	private Command thisCommand;
	protected Vector messages;
	protected List listMessages;
	protected DetailsForm messageDetailsScreen;
	protected MessagesDao messagesDao;

	public MessagesForm(String title, MessagesDao messagesDao) {
		super(title);
		this.messagesDao = messagesDao;
		thisCommand = new Command(getTitle()) {

			public void actionPerformed(ActionEvent evt) {
				show();
			}
		};

		messages = new Vector();
		loadMessages();

		initList();

		addComponent(listMessages);
	}

	private void initList() {

		listMessages = new List(messages);
		listMessages.setListCellRenderer(new DefaultListCellRenderer() {

			public Component getListCellRendererComponent(List list,
					Object value, int index, boolean isSelected) {
				try {
					Image image = Image.createImage("/newMessage.png");
					String messageLabel = formatItem((Message) value);
					setText(messageLabel);
					setIcon(image);
					getStyle().setBgTransparency(100);
					return this;
				} catch (IOException ex) {
					ex.printStackTrace();
					return this;
				}
			}

			public Component getListFocusComponent(List list) {
				Label label = new Label("");
				label.getStyle().setBgColor(25);
				label.getStyle().setBgTransparency(100);
				return label;
			}
		});

		listMessages.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = listMessages.getSelectedIndex();
				// System.out.println("selected index is " + selectedIndex);
				Message message = (Message) listMessages.getModel().getItemAt(
						selectedIndex);
				// System.out.println("message is " + message);
				messageDetailsScreen = DetailsForm.getInstance(thisCommand,
						message);
				messageDetailsScreen.show();
			}
		});

	}

	public void loadMessages() {
		try {
			messages = messagesDao.getAll(new DateDescComparator());
		} catch (Exception e) {
			NotifyDialog.notifyError(getTitle(),
					"Exception in screen Messages, loading messages failed");
			e.printStackTrace();
		}

	}

	private String formatItem(Message message) {
		String subject = message.getSubject();
		DateFormatter dateFormatter = new DateFormatter(message.getTime());

		String messageLabel = subject + " " + dateFormatter.formatDateTime();
		return messageLabel;
	}

	public void recordAdded(RecordStore recordStore, int recordId) {
		try {
			byte[] byteMessage = recordStore.getRecord(recordId);
			Message message = Message.toMessage(byteMessage);
			((DefaultListModel) listMessages.getModel()).addItemAtIndex(
					message, 0);
			NotifyDialog.notifyInfo(getTitle(),
					"new message in list... refreshing the list");
		} catch (Exception ex) {
			NotifyDialog
					.notifyError(getTitle(),
							"Messages screen, exception in listener method recordAdded");
			ex.printStackTrace();
		}

	}

	public void recordChanged(RecordStore recordStore, int recordId) {
	}

	public void recordDeleted(RecordStore recordStore, int recordId) {
		try {
			byte[] byteMessage = recordStore.getRecord(recordId);
			Message message = Message.toMessage(byteMessage);
			int index = findIndexOfMessage(message);
			if (index != -1) {
				listMessages.getModel().removeItem(index);
			}
			NotifyDialog.notifyError(getTitle(),
					"deleted message in list... refreshing the list");
		} catch (Exception ex) {
			NotifyDialog
					.notifyError(getTitle(),
							"Messages screen, exception in listener method recordDeleted");
			ex.printStackTrace();
		}
	}

	private int findIndexOfMessage(Message objToBeFind) {
		DefaultListModel listModel = ((DefaultListModel) listMessages
				.getModel());
		for (int i = 0; i < listModel.getSize(); i++) {
			Message message = (Message) listModel.getItemAt(i);
			if (message.equals(objToBeFind)) {
				return i;
			}
		}
		return -1;
	}
}
