package com.jnrcorp.ems.action;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.util.AlertDialogUtil;

public class VersionUpgradeAction implements UserAction {

	private Context contexet;

	public VersionUpgradeAction(Context contexet) {
		super();
		this.contexet = contexet;
	}

	@Override
	public void executeAction() {

		StringBuilder helpMenu = new StringBuilder();
		helpMenu.append(contexet.getString(R.string.version_upgrade_message));

		final SpannableString helpText = new SpannableString(helpMenu.toString());
		Linkify.addLinks(helpText, Linkify.ALL);

		String title = contexet.getString(R.string.recent_changes);
		AlertDialog d = AlertDialogUtil.createOkAlertDialog(contexet, title, helpText);
		d.show();

		((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	}

}
