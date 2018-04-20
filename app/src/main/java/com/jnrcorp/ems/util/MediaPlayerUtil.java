package com.jnrcorp.ems.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class MediaPlayerUtil {

	private MediaPlayerUtil() {
		super();
	}

	public static MediaPlayer createMediaPlayer(Context context, OnCompletionListener onCompletionListener, Uri notification) {
		if (notification != null) {
			MediaPlayer mediaPlayer = MediaPlayer.create(context, notification);
		    mediaPlayer.setOnCompletionListener(onCompletionListener);
		    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    return mediaPlayer;
		} else {
			throw new NullPointerException("Notification Uri is null");
		}
	}

}
