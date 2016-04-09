package rs.os.messenger.mobile.http;


import java.io.IOException;
import java.util.Vector;

import rs.os.messenger.mobile.listener.MessageChangesListener;
import rs.os.messenger.mobile.util.HttpConnectionTemplate;

public class HttpReceiver {

    private boolean quit;
    private final String servletUrl;
    private Vector inboxChangesListeners;

    public HttpReceiver(String servletUrl) {
        this.servletUrl = servletUrl;
        this.inboxChangesListeners = new Vector();
    }

    public void startReceivingMessagesInNewThread(final int sleepTime) {
        new Thread() {

            public void run() {
                while (!quit) {
                    try {
                        receiveMessage();
                        Thread.sleep(sleepTime);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void receiveMessage() throws IOException {
         new HttpConnectionTemplate() {

            public void onMessageInResponse(String response) {
                notifyNewMessage(response);
            }

            public void onBadResponseCode(int responseCode) {
                notifyReceivingFailed("Bad response receiving message", responseCode);
            }

            public void onUnexpectedException(Exception ex) {
                notifyUnexpectedException("Unexpected error receiving message", ex);
            }


        }.execute(servletUrl);
    }

    public void stopReceivingMessages() {
        this.quit = true;
    }

    public void addInboxChangesListener(MessageChangesListener listener) {
        inboxChangesListeners.addElement(listener);
    }

    public void removeInboxChangesListener(MessageChangesListener toBeRemoved) {
        if (inboxChangesListeners.contains(toBeRemoved)) {
            inboxChangesListeners.removeElement(toBeRemoved);
        }
    }

    private void notifyNewMessage(String newMessage) {
        for (int i = 0; i < inboxChangesListeners.size(); i++) {
            MessageChangesListener listener = (MessageChangesListener) inboxChangesListeners.elementAt(i);
            if (listener != null) {
                listener.onNewMessage(newMessage);
            }
        }
    }

    private void notifyReceivingFailed(String errorMessage, int responseCode) {
        for (int i = 0; i < inboxChangesListeners.size(); i++) {
            MessageChangesListener listener = (MessageChangesListener) inboxChangesListeners.elementAt(i);
            if (listener != null) {
                listener.onMessageReceivingFailed(errorMessage, responseCode);
            }
        }
    }

    private void notifyUnexpectedException(String errorMessage, Exception ex) {
        for (int i = 0; i < inboxChangesListeners.size(); i++) {
            MessageChangesListener listener = (MessageChangesListener) inboxChangesListeners.elementAt(i);
            if (listener != null) {
                listener.onUnexpectedException(errorMessage, ex);
            }
        }
    }
}
