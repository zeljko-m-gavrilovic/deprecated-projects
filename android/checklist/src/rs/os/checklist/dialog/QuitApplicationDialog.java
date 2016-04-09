package rs.os.checklist.dialog;

import rs.os.checklist.R;
import rs.os.checklist.model.CallbackMethod;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class QuitApplicationDialog {

	public void openDialog(Context context, final CallbackMethod callbackMethod) {
	new AlertDialog.Builder(context)
	.setTitle(context.getString(R.string.quit))
	.setMessage(context.getString(R.string.quitQuestion))
	.setPositiveButton(context.getString(R.string.ok),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						callbackMethod.execute();
						break;
					}
					dialog.dismiss();
				}

			}).setNegativeButton(context.getString(R.string.cancel), null).create().show();
	}
}
