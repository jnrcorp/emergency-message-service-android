package com.jnrcorp.ems.alert.executor;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.texttospeech.TextToSpeechCompletionListener;
import com.jnrcorp.ems.texttospeech.TextToSpeechService;

public class ReadAloudContinuous extends AudibleAlertExecutor implements TextToSpeechCompletionListener, OnCompletionListener {

	private TextToSpeechService textToSpeechService;
	private boolean shutdown = false;

	public ReadAloudContinuous(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		super(context, emsRule, alertMessageContext);
	}

	public static AlertType getAlertType() {
		return AlertType.READ_ALOUD_CONTINUOUS;
	}

	@Override
	protected void executeMessageAlerts() {
		textToSpeechService = new TextToSpeechService(this, getContext(), getAlertMessageContext().getMessage());
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		// No media player
	}

	@Override
	public void stopAudibleAlert() {
		stopMediaPlayer();
		if (textToSpeechService != null) {
			textToSpeechService.shutdown();
		}
		shutdown = true;
	}

	@Override
	public void onTextToSpeechCompletion() {
		if (!shutdown) {
			textToSpeechService.speakOut(getAlertMessageContext().getMessage());
		}
	}

	@Override
	public void onTextToSpeechFailure() {
		shutdown = true;
	}

}
