package rs.esolutions.ecomm.android.service;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import rs.esolutions.ecomm.android.domain.HttpResponseData;
import rs.esolutions.ecomm.android.domain.RegistrationData;
import rs.esolutions.ecomm.android.domain.UploadData;
import android.util.Log;

/**
 * HttpPostService post data to server. Supports multipart/form-data and 
 * application/x-www-form-urlencoded post type;
 * 
 * @author Gavrilovici
 *
 */
public class HttpPostService {
	
	private static final String UTF_8 = "UTF-8";
	private final String tag = this.getClass().getSimpleName();
	private final String newLine = "\r\n";
	private String boundary = "*****************************************";
	
	public HttpPostService() {
	}

	public HttpResponseData postUploadData(String sUrl, UploadData uploadData, String imsi, String uploadKey) {
		HttpResponseData result = null;
		try {
			URL url = new URL(sUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			con.setRequestProperty("Cookie", "imsi=" + URLEncoder.encode(imsi) + ";" 
											+ " uploadKey=" + URLEncoder.encode(uploadKey) + ";");
			
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			
			writeMultipartBinary(uploadData.getContent(), dos);
			writeMultipartProperty(dos, UploadData.PROPERTY_LATITUDE, uploadData.getLatitude());
			writeMultipartProperty(dos, UploadData.PROPERTY_LONGITUDE, uploadData.getLongitude());
			writeMultipartProperty(dos, UploadData.PROPERTY_COMMENT, uploadData.getComment());
			writeMultipartProperty(dos, UploadData.PROPERTY_CITY, uploadData.getCity());
			writeEndOfMultipart(dos);
			
			dos.flush();
			dos.close();
			
			return new HttpResponseData(con);
		} catch (Exception e) {
			Log.e(tag, "Exception uploading data to server " + e.toString() + " using url" + sUrl, e);
			result = new HttpResponseData(sUrl);
			result.setException(e);
		}
		return result;
	}
	
	public HttpResponseData postRegistration(String sUrl, RegistrationData registrationData) {
		HttpResponseData result = null;
		try {
			URL url = new URL(sUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			String encName = URLEncoder.encode(registrationData.getName(), UTF_8);
			String encSurname = URLEncoder.encode(registrationData.getSurname(), UTF_8);
			String encPhoneNumber = URLEncoder.encode(registrationData.getPhoneNumber(), UTF_8);
			String encImsi = URLEncoder.encode(registrationData.getImsi(), UTF_8);
			String p = String.format("name=%s&surname=%s&phoneNumber=%s&imsi=%s", 
					encName, encSurname, encPhoneNumber, encImsi);
			dos.write(p.getBytes(UTF_8));
			dos.flush();
			dos.close();
			
            return new HttpResponseData(con);
		} catch (Exception e) {
			Log.e(tag, "Exception registering user to server " + e.toString() + " using url" + sUrl, e);
			result = new HttpResponseData(sUrl);
        	result.setException(e);
		}
		return result;
	}
	
	private void writeMultipartProperty(DataOutputStream dos, String name, String value) throws IOException {
		if(value != null) {
			String encodedName = URLEncoder.encode(name, UTF_8);
			String encodedValue = URLEncoder.encode(value, UTF_8);
			dos.writeBytes(newLine + "--" + boundary);
			dos.writeBytes(newLine +  "Content-Disposition: form-data; name=\"" + encodedName + "\"");
			dos.writeBytes(newLine);
			dos.writeBytes(newLine + encodedValue);
		}
	}
	
	private void writeMultipartBinary(byte[] content, DataOutputStream dos) throws IOException {
		if(content != null && (content.length > 0)) {
			dos.writeBytes(newLine + "--" + boundary);
			dos.writeBytes(newLine + "Content-Disposition: form-data; name=\"photo\";filename=\"photo.jpeg\"");
			dos.writeBytes(newLine + "Content-Type: image/jpeg");
			dos.writeBytes(newLine + "Content-Transfer-Encoding: binary");
			dos.writeBytes(newLine);
			dos.writeBytes(newLine);
			
			dos.write(content);
		}
	}
	
	private void writeEndOfMultipart(DataOutputStream dos) throws IOException {
		dos.writeBytes(newLine + "--" + boundary + "--");
	}
}