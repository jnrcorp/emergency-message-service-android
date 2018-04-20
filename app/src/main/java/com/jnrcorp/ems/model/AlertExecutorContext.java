package com.jnrcorp.ems.model;

import com.jnrcorp.ems.alert.executor.AudibleAlertExecutor;
import com.jnrcorp.ems.util.FlashLightService;
import com.jnrcorp.ems.vibrate.executor.VibrateAlertExecutor;

public class AlertExecutorContext {

	private AudibleAlertExecutor audibleAlertExecutor;
	private VibrateAlertExecutor vibrateAlertExecutor;
	private FlashLightService flashLightService;

	public AlertExecutorContext(AudibleAlertExecutor audibleAlertExecutor, VibrateAlertExecutor vibrateAlertExecutor, FlashLightService flashLightService) {
		super();
		this.audibleAlertExecutor = audibleAlertExecutor;
		this.vibrateAlertExecutor = vibrateAlertExecutor;
		this.flashLightService = flashLightService;
	}

	public void stopAlerts() {
		if (audibleAlertExecutor != null) {
			audibleAlertExecutor.turnOffAudibleAlert();
		}
		if (vibrateAlertExecutor != null) {
			vibrateAlertExecutor.turnOffVibrateAlert();
		}
		if (flashLightService != null) {
			flashLightService.killStrobe();
		}
	}

	public AudibleAlertExecutor getAudibleAlertExecutor() {
		return audibleAlertExecutor;
	}

	public VibrateAlertExecutor getVibrateAlertExecutor() {
		return vibrateAlertExecutor;
	}

	public FlashLightService getFlashLightService() {
		return flashLightService;
	}

}
