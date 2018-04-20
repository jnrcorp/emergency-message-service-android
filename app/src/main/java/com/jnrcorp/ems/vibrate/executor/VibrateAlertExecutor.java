package com.jnrcorp.ems.vibrate.executor;

import android.content.Context;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;

public abstract class VibrateAlertExecutor {

	private Context context;

	public VibrateAlertExecutor(Context context) {
		super();
		this.context = context;
	}

	public void startVibrateAlert() {
		Log.i(Constants.LOG_TAG, "Found an alert executor: " + this.getClass());
		startVibratePattern();
	}

	public void turnOffVibrateAlert() {
		stopVibratePattern();
	}

	protected abstract void startVibratePattern();

	protected abstract void stopVibratePattern();

	protected Context getContext() {
		return context;
	}

}
