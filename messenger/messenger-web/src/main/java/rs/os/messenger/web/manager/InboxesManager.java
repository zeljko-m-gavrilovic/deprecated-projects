package rs.os.messenger.web.manager;

import java.util.ArrayList;
import java.util.List;

import rs.os.messenger.common.domain.Message;

public class InboxesManager {

    private List<Inbox> inboxes;

    public InboxesManager() {
        inboxes = new ArrayList<Inbox>();
    }

    public Message pullOldestMessage(String inboxId) {
        Inbox destination = findDestination(inboxId);
        if (destination == null) {
            return null;
        } else {
            return destination.pullOldestMessage();
        }
    }

    public void addMessage(Message message) {
        String to = message.getTo();
        Inbox destination = findDestination(to);
        if (destination == null) {
            destination = new Inbox(to);
            inboxes.add(destination);
        }
        destination.addMessage(message);
    }

    private Inbox findDestination(String inboxId) {
        for (Inbox destination : inboxes) {
            if ((destination.getId().equalsIgnoreCase(inboxId))) {
                return destination;
            }
        }
        return null;
    }

    public int getNumberOfInboxes() {
        return inboxes.size();
    }
}