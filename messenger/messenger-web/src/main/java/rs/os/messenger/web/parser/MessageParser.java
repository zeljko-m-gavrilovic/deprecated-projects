package rs.os.messenger.web.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.transform.TransformerException;

import rs.os.messenger.common.domain.Message;

public class MessageParser {

	private static final String UTF_8 = "UTF-8";

	private MessageParser() {
	}

	/*public static String toString(Message message) throws TransformerException,
			UnsupportedEncodingException {
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
		sb.append("content=" + URLEncoder.encode(message.getContent(), UTF_8));
		return sb.toString();
	}*/

	/*public static Message toObject(String xmlMessage)
			throws ParserConfigurationException, SAXException, IOException {

		return message;

	}*/

	/*public static Message toMessage(byte[] byteMessage)
			throws ParserConfigurationException, SAXException, IOException {
		String xmlMessage = null;
		try {
			xmlMessage = new String(byteMessage, UTF_8);
			return toObject(xmlMessage);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public static byte[] toBytes(Message message) throws TransformerException {
		String xmlMessage = toXml(message);
		try {
			return xmlMessage.getBytes(UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}*/

}