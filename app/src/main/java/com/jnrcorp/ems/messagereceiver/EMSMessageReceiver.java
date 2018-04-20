package com.jnrcorp.ems.messagereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.model.AlertMessageContext;
import com.jnrcorp.ems.model.MessageContext;
import com.jnrcorp.ems.rule.factory.EMSRuleFactory;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;

public class EMSMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle pudsBundle = intent.getExtras();
		Object[] pdus = (Object[]) pudsBundle.get("pdus");
		SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
		Log.i(Constants.LOG_TAG, message.getOriginatingAddress() + " - " + message.getMessageBody());
		MessageContext messageContext = new MessageContext(message.getMessageBody(), message.getOriginatingAddress());
		EMSRuleFactory emsRuleFactory = new EMSRuleFactory(context, messageContext);
		EmergencyMessageServiceRule emsRule = emsRuleFactory.execute();
		if (emsRule != null) {
			Log.i(Constants.LOG_TAG, "Found a rule: " + emsRule);
			goToAlertActivity(context, messageContext, emsRule);
//			addMessageToInbox(context, message);
//			abortBroadcast();
		}
	}

//	private void addMessageToInbox(Context context, SmsMessage message) {
//		ContentValues values = new ContentValues();
//		values.put("address", message.getOriginatingAddress());
//		values.put("body", message.getMessageBody());
//		values.put("read", Boolean.TRUE);
//		values.put("date", message.getTimestampMillis());
//		context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
//	}

	private void goToAlertActivity(Context context, MessageContext messageContext, EmergencyMessageServiceRule emsRule) {
		AlertMessageContext alertMessageContext = new AlertMessageContext(messageContext, emsRule.getRuleId());
		Intent i = new Intent();
		i.setClassName(Constants.APP_PACKAGE, Constants.ALERT_ACTIVITY);
		i.putExtra(Constants.ALERT_MESSAGE_CONTEXT, alertMessageContext);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
