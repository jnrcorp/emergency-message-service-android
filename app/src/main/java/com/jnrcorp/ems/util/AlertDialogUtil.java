package com.jnrcorp.ems.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jnrcorp.ems.R;

public class AlertDialogUtil {

	public static AlertDialog createOkAlertDialog(Context context, String outputMessage) {
		return createOkAlertDialog(context, null, outputMessage);
	}

	public static AlertDialog createOkAlertDialog(Context context, String title, CharSequence outputMessage) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setMessage(outputMessage);
		if (title != null) {
			dialogBuilder.setTitle(title);
		}
		dialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		return dialogBuilder.create();
	}

	public static AlertDialog createNoButtonDialog(Context context, String outputMessage) {
		return new AlertDialog.Builder(context).setMessage(outputMessage).create();
	}

	public static AlertDialog createNoButtonDialog(Context context, String title, String outputMessage) {
		return new AlertDialog.Builder(context).setTitle(title).setMessage(outputMessage).create();
	}

}
