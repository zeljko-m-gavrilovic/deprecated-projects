package rs.os.checklist.utility;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.os.checklist.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ImportExportUtility {
	private static final String ROOT_OF_EXPORTED_FILE = "/Android/data/rs.os.checklist/files/";
	public static final String EXPORTED_CHECKLISTS_FILE = "checklists";
	public static final String EXPORTED_CATEGORIES_FILE = "categories";
	public static int PICK_FILE_REQUEST = 1;
	
	private Context context;
	
	public ImportExportUtility(Context context) {
		this.context = context;
	}

	public String onActivityResult(int requestCode, int resultCode, Intent intent) {
		if ((requestCode == PICK_FILE_REQUEST) && (resultCode == Activity.RESULT_OK)) {
			Uri uri = intent.getData();
			if (uri != null) {
				String path = uri.toString();
				if (path.toLowerCase().startsWith("file://")) {
					File file = null;
					try {
						file = new File(new URI(uri.toString()));
					} catch (URISyntaxException e) {
						Log.e(getClass().getName(),"Cant create file frm uri " + uri.toString(), e);
						e.printStackTrace();
					}
					String contents = FileUtility.getContents(file);
					return contents;
				}
			}
		}
		return null;
	}
	
	public void exportObjects(String serializedObjects, String fileName) {
			File sdRoot = new File(Environment.getExternalStorageDirectory(), ROOT_OF_EXPORTED_FILE);
			sdRoot.mkdirs();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
			String dateFormatted = sdf.format(new Date());
			String tmpFileName = fileName + "_" + dateFormatted + ".txt";
			File file = new File(sdRoot, tmpFileName);
			try {
				FileUtility.setContents(file, serializedObjects);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(getClass().getName(),"Error during the export. Problem with temporary file", e);
			}
			Uri uri = Uri.fromFile(file);
			emailActivity(serializedObjects, uri, context);
			//file.delete();
	}
	
	public void emailActivity(String jsonObjects, Uri uri, Context context) {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		String dateFormatted = DateFormat.getInstance().format(new Date());
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				context.getString(R.string.messageSubjectForExport) + dateFormatted);
		emailIntent.putExtra(Intent.EXTRA_TEXT, jsonObjects);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.sendMail)));
	}
}