package rs.esolutions.ecomm.android.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.domain.RegistrationData;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

/**
 * Read and write properties from properties file
 * Data are stored as key=value pairs
 *
 * @author Gavrilovici
 *
 */
public class PersistentPropertiesService {

	public static final String APPLICATION_URL = "applicationUrl";
	private final String PROPERTIES_FILE_NAME = "/ecomm_properties.txt";
	private String filePath;
	
	public PersistentPropertiesService() {
		filePath = Environment.getExternalStorageDirectory() + PROPERTIES_FILE_NAME;
	}
	
	public Map<String, String> readPersistentProperties() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		File propertiesFile = createFileIfNotExist();
		Properties prop = new Properties();  
        InputStream is = new FileInputStream(propertiesFile);
		prop.load(is);  
        map.put(RegistrationData.PROPERTY_UPLOAD_KEY, prop.getProperty(RegistrationData.PROPERTY_UPLOAD_KEY));
        map.put(APPLICATION_URL, prop.getProperty(APPLICATION_URL));
        is.close();
        return map;
	}

	public void writePersitentProperties(Map<String, String> map) throws IOException {
		File propertiesFile = createFileIfNotExist();
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(propertiesFile)));
		for(String key : map.keySet()) {
			String line = key + "=" + map.get(key); 
			pw.println(line);
		}
		pw.flush();
		pw.close();
	}
	
	public void showNoPersistentPropertes(Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage(context.getString(R.string.no_properties_file))
				.setCancelable(false)
				.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
					});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	private File createFileIfNotExist() throws IOException {
		File propertiesFile = new File(filePath);
		if(!propertiesFile.exists()) {
			propertiesFile.createNewFile();
		}
		return propertiesFile;
	}
}