package com.jnrcorp.ems.sqllite.model;

import java.util.ArrayList;
import java.util.List;

import com.jnrcorp.ems.R;

public enum AlertType {

	RING_CONTINUOUS(1, R.string.ring_continuous_label, R.string.ring_continuous_detail, true, false),
	RING_ONCE(2, R.string.ring_once_label, R.string.ring_once_detail, true, false),
	RING_ONCE_READ_ALOUD_ONCE(3, R.string.ring_once_read_aloud_once_label, R.string.ring_once_read_aloud_once_detail, true, true),
	RING_ONCE_READ_ALOUD_CONTINUOUS(4, R.string.ring_once_read_aloud_continuous_label, R.string.ring_once_read_aloud_continuous_detail, true, true),
	RING_ONCE_READ_ALOUD_TWICE_REPEAT(5, R.string.ring_once_read_aloud_twice_repeat_label, R.string.ring_once_read_aloud_twice_repeat_detail, true, true),
	READ_ALOUD_ONCE(6, R.string.read_aloud_once_label, R.string.read_aloud_once_detail, false, true),
	READ_ALOUD_CONTINUOUS(7, R.string.read_aloud_continuous_label, R.string.read_aloud_continuous_detail, false, true);

	private int id;
	private int label;
	private int description;
	private boolean playsNotification;
	private boolean readsAloud;

	private AlertType(int id, int label, int description, boolean playsNotification, boolean readsAloud) {
		this.id = id;
		this.label = label;
		this.description = description;
		this.playsNotification = playsNotification;
		this.readsAloud = readsAloud;
	}

	public int getId() {
		return id;
	}

	public int getLabelResource() {
		return label;
	}

	public int getDescriptionResource() {
		return description;
	}

	public boolean isPlaysNotification() {
		return playsNotification;
	}

	public boolean isReadsAloud() {
		return readsAloud;
	}

	public static AlertType getById(int id) {
		for (AlertType alertType : AlertType.values()) {
			if (alertType.getId() == id) {
				return alertType;
			}
		}
		return null;
	}

	public static List<String> getAlertTypesForAdapter() {
		List<String> alertTypes = new ArrayList<String>();
		for (AlertType alertType : AlertType.values()) {
			alertTypes.add(alertType.toString());
		}
		return alertTypes;
	}

	public static List<AlertType> getForAlertTypeAdapter() {
		List<AlertType> alertTypes = new ArrayList<AlertType>();
		for (AlertType alertType : AlertType.values()) {
			alertTypes.add(alertType);
		}
		return alertTypes;
	}

}