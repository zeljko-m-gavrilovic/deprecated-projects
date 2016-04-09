package rs.esolutions.ecomm.android.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * One place utility class for functionalities like reading input stream... used on few places in project
 * 
 * @author Gavrilovici
 *
 */
public class StreamUtils {
	
	private final String newLine = "\r\n";
	
	public String readContentAsString(InputStream is) throws IOException {
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
		String content = "";
		String line;
		while ((line = buffReader.readLine()) != null) {
			content += line + newLine;
		}
		is.close();
		buffReader.close();
		return content;
	}
	
	public byte[] readContentAsBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		byte[] content = out.toByteArray();
		out.close();
		return content;
	}
	
}