package rs.os.messenger.mobile.screens;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.IOException;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;

import javax.microedition.rms.RecordStoreNotOpenException;
import rs.os.messenger.mobile.dao.MessagesDao;
import rs.os.messenger.mobile.main.MessengerMidlet;

public class MainForm extends Form implements RecordListener {

    public static final String EXIT = "Exit";
    private static final String MAIN_SCREEN_TITLE = "Main screen";
    public final String NEW_MESSAGE = "New message";
    public final String INBOX = "Inbox";
    public final String SENT = "Sent";
    public final String FAILED = "Failed";
    public final String PREFERENCES = "Preferences";

    private Command exitCommand;
    private Command thisCommand;
    private MessengerMidlet midlet;
    private EditForm newMessageForm;
    private MessagesForm inboxForm;
    private MessagesForm sentForm;
    private MessagesForm failedForm;
    private PreferencesForm preferencesForm;

    private static MainForm singleton = new MainForm();

    private Image newMessageImage;
    private Image inboxImage;
    private Image sentImage;
    private Image failedImage;
    private Image preferencesImage;
    
    private Button newMessageButton;
    private Button inboxButton;
    private Button sentButton;
    private Button failedButton;
    private Button preferencesButton;

    public static MainForm getInstance(final MessengerMidlet midlet) {
        singleton.midlet = midlet;
        return singleton;
    }

    private MainForm() {
        super(MAIN_SCREEN_TITLE);
        initCommands();
        initImages();
        initScreen();
        createSubScreens();
    }

    private void initCommands() {
        exitCommand = new Command(EXIT, 1) {

            public void actionPerformed(ActionEvent evt) {
                midlet.notifyDestroyed();
            }
        };
        addCommand(exitCommand);
        thisCommand = new Command(MAIN_SCREEN_TITLE, 1) {

            public void actionPerformed(ActionEvent evt) {
                show();
            }
        };
    }

    private void initImages() {
        try {
        newMessageImage = Image.createImage("/newMessage.png");
        inboxImage = Image.createImage("/inbox.png");
        sentImage = Image.createImage("/sent.png");
        failedImage = Image.createImage("/error.png");
        preferencesImage = Image.createImage("/preferences.png");
        } catch(IOException ioe) {
            NotifyDialog.notifyError("Main screen", "Can't load images");
        }
    }

    private void initScreen() {
        BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        newMessageButton = new Button("New message", newMessageImage);
        newMessageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newMessageForm = EditForm.getInstance(thisCommand);
                newMessageForm.show();
            }
        });
        inboxButton = new Button("Inbox", inboxImage);
        inboxButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                formatInboxLabel(0);
                inboxForm.show();
            }
        });
        sentButton = new Button("Sent", sentImage);
        sentButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                formatSentLabel(0);
                sentForm.show();
            }
        });
        failedButton = new Button("Failed", failedImage);
        failedButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                formatFailedLabel(0);
                failedForm.show();
            }
        });
        preferencesButton = new Button("Preferences", preferencesImage);
        preferencesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                preferencesForm.show();
            }
        });
        addComponent(newMessageButton);
        addComponent(inboxButton);
        addComponent(sentButton);
        addComponent(failedButton);
        addComponent(preferencesButton);
    }

    private void createSubScreens() {

        MessagesDao inboxDao = new MessagesDao("Inbox");
        inboxForm = InboxForm.getInstance(thisCommand);
        inboxDao.addListener(inboxForm);
        inboxDao.addListener(this);

        MessagesDao sentDao = new MessagesDao("Sent");
        sentForm = SentForm.getInstance(thisCommand);
        sentDao.addListener(sentForm);
        sentDao.addListener(this);

        MessagesDao failedDao = new MessagesDao("Failed");
        failedForm = FailedForm.getInstance(thisCommand);
        failedDao.addListener(failedForm);
        failedDao.addListener(this);

        preferencesForm = PreferencesForm.getInstance(thisCommand);
    }

    private String formatLabelNumOfMessages(String label, int numOfNewMessages) {
        String resultedLabel = label;
        if (numOfNewMessages > 0) {
            resultedLabel = label + " (" + String.valueOf(numOfNewMessages) + ')';
        }
        return resultedLabel;
    }

    private void formatInboxLabel(int numOfNewMessages) {
        String newLabel = formatLabelNumOfMessages(INBOX, numOfNewMessages);
        inboxButton.setText(newLabel);
    }

    private void formatSentLabel(int numOfNewMessages) {
        String newLabel = formatLabelNumOfMessages(SENT, numOfNewMessages);
        sentButton.setText(newLabel);
    }

    private void formatFailedLabel(int numOfNewMessages) {
        String newLabel = formatLabelNumOfMessages(FAILED, numOfNewMessages);
        failedButton.setText(newLabel);
    }

    public void recordAdded(RecordStore recordStore, int recordId) {
        try {
            if (recordStore.getName().equals("Inbox")) {
                formatInboxLabel(1);
            }

            if (recordStore.getName().equals("Sent")) {
                formatSentLabel(1);
            }

            if (recordStore.getName().equals("Failed")) {
                formatFailedLabel(1);
            }
        } catch (RecordStoreNotOpenException ex) {
            NotifyDialog.notifyError("Main Form", "exception in listener method recordAdded");
            ex.printStackTrace();
        }
        // repaint();
        NotifyDialog.notifyError("Main Form", "new message(s) in list... refreshing the screen");

    }

    public void recordChanged(RecordStore recordStore, int recordId) {
    }

    public void recordDeleted(RecordStore recordStore, int recordId) {
    }
}