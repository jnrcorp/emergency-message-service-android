package com.jnrcorp.ems.alert.executor;

import android.content.Context;
import android.media.MediaPlayer;

import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.texttospeech.TextToSpeechCompletionListener;
import com.jnrcorp.ems.texttospeech.TextToSpeechService;

public class ReadAloudOnce extends AudibleAlertExecutor implements TextToSpeechCompletionListener {

	private TextToSpeechService textToSpeechService;

	public ReadAloudOnce(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		super(context, emsRule, alertMessageContext);
	}

	public static AlertType getAlertType() {
		return AlertType.READ_ALOUD_ONCE;
	}

	@Override
	protected void executeMessageAlerts() {
		textToSpeechService = new TextToSpeechService(this, getContext(), getAlertMessageContext().getMessage());
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		// Not using media player here
	}

	@Override
	public void stopAudibleAlert() {
		if (textToSpeechService != null) {
			textToSpeechService.shutdown();
		}
	}

	@Override
	public void onTextToSpeechCompletion() {
		// do nothing, we only read aloud once
	}

	@Override
	public void onTextToSpeechFailure() {

	}

}
