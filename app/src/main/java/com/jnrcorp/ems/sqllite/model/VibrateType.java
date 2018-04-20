package com.jnrcorp.ems.sqllite.model;

import java.util.ArrayList;
import java.util.List;

import com.jnrcorp.ems.R;

public enum VibrateType {

	NO_VIBRATE(1, R.drawable.vibrate_off, R.string.vibrate_off_label, R.string.vibrate_off_detail),
	VIBRATE_ONCE(2, R.drawable.vibrate_once, R.string.vibrate_once_label, R.string.vibrate_once_detail),
	VIBRATE_CONTINUOUS(3, R.drawable.vibrate_continuous, R.string.vibrate_continuous_label, R.string.vibrate_continuous_detail);

	private int id;
	private int icon;
	private int label;
	private int description;

	private VibrateType(int id, int icon, int label, int description) {
		this.id = id;
		this.icon = icon;
		this.label = label;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public int getIcon() {
		return icon;
	}

	public int getLabelResource() {
		return label;
	}

	public int getDescriptionResource() {
		return description;
	}

	public static VibrateType getById(int id) {
		for (VibrateType vibrateType : VibrateType.values()) {
			if (vibrateType.getId() == id) {
				return vibrateType;
			}
		}
		return null;
	}

	public static List<String> getVibrateTypesForAdapter() {
		List<String> vibrateTypes = new ArrayList<String>();
		for (VibrateType vibrateType : VibrateType.values()) {
			vibrateTypes.add(vibrateType.toString());
		}
		return vibrateTypes;
	}

	public static List<VibrateType> getForVibrateTypeAdapter() {
		List<VibrateType> vibrateTypes = new ArrayList<VibrateType>();
		for (VibrateType vibrateType : VibrateType.values()) {
			vibrateTypes.add(vibrateType);
		}
		return vibrateTypes;
	}

}