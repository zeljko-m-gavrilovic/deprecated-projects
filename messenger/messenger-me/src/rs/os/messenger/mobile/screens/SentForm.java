/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.screens;

import com.sun.lwuit.Command;
import rs.os.messenger.mobile.dao.MessagesDao;

/**
 *
 * @author zgavrilovic
 */
public class SentForm extends MessagesForm {

    private static final MessagesForm singleton = new SentForm();

    public static MessagesForm getInstance(Command command) {
        singleton.addCommand(command);
        return singleton;
    }

    private SentForm() {
        super("Sent", new MessagesDao("Sent"));
    }
}