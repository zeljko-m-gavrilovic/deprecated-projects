/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rs.os.messenger.mobile.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author zgavrilovic
 */
public class HttpConnectionTemplate {

    private final int END_OF_STREAM = -1;

    public HttpConnectionTemplate() {
    }

    private HttpConnection createConnection(String servletUrl) throws IOException {
        return (HttpConnection) Connector.open(servletUrl);
    }

    public void setParameters(HttpConnection httpConnection) throws IOException {
        httpConnection.setRequestMethod(HttpConnection.GET);
    }

    public void writeToStream(OutputStreamWriter os) throws IOException {
    }

    private int getResponseCode(HttpConnection httpConnection) {
        try {
            int responseCode = httpConnection.getResponseCode();
            return responseCode;
        } catch (IOException ex) {
            onfailedToGetResponseCode(ex);
            return -1;
        }
    }

    private void receiveResponse(int responseCode, HttpConnection httpConnection) throws IOException {
        if (responseCode == HttpConnection.HTTP_OK) {
            onOkResponseCode(httpConnection.openInputStream());
        } else {
            onBadResponseCode(responseCode);
        }

    }

    public void onOkResponseCode(InputStream is) throws UnsupportedEncodingException, IOException {
        InputStreamReader inReader = new InputStreamReader(is, "UTF-8");
        int read = 0;
        StringBuffer sb = new StringBuffer();
        while ((read = inReader.read()) != END_OF_STREAM) {
            sb.append((char) read);
        }

        String response = sb.toString();
        if (response.length() > 0) {
            onMessageInResponse(response);
        } else {
            onEmptyResponse();
        }
        //closeInputStream(is);
        closeInputStream(inReader);
    }

    public void onBadResponseCode(int responseCode) {
        System.out.println("Badsponse code from server!");
    }

    public void onMessageInResponse(String response) {
    }

    public void onEmptyResponse() {
    }

    private void closeInputStream(InputStreamReader is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception ex) {
                onfailedToCloseInputStream(ex);
            }
        }
    }

    private void closeOutputStream(OutputStreamWriter os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception ex) {
                onfailedToCloseOutputStream(ex);
            }
        }
    }

    private void closeConnection(HttpConnection httpConnection) {
        if (httpConnection != null) {
            try {
                httpConnection.close();
            } catch (Exception ex) {
                onfailedToCloseConnection(ex);
            }
        }
    }

    public void onfailedToCloseInputStream(Exception ex) {
        System.out.println("Failed to close input stream!");
        onUnexpectedException(ex);
    }

    public void onfailedToCloseOutputStream(Exception ex) {
        System.out.println("Failed to close output stream!");
        onUnexpectedException(ex);
    }

    public void onfailedToCloseConnection(Exception ex) {
        System.out.println("Failed to close connection!");
        onUnexpectedException(ex);
    }

    public void onfailedToGetResponseCode(Exception ex) {
        System.out.println("Failed to get response code!");
        onUnexpectedException(ex);
    }

    public void onUnexpectedException(Exception ex) {
        System.out.println("Unexpected exception in HttpConnectionTemplate!" + ex);
        throw new RuntimeException(ex.getMessage());
    }

    public void execute(String servletUrl) throws IOException {
        executeInternal(servletUrl);
    }

    private void executeInternal(String servletUrl) throws IOException {
        HttpConnection httpConnection = createConnection(servletUrl);
        setParameters(httpConnection);
        OutputStreamWriter os = new OutputStreamWriter(httpConnection.openOutputStream(), "UTF-8");
        writeToStream(os);
        int responseCode = getResponseCode(httpConnection);
        receiveResponse(responseCode, httpConnection);
        closeOutputStream(os);
        closeConnection(httpConnection);
    }
}

 