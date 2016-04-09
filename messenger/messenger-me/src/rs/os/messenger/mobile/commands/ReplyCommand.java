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
public class ReplyCommand extends Command {

    private Command parent;
    private Message messageToReply;

    public ReplyCommand(Command parent, Message messageToReply) {
        super("Reply");
        this.parent = parent;
        this.messageToReply = messageToReply;
    }

    public void actionPerformed(ActionEvent evt) {
        EditForm editForm = EditForm.getInstance(parent);
        editForm.reply(messageToReply);
    }
}