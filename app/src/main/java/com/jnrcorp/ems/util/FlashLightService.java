package com.jnrcorp.ems.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.widget.Toast;

import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;

public class FlashLightService {

	private Context context;
	private EmergencyMessageServiceRule emsRule;
	private Camera camera;
	private boolean isFlashon;
	private Handler handler;

	private Runnable toggleFlash = new Runnable() {
		@Override
		public void run() {
			if (isFlashon) {
				// do Flash off
				isFlashon = false;
				flashLightOff();
			} else {
				// do Flash on
				isFlashon = true;
				flashLightOn();
			}
			handler.postDelayed(this, 1000 * 1);
		}
	};

	public FlashLightService(Context context, EmergencyMessageServiceRule emsRule) {
		super();
		this.context = context;
		this.emsRule = emsRule;
		boolean flashAvailable = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (flashAvailable && emsRule.isFlashLightOn()) {
			setupHandler();
		}
	}

	private void setupHandler() {
		this.handler = new Handler();
	    handler.removeCallbacks(toggleFlash);
	    handler.postDelayed(toggleFlash, 1000 * 1);
	}

	public void killStrobe() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			flashLightOff();
		}
	}

	private void flashLightOn() {
		try {
			if (emsRule.isFlashLightOn()) {
				camera = Camera.open();
				Parameters p = camera.getParameters();
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				camera.startPreview();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Exception flashLightOn()", Toast.LENGTH_SHORT).show();
		}
	}

	private void flashLightOff() {
		try {
			if (camera != null) {
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Exception flashLightOff", Toast.LENGTH_SHORT).show();
		}
	}

}
