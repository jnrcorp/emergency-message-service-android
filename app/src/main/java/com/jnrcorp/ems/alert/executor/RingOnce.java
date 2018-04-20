package com.jnrcorp.ems.alert.executor;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;

public class RingOnce extends AudibleAlertExecutor implements OnCompletionListener {

	public RingOnce(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		super(context, emsRule, alertMessageContext);
	}

	public static AlertType getAlertType() {
		return AlertType.RING_ONCE;
	}

	@Override
	protected void executeMessageAlerts() {
		playNotification();
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		stopAudibleAlert();
	}

	@Override
	public void stopAudibleAlert() {
		stopMediaPlayer();
	}

}
