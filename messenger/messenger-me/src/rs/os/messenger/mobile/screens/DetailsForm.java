package rs.os.messenger.mobile.screens;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.common.util.DateFormatter;
import rs.os.messenger.mobile.commands.DeleteCommand;
import rs.os.messenger.mobile.commands.ForwardCommand;
import rs.os.messenger.mobile.commands.ReplyCommand;

public class DetailsForm extends Form {

    public static final String REPLY = "Reply";
    private static final String DETAILS_FORM_TITLE = "Message details";
    private Command thisCommand;
    private Message message;
    private Label fromLabel;
    private Label toLabel;
    private Label subjectLabel;
    private Label contentLabel;
    private Label timeLabel;
    private TextField fromField;
    private TextField toField;
    private TextField subjectField;
    private TextField contentField;
    private TextField timeField;
    private static DetailsForm singleton = new DetailsForm();

    public static DetailsForm getInstance(Command backCommand, Message message) {
        singleton.message = message;
        singleton.toScreen();
        singleton.removeAllCommands();
        singleton.addCommand(backCommand);
        singleton.handleCommands(backCommand);
        return singleton;
    }

    private DetailsForm() {
        super(DETAILS_FORM_TITLE);

        initScreen();

        this.message = new Message();

        toScreen();
    }

//    public void show(Message message) {
//        this.message = message;
//        toScreen();
//        show();
//    }

    private void initScreen() {
        fromLabel = new Label("from");
        toLabel = new Label("to");
        subjectLabel = new Label("subject");
        timeLabel = new Label("time");
        contentLabel = new Label("content");

        fromField = new TextField();
        toField = new TextField();
        subjectField = new TextField();
        contentField = new TextField();
        timeField = new TextField();

        BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        addComponent(fromLabel);
        addComponent(fromField);
        addComponent(toLabel);
        addComponent(toField);
        addComponent(subjectLabel);
        addComponent(subjectField);
        addComponent(timeLabel);
        addComponent(timeField);
        addComponent(contentLabel);
        addComponent(contentField);

    }

    private void handleCommands(Command backCommand) {
        addCommand(backCommand);
        
        thisCommand = new Command(DETAILS_FORM_TITLE) {

            public void actionPerformed(ActionEvent evt) {
                show();
            }
        };
        
        ReplyCommand replyCommand = new ReplyCommand(thisCommand, message);
        addCommand(replyCommand);

        ForwardCommand forwardCommand = new ForwardCommand(thisCommand, message);
        addCommand(forwardCommand);

        DeleteCommand deleteCommand = new DeleteCommand(backCommand, message, null);
        addCommand(deleteCommand);
    }

    private void toScreen() {
        fromField.setText(message.getFrom());
        toField.setText(message.getTo());
        subjectField.setText(message.getSubject());
        DateFormatter dateFormater = new DateFormatter(message.getTime());
        timeField.setText(dateFormater.formatDateTime());
        contentField.setText(message.getContent());
    }
}