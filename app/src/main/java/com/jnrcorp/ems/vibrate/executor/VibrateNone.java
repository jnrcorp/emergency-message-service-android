package com.jnrcorp.ems.vibrate.executor;

import android.content.Context;

import com.jnrcorp.ems.sqllite.model.VibrateType;

public class VibrateNone extends VibrateAlertExecutor {

	public VibrateNone(Context context) {
		super(context);
	}

	public static VibrateType getVibrateType() {
		return VibrateType.NO_VIBRATE;
	}

	@Override
	protected void startVibratePattern() {
		// No Vibrate to start
	}

	@Override
	protected void stopVibratePattern() {
		// No vibrate to stop
	}

}
