package com.jnrcorp.ems.action;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.jnrcorp.ems.R;

public class AboutAction implements UserAction {

	private Context contexet;

	public AboutAction(Context contexet) {
		super();
		this.contexet = contexet;
	}

	@Override
	public void executeAction() {
		try {
			PackageManager packageManager = contexet.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(contexet.getPackageName(), 0 );
			String versionName = packageInfo.versionName;
			Toast.makeText(contexet.getApplicationContext(), contexet.getString(R.string.about_message, versionName), Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

}
