package rs.os.messenger.web.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpSendReceiveUtil class handles GET and POST to some URL
 * 
 * @author zgavrilovic
 * 
 */
public class HttpSendReceiveUtil {

	/**
	 * Discourage the initialization
	 */
	private HttpSendReceiveUtil() {
	}

	/**
	 * Make POST request and return response
	 * 
	 * @param url
	 *            to which we want to make POST request
	 * @param encodedMessage
	 *            is encoded message (in "application/x-www-form-urlencoded"
	 *            format) which will be set to request body
	 * @return raw response of server from URL
	 * @throws IOException
	 *             if internal input stream cannot be closed
	 */
	public static String doPost(URL url, String encodedMessage)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(encodedMessage
				.length()));

		OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),
				"UTF-8");
		os.write(encodedMessage);
		os.flush();
		// TODO check this close statement
		os.close();
		return readResponseAndClose(conn);
	}

	/**
	 * Make GET request and return response
	 * 
	 * @param url
	 *            to which we want to make GET request
	 * @return raw response of server from URL
	 * @throws IOException
	 *             if internal input stream cannot be closed
	 */
	public static String doGet(URL encodedUrl) throws IOException {
		String result = null;
		HttpURLConnection conn = (HttpURLConnection) encodedUrl
				.openConnection();
		conn.setRequestMethod("GET");

		int responseCode = conn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return readResponseAndClose(conn);
		}
		return result;
	}

	/**
	 * 
	 * @param conn
	 *            is connection to URL
	 * @return raw response of server from URL
	 * @throws IOException
	 *             if internal input stream cannot be closed
	 */
	private static String readResponseAndClose(HttpURLConnection conn)
			throws IOException {
		int responseCode = conn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStreamReader reader = new InputStreamReader(conn
					.getInputStream(), "UTF-8");
			StringBuffer response = new StringBuffer();
			int read = 0;
			while ((read = reader.read()) >= 0) {
				response.append((char) read);
			}
			// close resources
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException isExc) {
					throw new RuntimeException(
							"Failed to close input stream..." + isExc);
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
			return response.toString();
		} else {
			throw new RuntimeException(
					"Exception sending message to server. Server response code is "
							+ responseCode);
		}
	}
}