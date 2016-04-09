/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.commands;

import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionEvent;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.mobile.screens.EditForm;

/**
 *
 * @author Zeljko
 */
public class ForwardCommand extends Command {

    private Command parent;
    private Message messageToForward;

    public ForwardCommand(Command parent, Message messageToForward) {
        super("Forward");
        this.parent = parent;
        this.messageToForward = messageToForward;
    }

    public void actionPerformed(ActionEvent evt) {
        EditForm editForm = EditForm.getInstance(parent);
        editForm.forward(messageToForward);
    }
}
