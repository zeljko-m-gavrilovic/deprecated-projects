/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.os.messenger.mobile.listener;

/**
 *
 * @author zgavrilovic
 */
public interface MessageChangesListener {

    public void onNewMessage(String newMessage);

    public void onMessageReceivingFailed(String errorMessage, int responseCode);

    public void onUnexpectedException(String errorMessage, Exception ex);
}