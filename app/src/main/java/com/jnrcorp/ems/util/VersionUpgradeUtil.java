package com.jnrcorp.ems.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jnrcorp.ems.constant.Constants;

public class VersionUpgradeUtil {

	private VersionUpgradeUtil() {
		super();
	}

	public static VersionUpgradeData isVersionUpgrade(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0 );
			int currentVersionCode = packageInfo.versionCode;
			int lastVersionCodeRun = getLastVersionCodeRun(context);
			updateLastVersionCodeRun(context, currentVersionCode);
			VersionUpgradeData versionUpgradeData = new VersionUpgradeData(currentVersionCode, lastVersionCodeRun);
			return versionUpgradeData;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int getLastVersionCodeRun(Context context) {
		SharedPreferencesService sharedPreferencesService = new SharedPreferencesService(context);
		int lastVersionCodeRun = sharedPreferencesService.getInt(Constants.SHARED_PREFS_EMS_LAST_VERSION_RUN);
		return lastVersionCodeRun;
	}

	private static void updateLastVersionCodeRun(Context context, int currentVersionCode) {
		SharedPreferencesService sharedPreferencesService = new SharedPreferencesService(context);
		sharedPreferencesService.storeInt(Constants.SHARED_PREFS_EMS_LAST_VERSION_RUN, currentVersionCode);
	}

}
