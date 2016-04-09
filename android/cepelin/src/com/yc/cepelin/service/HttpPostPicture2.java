package com.yc.cepelin.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpPostPicture2 {

	public static HttpData post(String sUrl, int venueId, String contentType, InputStream stream) {
		HttpData ret = new HttpData();
		try {
			String boundary = "*****************************************";
			String newLine = "\r\n";
			int bytesAvailable;
			int bufferSize;
			int maxBufferSize = 4096;
			int bytesRead;
			URL url = new URL(sUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.addRequestProperty("venueId", String.valueOf(venueId));
			con.addRequestProperty("contentType", contentType);
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
				
			dos.writeBytes("--" + boundary + newLine);
			dos.writeBytes("Content-Disposition: form-data; "
					+ "name=\"image\";filename=\"image.jpeg\"" + newLine + newLine);
			
			bytesAvailable = stream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
			bytesRead = stream.read(buffer, 0, bufferSize);
			while (bytesRead> 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = stream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = stream.read(buffer, 0, bufferSize);
			}
			dos.writeBytes(newLine);
			dos.writeBytes("--" + boundary + "--" + newLine);
			stream.close();
		
			dos.flush();
			BufferedReader rd = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				ret.content += line + "\r\n";
			}			
			dos.close();
			rd.close();
		} catch (MalformedURLException me) {
		} catch (IOException ie) {
		} catch (Exception e) {
			Log.e("HREQ", "Exception: "+e.toString());
		}
		return ret;
	}
}
