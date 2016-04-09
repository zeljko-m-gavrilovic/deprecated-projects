package rs.os.messenger.mobile.dao;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.xmlpull.v1.XmlPullParserException;

import rs.os.messenger.common.domain.Preferences;

public class PreferencesDao {

    private RecordStore recordStore;
    public static String recordStoreName = "Preferences";

    public PreferencesDao() throws RecordStoreException {
        recordStore = RecordStore.openRecordStore(recordStoreName, true);
    }

    public void close() throws RecordStoreException {
        recordStore.closeRecordStore();
    }

    private int findRecordId() throws RecordStoreException, InvalidRecordIDException {
        int numOfRecords = recordStore.getNumRecords();
        if (numOfRecords > 0) {
            return recordStore.enumerateRecords(null, null, true).nextRecordId();
        } else {
            return recordStore.getNextRecordID();
        }
    }

    public Preferences getPreferences() throws RecordStoreException {
        int numOfRecords = recordStore.getNumRecords();
        if (numOfRecords > 0) {
            byte[] bytes = recordStore.getRecord(findRecordId());
            return Preferences.toObject(bytes);
        } else {
            return null;
        }
    }

    public static void deleteStore() throws RecordStoreException {
        try {
            RecordStore.deleteRecordStore(recordStoreName);
        } catch (RecordStoreNotFoundException rsnfe) {
            System.out.println("Ignoring exception that record store was not found");
        }
    }

    public void updatePreferences(Preferences settings) throws IOException, XmlPullParserException,
            RecordStoreNotOpenException, RecordStoreException {
        byte[] bytesSettings = Preferences.toBytes(settings);
        int numOfRecords = recordStore.getNumRecords();
        if (numOfRecords > 0) {
            recordStore.setRecord(findRecordId(), bytesSettings, 0, bytesSettings.length);
        } else {
            recordStore.addRecord(bytesSettings, 0, bytesSettings.length);
        }
    }

    public boolean isEmpty() throws RecordStoreNotOpenException {
        return recordStore.getNumRecords() == 0;
    }
}