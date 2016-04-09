package rs.os.messenger.web.manager;

import java.util.LinkedList;
import java.util.Queue;

import rs.os.messenger.common.domain.Message;

public class Inbox {

    private Queue<Message> queue;
    private String id;

    public Inbox(String inboxId) {
        this.id = inboxId;
        queue = new LinkedList<Message>();
    }

    public String getId() {
        return id;
    }

    public void addMessage(Message message) {
        boolean correctInbox = message.getTo().equalsIgnoreCase(id);
        if (correctInbox) {
            queue.add(message);
        } else {
            throw new RuntimeException("Bad destination queue for message");
        }
    }

    public Message pullOldestMessage() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Inbox other = (Inbox) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
        
}