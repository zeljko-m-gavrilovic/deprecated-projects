package rs.os.messenger.mobile.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import java.io.UnsupportedEncodingException;
import javax.microedition.io.HttpConnection;
import rs.os.messenger.mobile.util.HttpConnectionTemplate;

public class HttpSender {

    private String servletUrl;

    public HttpSender(String servletUrl) {
        this.servletUrl = servletUrl;
    }

    public void sendMessageInNewThread(final String encodedMessage) {
        new Thread() {

            public void run() {
                try {
                    sendMessage(encodedMessage);
                } catch (Exception ex) {
                    onUnexpectedException("Unexpected exception occured in thred. Sending mesage is terminated", ex);
                }
            }
        }.start();
    }

    public void sendMessage(final String encodedMessage) throws IOException {

        new HttpConnectionTemplate() {

            public void setParameters(HttpConnection httpConnection) throws IOException {
                httpConnection.setRequestMethod(HttpConnection.POST);
                httpConnection.setRequestProperty("Content-Type", "application/octet-stream");
                httpConnection.setRequestProperty("Content-Length", String.valueOf(encodedMessage.length()));
                httpConnection.setRequestProperty("User-Agent",
                        "Profile/MIDP-2.0 Configuration/CLDC-1.1");
            }

            public void writeToStream(OutputStreamWriter os) throws IOException {
                super.writeToStream(os);
                os.write(encodedMessage);
                os.flush();
            }

            public void onOkResponseCode(InputStream is) throws UnsupportedEncodingException, IOException {
               HttpSender.this.onMessageSendingSucced(encodedMessage);
            }

            public void onBadResponseCode(int responseCode) {
                HttpSender.this.onMessageSendingFailed("Bad response sending message", responseCode);
            }

            public void onUnexpectedException(Exception ex) {
                HttpSender.this.onUnexpectedException("Unexpected error while sending message", ex);
            }

        }.execute(servletUrl);

    }

    protected void onMessageSendingSucced(String messageStr) {
    }

    protected void onMessageSendingFailed(String messageStr, int responseCode) {
    }

    protected void onUnexpectedException(String errorMessage, Exception ex) {
    }
}
