package com.jnrcorp.ems.texttospeech;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;

public class TextToSpeechService extends UtteranceProgressListener implements OnInitListener {

	private TextToSpeechCompletionListener completionListener;
	private TextToSpeech textToSpeech;
	private String readAloud;

	public TextToSpeechService(TextToSpeechCompletionListener completionListener, Context context, String readAloud) {
		this.completionListener = completionListener;
		this.textToSpeech = new TextToSpeech(context, this);
		this.readAloud = readAloud;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = textToSpeech.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e(Constants.LOG_TAG, "This Language is not supported");
			} else {
				textToSpeech.setOnUtteranceProgressListener(this);
				speakOut(readAloud);
			}
		} else {
			Log.e(Constants.LOG_TAG, "Initilization Failed!");
		}
	}

	public void speakOut(String message) {
		HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance1");
		textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, params);
	}

	public void shutdown() {
		// Don't forget to shutdown tts!
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
	}

	@Override
	public void onDone(String utteranceId) {
		completionListener.onTextToSpeechCompletion();
	}

	@Override
	public void onError(String utteranceId) {
		completionListener.onTextToSpeechFailure();
	}

	@Override
	public void onStart(String utteranceId) {
		// Nothing to do on start
	}

}
