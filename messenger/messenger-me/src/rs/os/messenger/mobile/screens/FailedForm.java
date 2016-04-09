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
public class FailedForm extends MessagesForm {

    private static final MessagesForm singleton = new FailedForm();

    public static MessagesForm getInstance(Command command) {
        singleton.addCommand(command);
        return singleton;
    }

    private FailedForm() {
        super("Failed", new MessagesDao("Failed"));
    }
}