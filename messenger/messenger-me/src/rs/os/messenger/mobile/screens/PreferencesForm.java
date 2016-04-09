package rs.os.messenger.mobile.screens;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;
import javax.microedition.rms.RecordStoreException;

import rs.os.messenger.common.domain.Preferences;
import rs.os.messenger.mobile.dao.PreferencesDao;

public final class PreferencesForm extends Form {

    public static final String ID = "id";
    public static final String PORT = "port";
    public static final String SAVE = "Save";
    public static final String SERVER = "server";
    public static final String SERVLET = "servlet";
    private static final String PREFERENCES_FORM_TITLE = "Preferences";

    private Command saveCommand;
    private Label idLabel;
    private Label serverLabel;
    private Label portLabel;
    private Label servletLabel;
    private TextField idField;
    private TextField serverField;
    private TextField portField;
    private TextField servletField;
    private Preferences preferences;
    private PreferencesDao preferencesDao;
    private static PreferencesForm singleton = new PreferencesForm();

    public static PreferencesForm getInstance(Command backCommand) {
        singleton.removeAllCommands();
        singleton.addCommand(backCommand);
        singleton.initCommands();
        return singleton;
    }

    private PreferencesForm() {
        super(PREFERENCES_FORM_TITLE);
        initPreferences();
        initScreen();
        initCommands();
    }

    private void initCommands() {
        saveCommand = new Command(SAVE) {

            public void actionPerformed(ActionEvent evt) {
                updateSettings();
            }
        };
        addCommand(saveCommand);
    }

    private void initPreferences() {
        try {
            preferencesDao = new PreferencesDao();
            preferences = preferencesDao.getPreferences();
        } catch (RecordStoreException re) {
            NotifyDialog.notifyError("Preferences Form", "settings screen, settings rms failed to load settings");
        }
    }

    private void initScreen() {
        BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        idLabel = new Label(ID);
        serverLabel = new Label(SERVER);
        portLabel = new Label(PORT);
        servletLabel = new Label(SERVLET);
        idField = new TextField();
        serverField = new TextField();
        portField = new TextField();
        servletField = new TextField();

        addComponent(idLabel);
        addComponent(idField);
        addComponent(serverLabel);
        addComponent(serverField);
        addComponent(portLabel);
        addComponent(portField);
        addComponent(servletLabel);
        addComponent(servletField);
    }

    private void toObject() {
        preferences.setId(idField.getText());
        preferences.setServer(serverField.getText());
        preferences.setPort(portField.getText());
        preferences.setServlet(servletField.getText());
    }

    private void updateSettings() {
        try {
            toObject();
            preferencesDao.updatePreferences(preferences);
            getCommand(1).actionPerformed(null);
        } catch (Exception e) {
            NotifyDialog.notifyError("Preferences Form", "settings screen, update settings failed");
        }
    }
}
