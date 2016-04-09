package rs.os.messenger.common.data;

import java.util.Date;

import rs.os.messenger.common.domain.Message;

public class SampleMessageData {

    private SampleMessageData() {
    }

    public static Message getMessageKasnim() {
        Message message = new Message();
        message.setId(new Long(1));
        message.setFrom("+381631047751");
        message.setTo("+381641924244");
        message.setSubject("kasnim 10min");
        message.setContent("Ljudi kasnim 10ak minuta");
        message.setTime(new Date());
        message.setType("text");
        return message;
    }

    public static Message getMessageBanka() {
        Message message = new Message();
        message.setId(new Long(2));
        message.setFrom("+381631047751");
        message.setTo("+381641924244");
        message.setSubject("stigla plata");
        message.setContent("Uplaceno 10000e na vas racun");
        message.setTime(new Date());
        message.setType("text");
        return message;
    }

    public static Message getMessageTelekom() {
        Message message = new Message();
        message.setId(new Long(3));
        message.setFrom("+381631047751");
        message.setTo("+381641924244");
        message.setSubject("uplata kredita");
        message.setContent("Vas kredit je uvecan za 500 RSD");
        message.setTime(new Date());
        message.setType("text");
        return message;
    }

    public static Message getMessageZurka() {
        Message message = new Message();
        message.setId(new Long(4));
        message.setFrom("+381641924244");
        message.setTo("+381631047751");
        message.setSubject("zurka u 19");
        message.setContent("veceras zurka kod mene");
        message.setTime(new Date());
        message.setType("text");
        return message;
    }

    public static Message getMessageRodjendan() {
        Message message = new Message();
        message.setId(new Long(5));
        message.setFrom("+381641924244");
        message.setTo("+381111222");
        message.setSubject("srecan rodjendan");
        message.setContent("Srecan rodjendan covek");
        message.setTime(new Date());
        message.setType("text");
        return message;
    }

    public static Message[] getSampleMessagesInbox() {
        Message[] inbox = new Message[3];
        inbox[0] = getMessageKasnim();
        inbox[1] = getMessageBanka();
        inbox[2] = getMessageTelekom();
        return inbox;
    }

    public static Message[] getSampleMessagesSent() {
        Message[] sent = new Message[2];
        sent[0] = getMessageZurka();
        sent[1] = getMessageRodjendan();
        return sent;
    }
}
