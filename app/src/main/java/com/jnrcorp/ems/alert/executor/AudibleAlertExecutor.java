package com.jnrcorp.ems.alert.executor;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.model.VolumeContext;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.util.MediaPlayerUtil;
import com.jnrcorp.ems.util.RingtoneUtil;

public abstract class AudibleAlertExecutor implements OnCompletionListener {

	private Context context;
	private EmergencyMessageServiceRule emsRule;
	private AlertMessageContext alertMessageContext;
	private VolumeContext originalVolumeContext;
	private MediaPlayer mediaPlayer;
	private boolean mediaPlayerReleased = false;

	public AudibleAlertExecutor(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		super();
		this.context = context;
		this.emsRule = emsRule;
		this.alertMessageContext = alertMessageContext;
	}

	protected abstract void executeMessageAlerts();

	public void startAudibleAlert() {
		Log.i(Constants.LOG_TAG, "Found an alert executor: " + this.getClass());
		storeOriginalVolumeSettings();
		setVolumeBasedOnRule();
		executeMessageAlerts();
	}

	protected void playNotification() {
		try {
			Uri notification = RingtoneUtil.getRingtoneUri(getEmsRule());
			mediaPlayer = MediaPlayerUtil.createMediaPlayer(getContext(), this, notification);
		    mediaPlayer.start();
		} catch (Exception e) {
		    e.printStackTrace();
		    Log.e(Constants.LOG_TAG, "Error starting media player: " + e.getMessage());
		    this.onCompletion(mediaPlayer);
		}
	}

	protected void stopMediaPlayer() {
		if (mediaPlayer != null && !mediaPlayerReleased) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayerReleased = true;
		}
	}

	public void turnOffAudibleAlert() {
		stopAudibleAlert();
		revertToOriginalVolumeSettings();
	}

	protected abstract void stopAudibleAlert();

	protected EmergencyMessageServiceRule getEmsRule() {
		return emsRule;
	}

	protected AlertMessageContext getAlertMessageContext() {
		return alertMessageContext;
	}

	protected Context getContext() {
		return context;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	private void revertToOriginalVolumeSettings() {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, originalVolumeContext.getNotificationVolume(), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		audioManager.setStreamVolume(AudioManager.STREAM_RING, originalVolumeContext.getRingVolume(), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeContext.getMediaVolume(), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	}

	private void setVolumeBasedOnRule() {
		int ruleVolume = emsRule.getVolume();
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (!isUsingEarPiece()) {
			audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, ruleVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			audioManager.setStreamVolume(AudioManager.STREAM_RING, ruleVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ruleVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		}
	}

	private boolean isUsingEarPiece() {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.isWiredHeadsetOn() || audioManager.isBluetoothA2dpOn();
	}

	private void storeOriginalVolumeSettings() {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int originalNotificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		int originalRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		int originalMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		originalVolumeContext = new VolumeContext(originalNotificationVolume, originalRingVolume, originalMediaVolume);
	}

}
