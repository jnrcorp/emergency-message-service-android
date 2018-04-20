package com.jnrcorp.ems.alert.executor;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;

public class RingContinuous extends AudibleAlertExecutor implements OnCompletionListener {

	public RingContinuous(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		super(context, emsRule, alertMessageContext);
	}

	public static AlertType getAlertType() {
		return AlertType.RING_CONTINUOUS;
	}

	@Override
	protected void executeMessageAlerts() {
		playNotification();
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		mediaPlayer.start();
	}

	@Override
	public void stopAudibleAlert() {
		stopMediaPlayer();
	}

}
