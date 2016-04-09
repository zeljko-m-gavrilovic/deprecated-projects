/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.commands;

import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionEvent;
import java.io.IOException;
import javax.microedition.rms.RecordStoreException;
import org.xmlpull.v1.XmlPullParserException;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.mobile.dao.MessagesDao;

/**
 *
 * @author Zeljko
 */
public class DeleteCommand extends Command {

    private Command parent;
    private Message message;
    private MessagesDao messagesDao;
    
    public DeleteCommand(Command parent, Message message, MessagesDao messagesDao) {
        super("Delete");
        this.parent = parent;
        this.message = message;
        this.messagesDao = messagesDao;
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            messagesDao.deleteMessage(message);
            parent.actionPerformed(null);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}