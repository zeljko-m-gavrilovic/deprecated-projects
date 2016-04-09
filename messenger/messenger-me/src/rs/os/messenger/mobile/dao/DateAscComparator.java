package rs.os.messenger.mobile.dao;

import javax.microedition.rms.RecordComparator;

import rs.os.messenger.common.domain.Message;

public class DateAscComparator implements RecordComparator {

	public int compare(byte[] abyteLeft, byte[] abyteRight) {
		Message left = null;
		Message right = null;
		try {
			left = Message.toMessage(abyteLeft);
			right = Message.toMessage(abyteRight);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (left.getTime().getTime() < right.getTime().getTime()) {
			return RecordComparator.PRECEDES;
		}
		if (left.getTime().getTime() == right.getTime().getTime()) {
			return RecordComparator.EQUIVALENT;
		}
		if (left.getTime().getTime() > right.getTime().getTime()) {
			return RecordComparator.FOLLOWS;
		}
		return -1;
	}
}
