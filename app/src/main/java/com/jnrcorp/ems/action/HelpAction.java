package com.jnrcorp.ems.action;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.util.AlertDialogUtil;

public class HelpAction implements UserAction {

	private Context contexet;

	public HelpAction(Context contexet) {
		super();
		this.contexet = contexet;
	}

	@Override
	public void executeAction() {

		StringBuilder helpMenu = new StringBuilder();
		helpMenu.append(contexet.getString(R.string.ems_help_1));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_2));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_3));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_4));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_5));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_6));
		helpMenu.append("\n\n"+contexet.getString(R.string.ems_help_7));

		final SpannableString helpText = new SpannableString(helpMenu.toString());
		Linkify.addLinks(helpText, Linkify.ALL);

		String title = contexet.getString(R.string.ems_help_title);
		AlertDialog d = AlertDialogUtil.createOkAlertDialog(contexet, title, helpText);
		d.show();

		((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	}

}
