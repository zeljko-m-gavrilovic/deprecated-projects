package rs.os.checklist.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import rs.os.checklist.model.Described;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

/**
 * Common application resource functionalities which are add here in order to prevent copy/paste of code. 

 * @author zgavrilovic
 *
 */
public class ResourceUtility {

	public static enum ResourceType {string, drawable};
	
	/**
	 * Prevent instantiation of this class outside of this class
	 */
	private ResourceUtility() {
	}
	
	private static Integer getIdentifierByName(Context context, String aString, ResourceType resourceType) {
		int resId = context.getResources().getIdentifier(aString,
				resourceType.name(), context.getPackageName());
		return resId;
		
	}
	
	/*private static String getResourceByName(Context context, String aString, ResourceType resourceType) {
		try {
			return context.getString(getIdentifierByName(context, aString, resourceType));
		} catch(NotFoundException nfe) {
			return "?" + aString + "?";
		}
	}*/
	
	public static void setImage(Described described, ImageView ico, Context context) {
		if (described.getImageName() != null) {
			if (described.getImageName().startsWith("content:")) {
				setImageFromSDCard(described, ico, context);
			} else {
				setImageFromApplication(described, ico, context);
			}
		}
	}

	/**
	 * Set the image from the application drawable folder
	 * 
	 * @param described is object which keeps path to image
	 * @param ico is image view
	 * @param context is context of the activity
	 */
	private static void setImageFromApplication(Described described, ImageView ico, Context context) {
		Integer identifierByName = ResourceUtility.getIdentifierByName(
				context, described.getImageName(),
				ResourceUtility.ResourceType.drawable);
		ico.setImageResource(identifierByName);
		if (identifierByName != null) {
			ico.setImageResource(identifierByName);
		}
	}

	/**
	 * Set the image from the SD card
	 * 
	 * @param described is object which keeps path to image
	 * @param ico is image view
	 * @param context is context of the activity
	 */
	private static void setImageFromSDCard(Described described, ImageView ico, Context context) {
		Uri uri = Uri.parse(described.getImageName());
		InputStream fis = null;
		try {
			fis = context.getContentResolver().openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap bMap = BitmapFactory.decodeStream(fis, null, options);
			ico.setImageBitmap(bMap);
			bMap = null;
		} catch (FileNotFoundException e) {
			Log.e(ResourceUtility.class.getName(), "image with path " + described.getImageName(), e);
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				Log.e(ResourceUtility.class.getName(), "Exception closing input stream to URI " + uri.toString(), e);
				e.printStackTrace();
			}
		}
	}
	
}