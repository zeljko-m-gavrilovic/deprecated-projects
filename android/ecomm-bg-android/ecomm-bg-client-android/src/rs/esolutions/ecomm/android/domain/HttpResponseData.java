package rs.esolutions.ecomm.android.domain;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.utility.StreamUtils;
import android.content.Context;

/**
 * Placeholder for HTTP response. It keeps info about response code, response content (header, body, cookies) 
 * 
 * @author Gavrilovici
 *
 */
public class HttpResponseData {
	private final int HTTP_RESPONSE_CODE_OK = 200;
	private final String NAME_VALUE_SEPARATOR = "=";
	private final String FIELD_SEPARATOR = ";";
	
	
	private String url;
	private String body;
	private int responseCode;
	private Map<String, String> cookies;
	private Exception exception;
	
	public HttpResponseData(String url) {
		this.url = url;
		cookies = new HashMap<String, String>();
	}
	
	/**
	 * Tries to read response from connection and holds result of the response or exception if any
	 * 
	 * @param con
	 */
	public HttpResponseData(HttpURLConnection con) {
		this(con.getURL().toString());
		try {
			responseCode = con.getResponseCode();
			if (responseCode == HTTP_RESPONSE_CODE_OK) {
				body = new StreamUtils().readContentAsString(con.getInputStream());
				exctractCookies(con);
            }
		} catch(Exception e) {
        	setException(e);
		}
		
	}

	/**
	 * Extracts cookies from response
	 * 
	 * @param con
	 */
	private void exctractCookies(HttpURLConnection con) {
		// take just set-cookie header fields, it can be more than one cookie...
		List<String> cookieList = con.getHeaderFields().get("set-cookie");
    	for (String c : cookieList) {
    		// first field in cookie is pair of name/value
    		String name = c.substring(0, c.indexOf(NAME_VALUE_SEPARATOR));
    		// take the value part
    		int semicolonIndex = c.indexOf(FIELD_SEPARATOR);
    		int endIndex = c.length();
    		if((semicolonIndex > -1) ) {
    			endIndex = semicolonIndex;
    		}
		    String value = c.substring(c.indexOf(NAME_VALUE_SEPARATOR) + 1, endIndex);
		    // trim apostrophe signs if they exist
		    if(value.startsWith("\"") && value.endsWith("\"")) {
		    	value = value.substring(1, value.length() - 1);
		    }
		    
		    // ignore the rest of the cookie fields (like version which is required or other optional fields...)
		    cookies.put(name, value);
		}
	}
	
	/**
	 * Returns body of the response if everything is OK or exception message if something gone wrong
	 * 
	 * @return
	 */
	public String getResult(Context context) {
		if(responseCode == HTTP_RESPONSE_CODE_OK) {
			int keyIdentifier = context.getResources().getIdentifier(getKeyFromBody(), 
					"string", "rs.esolutions.ecomm.android");
			try {
				return context.getString(keyIdentifier);
			} catch(Exception e) {
				return context.getString(R.string.exception_in_communication) + " " +e.getLocalizedMessage();
			}
		} else {
			if(exception != null) {
				String exceptionMessage = exception.getLocalizedMessage();
				// handle connection refused
				if((exceptionMessage != null) && exceptionMessage.endsWith("Connection refused")) {
					int indexOfPort = exceptionMessage.indexOf(":");
					if(indexOfPort > 0) {
						String serverAddress = exceptionMessage.substring(0, indexOfPort);
						return context.getText(R.string.server_not_working) + " " + serverAddress;
						
					}
				}
				// handle operation timed out
				if((exceptionMessage != null) && exceptionMessage.endsWith("The operation timed out")) {
					return (String) context.getText(R.string.server_not_working) + " " + getUrl() + ". " + exceptionMessage;
				}
				// return localized exception message
				return exceptionMessage;
			} else {
				// code -1 should never occured
				if(responseCode == -1) {
					context.getString(R.string.httpt_response_minus_one);
				}
				return context.getString(R.string.error_in_communication) + responseCode;
			}
		}
	}
	
	public boolean isResponseCodeOk() {
		return responseCode == HTTP_RESPONSE_CODE_OK;
	}
	
	public String getKeyFromBody() {
		int sIndex = body.indexOf("S#");
		int eIndex = body.indexOf("E#");
		String key = "";
		if(sIndex >= 0 && eIndex >= 0) {
			key = body.substring(sIndex + 2, eIndex);
		}
		//key = body.replace('\n', ' ').replace('\r', ' ').trim();
		return key;
	}
	
	public String getCookie(String cookieName) {
		return cookies.get(cookieName);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}