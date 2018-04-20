package com.jnrcorp.ems.model;

public class VolumeContext {

	private int notificationVolume;
	private int ringVolume;
	private int mediaVolume;

	public VolumeContext(int notificationVolume, int ringVolume, int mediaVolume) {
		super();
		this.notificationVolume = notificationVolume;
		this.ringVolume = ringVolume;
		this.mediaVolume = mediaVolume;
	}

	public int getNotificationVolume() {
		return notificationVolume;
	}

	public int getRingVolume() {
		return ringVolume;
	}

	public int getMediaVolume() {
		return mediaVolume;
	}

}
