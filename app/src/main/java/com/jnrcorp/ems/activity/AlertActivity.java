package com.jnrcorp.ems.activity;

import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.alert.factory.EMSAlertFactory;
import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.model.AlertExecutorContext;
import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.model.ContactContext;
import com.jnrcorp.ems.model.MessageContext;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.service.RuleService;
import com.jnrcorp.ems.util.ContactUtil;

public class AlertActivity extends Activity implements OnClickListener {

	private RuleService ruleService;
	private AlertExecutorContext alertExecutorContext;
	private AlertMessageContext alertMessageContext;
	private EmergencyMessageServiceRule emsRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		ruleService = new RuleService(this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			alertMessageContext = (AlertMessageContext) extras.get(Constants.ALERT_MESSAGE_CONTEXT);
			emsRule = ruleService.loadRule(alertMessageContext.getEmsRuleId());
			EMSAlertFactory emsAlertFactory = new EMSAlertFactory(this, emsRule, alertMessageContext);
			alertExecutorContext = emsAlertFactory.execute();
			unlockScreen();
		} else {
			List<EmergencyMessageServiceRule> emsRules = ruleService.loadAllRules();
			if (emsRules != null && emsRules.size() > 0) {
				emsRule = emsRules.get(0);
			}
			MessageContext messageContext = new MessageContext("Testing the emergency message service. This is some default text when no message is passed in. 6800 Twin Buttes Marina Park Rd", "123459");
			alertMessageContext = new AlertMessageContext(messageContext, emsRule.getRuleId());
		}
		buildDisplay();
	};

	private void unlockScreen() {
		if (emsRule.isTurnScreenOn()) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		}
	}

	private void buildDisplay() {
		ContactContext contactContext = ContactUtil.getContactContextByNumber(this, alertMessageContext.getSenderPhoneNumber());
		TextView textMessageFrom = (TextView) findViewById(R.id.textMessageFrom);
		if (contactContext.isFoundContact()) {
			QuickContactBadge contactBadge = (QuickContactBadge) findViewById(R.id.quickbadge);
			contactBadge.assignContactUri(contactContext.getContactUri());
			contactBadge.setImageBitmap(ContactUtil.getPhotoThumbnail(this, contactContext.getContactId()));
			textMessageFrom.setText(contactContext.getName());
		} else {
			String fromLabel = getString(R.string.messageFrom, contactContext.getPhoneNumber());
			textMessageFrom.setText(fromLabel);
		}
		TextView textMessageBody = (TextView) findViewById(R.id.textMessageBody);
		Button killAlert = (Button) findViewById(R.id.killAlert);
		Button dismissAlert = (Button) findViewById(R.id.dismissAlert);
		textMessageBody.setText(alertMessageContext.getMessage());
		killAlert.setOnClickListener(this);
		dismissAlert.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.killAlert && alertExecutorContext != null) {
			turnOffAlerts();
		} else if (view.getId() == R.id.dismissAlert) {
			this.finish();
		}
	}

	private void turnOffAlerts() {
		alertExecutorContext.stopAlerts();
	}

	@Override
	protected void onResume() {
		ruleService = new RuleService(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		ruleService.close();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (alertExecutorContext != null) {
			turnOffAlerts();
		}
		ruleService.close();
		super.onDestroy();
	}

}
