package com.jnrcorp.ems.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.VibrateType;

public class RulesDAO extends BaseDAO {

	private String[] allRuleColumns = {
		EMSOpenHelper.RULE_ID, EMSOpenHelper.ALERT_TYPE, EMSOpenHelper.RINGTONE, EMSOpenHelper.VOLUME,
		EMSOpenHelper.VIBRATE, EMSOpenHelper.TURN_SCREEN_ON, EMSOpenHelper.FLASH_LIGHT, EMSOpenHelper.ENABLED
	};

	public RulesDAO(SQLiteDatabase database) {
		super(database);
	}

	public EmergencyMessageServiceRule createRule(EmergencyMessageServiceRule emsRule) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.ALERT_TYPE, emsRule.getAlertType().toString());
		values.put(EMSOpenHelper.RINGTONE, emsRule.getRingtone());
		values.put(EMSOpenHelper.VOLUME, emsRule.getVolume());
		values.put(EMSOpenHelper.VIBRATE, emsRule.getVibrateType().toString());
		values.put(EMSOpenHelper.TURN_SCREEN_ON, emsRule.isTurnScreenOn());
		values.put(EMSOpenHelper.FLASH_LIGHT, emsRule.isFlashLightOn());
		values.put(EMSOpenHelper.ENABLED, emsRule.isEnabled());
		if (emsRule.getRuleId() != 0) {
			getDatabase().update(EMSOpenHelper.TABLE_RULES, values, getRuleIdWhereClause(emsRule.getRuleId()), null);
			return getEmergencyMessageServiceRule(emsRule.getRuleId());
		} else {
			long insertId = getDatabase().insert(EMSOpenHelper.TABLE_RULES, null, values);
			return getEmergencyMessageServiceRule(insertId);
		}
	}

	public void deleteRule(EmergencyMessageServiceRule emsRule) {
		long emsRuleId = emsRule.getRuleId();
		Log.i(Constants.LOG_TAG, "EMS Rule deleted with id: " + emsRuleId);
		getDatabase().delete(EMSOpenHelper.TABLE_RULES, getRuleIdWhereClause(emsRuleId), null);
	}

	private String getRuleIdWhereClause(long emsRuleId) {
		return EMSOpenHelper.RULE_ID + " = " + emsRuleId;
	}

	public EmergencyMessageServiceRule getEmergencyMessageServiceRule(long emsRuleId) {
		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULES, allRuleColumns, getRuleIdWhereClause(emsRuleId), null, null, null, null);
		cursor.moveToFirst();
		EmergencyMessageServiceRule emsRule = convertToRule(cursor);
		cursor.close();
		return emsRule;
	}

	public List<EmergencyMessageServiceRule> getAllEmergencyMessageServiceRules() {
		List<EmergencyMessageServiceRule> emsRules = new ArrayList<EmergencyMessageServiceRule>();

		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULES, allRuleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EmergencyMessageServiceRule emsRule = convertToRule(cursor);
			emsRules.add(emsRule);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return emsRules;
	}

	private EmergencyMessageServiceRule convertToRule(Cursor cursor) {
		EmergencyMessageServiceRule emsRule = new EmergencyMessageServiceRule();
		emsRule.setRuleId(cursor.getLong(0));
		emsRule.setAlertType(AlertType.valueOf(cursor.getString(1)));
		emsRule.setRingtone(cursor.getString(2));
		emsRule.setVolume(cursor.getInt(3));
		emsRule.setVibrateType(VibrateType.valueOf(cursor.getString(4)));
		emsRule.setTurnScreenOn(isTrue(cursor.getInt(5)));
		emsRule.setFlashLightOn(isTrue(cursor.getInt(6)));
		emsRule.setEnabled(isTrue(cursor.getInt(7)));
		return emsRule;
	}

}
