package com.jnrcorp.ems.sqllite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.sqllite.model.Day;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleTimeDay;

public class RuleTimeDaysDAO extends BaseDAO {

	private String[] allTimeColumns = {
		EMSOpenHelper.DAY_ID, EMSOpenHelper.TIME_ID, EMSOpenHelper.DAY
	};

	public RuleTimeDaysDAO(SQLiteDatabase database) {
		super(database);
	}

	public RuleTimeDay createRuleTimeDay(RuleTimeDay emsRuleTimeDay) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.TIME_ID, emsRuleTimeDay.getTimeId());
		values.put(EMSOpenHelper.DAY, emsRuleTimeDay.getDay().toString());
		long insertId = getDatabase().insert(EMSOpenHelper.TABLE_RULE_TIME_DAYS, null, values);
		return getRuleTimeDay(insertId);
	}

	public void deleteRuleTimeDays(RuleTime ruleTime) {
		long timeId = ruleTime.getTimeId();
		Log.i(Constants.LOG_TAG, "EMS Rule Time deleted with id: " + timeId);
		getDatabase().delete(EMSOpenHelper.TABLE_RULE_TIME_DAYS, getRuleTimeIdWhereClause(timeId), null);
	}

	private String getRuleTimeDayIdWhereClause(long dayId) {
		return EMSOpenHelper.DAY_ID + " = " + dayId;
	}

	private String getRuleTimeIdWhereClause(long timeId) {
		return EMSOpenHelper.TIME_ID + " = " + timeId;
	}

	public RuleTimeDay getRuleTimeDay(long emsDayId) {
		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_TIME_DAYS, allTimeColumns, getRuleTimeDayIdWhereClause(emsDayId), null, null, null, null);
		cursor.moveToFirst();
		RuleTimeDay emsRuleTimeDay = convertToRuleTimeDay(cursor);
		cursor.close();
		return emsRuleTimeDay;
	}

	public List<RuleTimeDay> getAllRuleTimeDays(long emsTimeId) {
		List<RuleTimeDay> emsRuleTimeDays = new ArrayList<RuleTimeDay>();

		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_TIME_DAYS, allTimeColumns, getRuleTimeIdWhereClause(emsTimeId), null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RuleTimeDay emsRuleTimeDay = convertToRuleTimeDay(cursor);
			emsRuleTimeDays.add(emsRuleTimeDay);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		Collections.sort(emsRuleTimeDays);
		return emsRuleTimeDays;
	}

	private RuleTimeDay convertToRuleTimeDay(Cursor cursor) {
		RuleTimeDay emsRuleTimeDay = new RuleTimeDay();
		emsRuleTimeDay.setDayId(cursor.getLong(0));
		emsRuleTimeDay.setTimeId(cursor.getLong(1));
		emsRuleTimeDay.setDay(Day.valueOf(cursor.getString(2)));
		return emsRuleTimeDay;
	}

}
