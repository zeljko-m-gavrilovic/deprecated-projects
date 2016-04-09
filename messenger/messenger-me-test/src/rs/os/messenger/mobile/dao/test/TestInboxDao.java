package rs.os.messenger.mobile.dao.test;

import java.io.IOException;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import org.xmlpull.v1.XmlPullParserException;
import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.mobile.dao.DateAscComparator;
import rs.os.messenger.mobile.dao.MessagesDao;

public class TestInboxDao extends TestCase {
    public static final String INBOX = "Inbox";

    private MessagesDao inboxRms;

    // private MessagesRms sent;
    public TestInboxDao() {
        super(1, "TestInboxDao");
    }

    public void setUp() {
        try {
            MessagesDao.deleteStore(INBOX);
            inboxRms = new MessagesDao(INBOX);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    protected void testAdding() throws AssertionFailedException, MIDletStateChangeException, IOException, XmlPullParserException, RecordStoreException {
        inboxRms.addMessage(SampleMessageData.getMessageBanka());
        inboxRms.addMessage(SampleMessageData.getMessageKasnim());
        inboxRms.addMessage(SampleMessageData.getMessageZurka());
        int numOfRecords = inboxRms.getAll(new DateAscComparator()).size();
        assertEquals(3, numOfRecords);
    }

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0:
                testAdding();
                break;
            default:
                break;
        }
    }
}