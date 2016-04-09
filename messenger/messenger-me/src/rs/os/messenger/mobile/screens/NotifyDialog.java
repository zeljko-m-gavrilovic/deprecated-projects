/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.screens;

import com.sun.lwuit.Dialog;

/**
 *
 * @author zgavrilovic
 */
public final class NotifyDialog {

    private NotifyDialog() {
    }
    
    private static final long DEFAULT_FIMEOUT = 5000;

    public static void notifyAlarm(String title, String messageStr) {
        Dialog.show(title, messageStr, null, Dialog.TYPE_ALARM, null, DEFAULT_FIMEOUT);
    }

    public static void notifyInfo(String title, String messageStr) {
        Dialog.show(title, messageStr, null, Dialog.TYPE_INFO, null, DEFAULT_FIMEOUT);
    }

    public static void notifyWarning(String title, String messageStr) {
        Dialog.show(title, messageStr, null, Dialog.TYPE_WARNING, null, DEFAULT_FIMEOUT);
    }

    public static void notifyError(String title, String messageStr) {
        Dialog.show(title, messageStr, null, Dialog.TYPE_ERROR, null, DEFAULT_FIMEOUT);
    }

    public static void notifyConfirmation(String title, String messageStr) {
        Dialog.show(title, messageStr, Dialog.TYPE_CONFIRMATION, null,"OK", "Cancel");
    }
}