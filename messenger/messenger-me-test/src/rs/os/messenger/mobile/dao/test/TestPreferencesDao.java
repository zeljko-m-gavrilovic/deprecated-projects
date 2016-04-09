/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.dao.test;

import java.io.IOException;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;
import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import org.xmlpull.v1.XmlPullParserException;
import rs.os.messenger.common.domain.Preferences;
import rs.os.messenger.mobile.dao.PreferencesDao;

/**
 *
 * @author zgavrilovic
 */
public class TestPreferencesDao extends TestCase {

    private PreferencesDao preferencesDao;

    public TestPreferencesDao() {
        super(2, "TestPreferencesDao");
    }

    public void setUp() {
        try {
            PreferencesDao.deleteStore();
            preferencesDao = new PreferencesDao();

        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        super.tearDown();
        try {
            preferencesDao.close();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }



    protected void testLoadingFromEmptyRms() throws AssertionFailedException, MIDletStateChangeException, IOException, XmlPullParserException, RecordStoreException {
        Preferences storedPreferences = preferencesDao.getPreferences();
        assertNull(storedPreferences);
    }

    protected void testUpdating() throws AssertionFailedException, MIDletStateChangeException, IOException, XmlPullParserException, RecordStoreException {
        Preferences emptyPreferences = preferencesDao.getPreferences();
        assertNull(emptyPreferences);
        
        Preferences testPreferences = Preferences.getDefaultPreferences();
        preferencesDao.updatePreferences(testPreferences);

        Preferences storedPreferences = preferencesDao.getPreferences();
        assertNotNull(storedPreferences);

        assertEquals(testPreferences, storedPreferences);
    }

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0:
                testLoadingFromEmptyRms();
                break;
            case 1:
                testUpdating();
                break;
            default:
                break;
        }
    }
}
