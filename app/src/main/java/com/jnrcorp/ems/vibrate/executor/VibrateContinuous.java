package com.jnrcorp.ems.vibrate.executor;

import android.content.Context;
import android.os.Vibrator;

import com.jnrcorp.ems.sqllite.model.VibrateType;

public class VibrateContinuous extends VibrateAlertExecutor {

	private Vibrator vibrator;

	public VibrateContinuous(Context context) {
		super(context);
	}

	public static VibrateType getVibrateType() {
		return VibrateType.VIBRATE_CONTINUOUS;
	}

	@Override
	protected void startVibratePattern() {
		vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, 3000, 3000 };
		vibrator.vibrate(pattern, 0);
	}

	@Override
	protected void stopVibratePattern() {
		if (vibrator != null) {
			vibrator.cancel();
		}
	}

}
