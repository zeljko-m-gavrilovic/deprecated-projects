/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.alltests;

import rs.os.messenger.mobile.parser.test.*;
import rs.os.messenger.mobile.dao.test.TestPreferencesDao;
import rs.os.messenger.mobile.dao.test.TestInboxDao;
import jmunit.framework.cldc11.TestSuite;

/**
 *
 * @author zgavrilovic
 */
public class MessengerTestSuit extends TestSuite {

    public MessengerTestSuit() {
        super("All Messenger Tests");
        add(new TestMessageParser());
        add(new TestInboxDao());
        add(new TestPreferencesDao());
    }
}
