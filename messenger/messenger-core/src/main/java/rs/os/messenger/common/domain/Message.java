package rs.os.messenger.common.domain;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class Message {

	private static final String UTF_8 = "UTF-8";

	private Long id = new Long(-1);
	private String from = "";
	private String to = "";
	private Date time = new Date();
	private String content = "";
	private String subject = "";
	private String type = "";

	public Message() {
	}

	public static String formEncode(Message message)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String id = String.valueOf(message.getId());
		sb.append("id=" + URLEncoder.encode(id, UTF_8)).append('&');
		String time = String.valueOf(message.getTime().getTime());
		sb.append("time=" + URLEncoder.encode(time, UTF_8)).append('&');
		sb.append("from=" + URLEncoder.encode(message.getFrom(), UTF_8))
				.append('&');
		sb.append("to=" + URLEncoder.encode(message.getTo(), UTF_8))
				.append('&');
		sb.append("subject=" + URLEncoder.encode(message.getSubject(), UTF_8))
				.append('&');
		sb.append("content=" + URLEncoder.encode(message.getContent(), UTF_8))
				.append('&');
		sb.append("type=" + URLEncoder.encode(message.getType(), UTF_8));
		return sb.toString();
	}

	public static String pack(Message message)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String id = String.valueOf(message.getId());
		sb.append("id=").append(id).append(',');
		String time = String.valueOf(message.getTime().getTime());
		sb.append("time=").append(time).append(',');
		sb.append("from=").append(message.getFrom()).append(',');
		sb.append("to=").append(message.getTo()).append(',');
		sb.append("subject=").append(message.getSubject()).append(',');
		sb.append("content=").append(message.getContent()).append(',');
		sb.append("type=").append(message.getType());
		return sb.toString();
	}

	public static Message unpack(String messageStr)
			throws UnsupportedEncodingException {

		String[] properties = messageStr.split(",");
		String id = properties[0].split("=")[1];
		String time = properties[1].split("=")[1];
		String from = properties[2].split("=")[1];
		String to = properties[3].split("=")[1];
		String subject = properties[4].split("=")[1];
		String content = properties[5].split("=")[1];
		String type = properties[6].split("=")[1];

		Message message = new Message();
		message.setId(Long.valueOf(id));
		message.setTime(new Date(new Long(time).longValue()));
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setContent(content);
		message.setType(type);

		return message;
	}

	public static Message toMessage(byte[] byteMessage) throws IOException {
		String messageStr = null;
		try {
			messageStr = new String(byteMessage, UTF_8);
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		return Message.unpack(messageStr);
	}

	public static byte[] toBytes(Message message) throws IOException {
		String messageStr = null;
		messageStr = Message.pack(message);
		try {
			return messageStr.getBytes(UTF_8);
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		return null;
	}

	public String toString() {
		return "Message [content=" + content + ", from=" + from + ", id=" + id
				+ ", subject=" + subject + ", time=" + time.getTime() + ", to="
				+ to + ", type=" + type + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}