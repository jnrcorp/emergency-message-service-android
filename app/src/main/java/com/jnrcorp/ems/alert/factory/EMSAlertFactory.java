package com.jnrcorp.ems.alert.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.jnrcorp.ems.alert.executor.AudibleAlertExecutor;
import com.jnrcorp.ems.alert.executor.ReadAloudContinuous;
import com.jnrcorp.ems.alert.executor.ReadAloudOnce;
import com.jnrcorp.ems.alert.executor.RingContinuous;
import com.jnrcorp.ems.alert.executor.RingOnce;
import com.jnrcorp.ems.alert.executor.RingOnceReadAloudContinuous;
import com.jnrcorp.ems.alert.executor.RingOnceReadAloudOnce;
import com.jnrcorp.ems.alert.executor.RingOnceReadAloudTwiceRepeat;
import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.model.AlertExecutorContext;
import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.VibrateType;
import com.jnrcorp.ems.util.FlashLightService;
import com.jnrcorp.ems.vibrate.executor.VibrateAlertExecutor;
import com.jnrcorp.ems.vibrate.executor.VibrateContinuous;
import com.jnrcorp.ems.vibrate.executor.VibrateNone;
import com.jnrcorp.ems.vibrate.executor.VibrateOnce;

public class EMSAlertFactory {

	private Context context;
	private EmergencyMessageServiceRule emsRule;
	private AlertMessageContext alertMessageContext;
	private static Map<AlertType, Class<? extends AudibleAlertExecutor>> audibleAlertExecutorsByAlertType = new HashMap<AlertType, Class<? extends AudibleAlertExecutor>>();
	private static Map<VibrateType, Class<? extends VibrateAlertExecutor>> vibrateAlertExecutorsByVibrateType = new HashMap<VibrateType, Class<? extends VibrateAlertExecutor>>();

	static {
		audibleAlertExecutorsByAlertType.put(RingContinuous.getAlertType(), RingContinuous.class);
		audibleAlertExecutorsByAlertType.put(RingOnce.getAlertType(), RingOnce.class);
		audibleAlertExecutorsByAlertType.put(RingOnceReadAloudOnce.getAlertType(), RingOnceReadAloudOnce.class);
		audibleAlertExecutorsByAlertType.put(RingOnceReadAloudContinuous.getAlertType(), RingOnceReadAloudContinuous.class);
		audibleAlertExecutorsByAlertType.put(RingOnceReadAloudTwiceRepeat.getAlertType(), RingOnceReadAloudTwiceRepeat.class);
		audibleAlertExecutorsByAlertType.put(ReadAloudOnce.getAlertType(), ReadAloudOnce.class);
		audibleAlertExecutorsByAlertType.put(ReadAloudContinuous.getAlertType(), ReadAloudContinuous.class);
	}

	static {
		vibrateAlertExecutorsByVibrateType.put(VibrateNone.getVibrateType(), VibrateNone.class);
		vibrateAlertExecutorsByVibrateType.put(VibrateOnce.getVibrateType(), VibrateOnce.class);
		vibrateAlertExecutorsByVibrateType.put(VibrateContinuous.getVibrateType(), VibrateContinuous.class);
	}

	public EMSAlertFactory(Context context, EmergencyMessageServiceRule emsRule, AlertMessageContext alertMessageContext) {
		this.context = context;
		this.emsRule = emsRule;
		this.alertMessageContext = alertMessageContext;
	}

	public AlertExecutorContext execute() {
		AudibleAlertExecutor audibleAlertExecutor = null;
		VibrateAlertExecutor vibrateAlertExecutor = null;
		try {
			audibleAlertExecutor = createAudibleAlertExecutor();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Constants.LOG_TAG, "Unable to start audible alert executor.");
		}
		try {
			vibrateAlertExecutor = createVibrateAlertExecutor();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Constants.LOG_TAG, "Unable to start vibrate alert executor.");
		}
		FlashLightService flashLightService = new FlashLightService(context, emsRule);
		return new AlertExecutorContext(audibleAlertExecutor, vibrateAlertExecutor, flashLightService);
	}

	private VibrateAlertExecutor createVibrateAlertExecutor() throws ReflectiveOperationException {
		Class<? extends VibrateAlertExecutor> vibrateAlertExecutorClass = vibrateAlertExecutorsByVibrateType.get(emsRule.getVibrateType());
		Constructor<? extends VibrateAlertExecutor> constructor = vibrateAlertExecutorClass.getConstructor(Context.class);
		VibrateAlertExecutor vibrateAlertExecutor = constructor.newInstance(context);
		vibrateAlertExecutor.startVibrateAlert();
		return vibrateAlertExecutor;
	}

	private AudibleAlertExecutor createAudibleAlertExecutor() throws ReflectiveOperationException {
		Class<? extends AudibleAlertExecutor> audibleAlertExecutorClass = audibleAlertExecutorsByAlertType.get(emsRule.getAlertType());
		Constructor<? extends AudibleAlertExecutor> constructor = audibleAlertExecutorClass.getConstructor(Context.class, EmergencyMessageServiceRule.class, AlertMessageContext.class);
		AudibleAlertExecutor audibleAlertExecutor = constructor.newInstance(context, emsRule, alertMessageContext);
		audibleAlertExecutor.startAudibleAlert();
		return audibleAlertExecutor;
	}

}
