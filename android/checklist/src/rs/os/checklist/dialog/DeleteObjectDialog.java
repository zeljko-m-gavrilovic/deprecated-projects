package rs.os.checklist.dialog;

import rs.os.checklist.R;
import rs.os.checklist.model.CallbackMethod;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Show confirmation dialog in order to ensure the user realy wants to delete the object
 * @author zgavrilovic
 *
 */
public class DeleteObjectDialog {

	public void openDialog(Context context, final CallbackMethod callbackMethod) {
		new AlertDialog.Builder(context)
		.setTitle(context.getString(R.string.delete))
		.setMessage(context.getString(R.string.deleteQuestion))
		.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					callbackMethod.execute();
					break;
				}
				dialog.dismiss();
			}
			
		})
		.setNeutralButton(context.getString(R.string.cancel), null)
		.create().show();
	}
}