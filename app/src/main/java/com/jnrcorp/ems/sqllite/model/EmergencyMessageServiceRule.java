package com.jnrcorp.ems.sqllite.model;

import java.util.ArrayList;
import java.util.List;

public class EmergencyMessageServiceRule {

	private long ruleId;
	private AlertType alertType;
	private String ringtone;
	private int volume;
	private VibrateType vibrateType;
	private boolean turnScreenOn;
	private boolean flashLightOn;
	private boolean enabled;

	private List<RuleFilter> ruleFilters = new ArrayList<RuleFilter>();
	private List<RuleTime> ruleTimes = new ArrayList<RuleTime>();

	public EmergencyMessageServiceRule() {
		super();
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(": Volume is " + volume);
		if (vibrateType != null) {
			sb.append(": " + vibrateType.toString());
		}
		if (alertType != null) {
			sb.append(": " + alertType.toString());
		}
		return sb.toString();
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public String getRingtone() {
		return ringtone;
	}

	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public VibrateType getVibrateType() {
		return vibrateType;
	}

	public void setVibrateType(VibrateType vibrateType) {
		this.vibrateType = vibrateType;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	public boolean isTurnScreenOn() {
		return turnScreenOn;
	}

	public void setTurnScreenOn(boolean turnScreenOn) {
		this.turnScreenOn = turnScreenOn;
	}

	public boolean isFlashLightOn() {
		return flashLightOn;
	}

	public void setFlashLightOn(boolean flashLightOn) {
		this.flashLightOn = flashLightOn;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<RuleFilter> getRuleFilters() {
		return ruleFilters;
	}

	public void setRuleFilters(List<RuleFilter> ruleFilters) {
		this.ruleFilters = ruleFilters;
	}

	public List<RuleTime> getRuleTimes() {
		return ruleTimes;
	}

	public void setRuleTimes(List<RuleTime> ruleTimes) {
		this.ruleTimes = ruleTimes;
	}

}
