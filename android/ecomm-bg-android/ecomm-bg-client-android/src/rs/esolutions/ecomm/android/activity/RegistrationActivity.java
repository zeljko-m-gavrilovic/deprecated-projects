
package rs.esolutions.ecomm.android.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.esolutions.ecomm.android.Constants;
import rs.esolutions.ecomm.android.R;
import rs.esolutions.ecomm.android.domain.Callback;
import rs.esolutions.ecomm.android.domain.RegistrationData;
import rs.esolutions.ecomm.android.service.HttpRegistrationTask;
import rs.esolutions.ecomm.android.service.PersistentPropertiesService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;

public class RegistrationActivity extends Activity {

	// constants
	private final String tag = this.getClass().getSimpleName();

	// GUI components
	protected TextView nameTextView;
	protected TextView surnameTextView;
	protected TextView phoneNumberTextView;
	protected Button registerButton;

	// services
	protected PersistentPropertiesService pps;

	// model
	protected Map<String, String> persistentProperties;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pps = new PersistentPropertiesService();
		try {
			persistentProperties = pps.readPersistentProperties();
		} catch (IOException e) {
			pps.showNoPersistentPropertes(getApplicationContext());
		}

		// manage GUI stuff
		setContentView(R.layout.registration);
		nameTextView = (TextView) findViewById(R.id.name);
		surnameTextView = (TextView) findViewById(R.id.surname);
		phoneNumberTextView = (TextView) findViewById(R.id.phone_number);
		phoneNumberTextView.setOnKeyListener(phoneNumberKeyListener);
		registerButton = (Button) findViewById(R.id.registration_button);
		registerButton.setOnClickListener(new RegistrationButtonClickHandler());
	}

	private OnKeyListener phoneNumberKeyListener = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
//				phoneNumberTextView.clearFocus();
				registerButton.requestFocus();
				return true;
			}
			return false;
		}
	};

	/**
	 * 
	 * Handle upload data button is clicked
	 */
	public class RegistrationButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.i(tag, "RegistrationButtonClickHandler.onClick()");
			if (Constants.DEBUG) {
				mainActivity();
				finish();
				return;
			}
			// prepare data
			RegistrationData rd = fromGuiToModel();
			List<String> validationErrors = validate(rd);
			if (validationErrors.size() == 0) {
				String applicationUrl = persistentProperties
						.get(PersistentPropertiesService.APPLICATION_URL);
				HttpRegistrationTask ps = new HttpRegistrationTask(RegistrationActivity.this, rd,
						applicationUrl, new Callback<Boolean>() {

							@Override
							public void onFinish(Boolean result) {
								mainActivity();
								finish();
							}
						});
				ps.execute();
			} else {
				showValidationErrors(validationErrors);
			}

		}

		private RegistrationData fromGuiToModel() {
			RegistrationData rd = new RegistrationData();
			rd.setName(nameTextView.getText().toString());
			rd.setSurname(surnameTextView.getText().toString());
			rd.setPhoneNumber(phoneNumberTextView.getText().toString());
			rd.setImsi(getMyPhoneImsi());
			return rd;
		}
	}

	private void mainActivity() {
		Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
		RegistrationActivity.this.startActivity(intent);
	}

	/**
	 * Vlidate registration data 
	 * 
	 * @param rd is object to be validated
	 * @return list of errors or empty list if there is no errors
	 */
	public List<String> validate(RegistrationData rd) {
		List<String> errorMessage = new ArrayList<String>();
		if ((rd.getName() == null) || (rd.getName().length() == 0)) {
			errorMessage.add((String) getText(R.string.name_mandatory));
		}
		if ((rd.getSurname() == null) || (rd.getSurname().length() == 0)) {
			errorMessage.add((String) getText(R.string.surname_mandatory));
		}
		if ((rd.getPhoneNumber() == null) || (rd.getPhoneNumber().length() == 0)) {
			errorMessage.add((String) getText(R.string.phone_number_mandatory));
		} else {
			boolean phoneNumberValidation = isPhoneNumberValid(rd.getPhoneNumber());
			if (!phoneNumberValidation) {
				errorMessage.add((String) getText(R.string.phone_number_not_valid));
			}
		}
		return errorMessage;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(tag, "onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
		nameTextView.setText(savedInstanceState.getString(RegistrationData.PROPERTY_NAME));
		surnameTextView.setText(savedInstanceState.getString(RegistrationData.PROPERTY_SURNAME));
		phoneNumberTextView.setText(savedInstanceState
				.getString(RegistrationData.PROPERTY_PHONE_NUMBER));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(RegistrationData.PROPERTY_NAME, nameTextView.getText().toString());
		outState.putString(RegistrationData.PROPERTY_SURNAME, surnameTextView.getText().toString());
		outState.putString(RegistrationData.PROPERTY_PHONE_NUMBER, phoneNumberTextView.getText()
				.toString());
		super.onSaveInstanceState(outState);
	}

	/**
	 * Returns phone SIM car unique IMSI
	 * 
	 * Note: SIM card can be identified by unique IMSI number. Each SIM card can have precise one IMSI and it can have 
	 * more than one MSISDN number.  
	 * 
	 * @return IMSI from SIM card
	 */
	private String getMyPhoneImsi() {
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// following line returns IMSI
		return mTelephonyMgr.getSubscriberId();
	}

	private void showValidationErrors(List<String> validationErrors) {
		String message = "";
		for (int i = 0; i < validationErrors.size(); i++) {
			String validationError = validationErrors.get(i);
			message += validationError;
			boolean lastElement = (i + 1) == validationErrors.size();
			if (!lastElement) {
				message += "\n";
			}
		}
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.ok_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	/** 
	* Validate phone method checks if the input string is a valid phone number.
	*
	* @param email String. Phone number to validate
	* @return boolean: true if phone number is valid, false otherwise.
	*/
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expressionWithCountryCode = "^\\+\\d{5}\\d{5,9}"; // +38164123456
		String phoneExpressionLocal = "^0\\d{2}\\d{5,9}"; //  064123456

		Pattern pattern1 = Pattern.compile(expressionWithCountryCode);
		Matcher matcher1 = pattern1.matcher(phoneNumber);

		Pattern pattern2 = Pattern.compile(phoneExpressionLocal);
		Matcher matcher2 = pattern2.matcher(phoneNumber);

		if (matcher1.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

}
