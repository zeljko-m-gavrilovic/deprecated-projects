package rs.os.messenger.mobile.dao;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

import org.xmlpull.v1.XmlPullParserException;

import rs.os.messenger.common.domain.Message;

public class MessagesDao {

	private RecordStore recordStore;
	private static final int MESSAGES_NOT_FOUND = -1;

	public MessagesDao(String recordStoreName) {
		try {
			recordStore = RecordStore.openRecordStore(recordStoreName, true);
		} catch (RecordStoreException ex) {
			ex.printStackTrace();
			throw new RuntimeException("can open messages record store "
					+ recordStoreName);
		}
	}

	public int addMessage(Message message) throws IOException,
			XmlPullParserException, RecordStoreException {
		byte[] rowMessage = Message.toBytes(message);
		return recordStore.addRecord(rowMessage, 0, rowMessage.length);
	}

	public void updateMessage(Message message) throws IOException,
			XmlPullParserException, RecordStoreException {
		int recordId = findRecordIdByMessage(message);
		byte[] updatedRecord = Message.toBytes(message);
		recordStore.setRecord(recordId, updatedRecord, 0, updatedRecord.length);
	}

	public void deleteMessage(Message message) throws RecordStoreException,
			XmlPullParserException, IOException {
		int recordId = findRecordIdByMessage(message);
		recordStore.deleteRecord(recordId);
	}

	public static void deleteStore(String recordStoreName)
			throws RecordStoreException {
		try {
			RecordStore.deleteRecordStore(recordStoreName);
		} catch (RecordStoreNotFoundException rsnfe) {
			System.out.println("There is no store " + recordStoreName
					+ " so store can't be deleted. Ignoring this exception ");
		}
	}

	public Vector getAll(RecordComparator recordComparator)
			throws RecordStoreException, XmlPullParserException, IOException {
		RecordEnumeration records = recordStore.enumerateRecords(null,
				recordComparator, false);
		Vector messages = new Vector(records.numRecords());
		while (records.hasNextElement()) {
			Message parsedMessage = Message.toMessage(records.nextRecord());
			messages.addElement(parsedMessage);
		}
		return messages;
	}

	public void addListener(RecordListener recordListener) {
		recordStore.addRecordListener(recordListener);
	}

	private int findRecordIdByMessage(Message message)
			throws RecordStoreException, XmlPullParserException, IOException {
		RecordEnumeration records = null;
		records = recordStore.enumerateRecords(null, null, false);
		while (records.hasNextElement()) {
			int recordId = records.nextRecordId();
			byte[] byteMessage = recordStore.getRecord(recordId);
			Message storedMessage = Message.toMessage(byteMessage);
			if (storedMessage.equals(message)) {
				return recordId;
			}
		}
		return MESSAGES_NOT_FOUND;
	}
}
