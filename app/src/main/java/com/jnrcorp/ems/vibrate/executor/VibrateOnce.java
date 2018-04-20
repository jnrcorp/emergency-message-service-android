package com.jnrcorp.ems.vibrate.executor;

import android.content.Context;
import android.os.Vibrator;

import com.jnrcorp.ems.sqllite.model.VibrateType;

public class VibrateOnce extends VibrateAlertExecutor {

	private Vibrator vibrator;

	public VibrateOnce(Context context) {
		super(context);
	}

	public static VibrateType getVibrateType() {
		return VibrateType.VIBRATE_ONCE;
	}

	@Override
	protected void startVibratePattern() {
		vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(3000);
	}

	@Override
	protected void stopVibratePattern() {
		if (vibrator != null) {
			vibrator.cancel();
		}
	}

}
