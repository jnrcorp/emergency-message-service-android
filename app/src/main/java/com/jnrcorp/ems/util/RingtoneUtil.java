package com.jnrcorp.ems.util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;

public class RingtoneUtil {

	private RingtoneUtil() {
		super();
	}

	public static String getRingtoneTitle(Context context, String ringtone) {
		Uri ringtoneUri = RingtoneUtil.getRingtoneUri(ringtone);
		return RingtoneUtil.getRingtoneTitle(context, ringtoneUri);
	}

	public static String getRingtoneTitle(Context context, Uri ringtoneUri) {
		String ringtoneTitle;
		if (ringtoneUri != null) {
			Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
			ringtoneTitle = context.getResources().getString(R.string.ringtoneSelected, ringtone.getTitle(context));
		} else {
			ringtoneTitle = context.getResources().getString(R.string.noRingtoneSelected);
		}
		return ringtoneTitle;
	}

	public static Uri getRingtoneUri(EmergencyMessageServiceRule emsRule) {
		Uri selectedRingtone = getRingtoneUri(emsRule.getRingtone());
		if (selectedRingtone == null) {
			selectedRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		return selectedRingtone;
	}

	public static Uri getRingtoneUri(String ringtone) {
		Uri ringtoneUri = null;
		if (ringtone != null && !ringtone.equals("")) {
			ringtoneUri = Uri.parse(ringtone);
		}
		return ringtoneUri;
	}

}
