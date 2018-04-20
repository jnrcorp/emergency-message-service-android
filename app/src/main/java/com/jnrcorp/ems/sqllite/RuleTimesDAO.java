package com.jnrcorp.ems.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.util.TimeUtil;

public class RuleTimesDAO extends BaseDAO {

	private String[] allTimeColumns = {
		EMSOpenHelper.TIME_ID, EMSOpenHelper.RULE_ID, EMSOpenHelper.TIME_START, EMSOpenHelper.TIME_END
	};

	public RuleTimesDAO(SQLiteDatabase database) {
		super(database);
	}

	public RuleTime createRuleTime(RuleTime emsRuleTime) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.RULE_ID, emsRuleTime.getRuleId());
		values.put(EMSOpenHelper.TIME_START, TimeUtil.getTime24Hour(emsRuleTime.getTimeStart()));
		values.put(EMSOpenHelper.TIME_END, TimeUtil.getTime24Hour(emsRuleTime.getTimeEnd()));
		if (emsRuleTime.getTimeId() != 0) {
			getDatabase().update(EMSOpenHelper.TABLE_RULE_TIMES, values, getRuleTimeIdWhereClause(emsRuleTime.getTimeId()), null);
			return getRuleTime(emsRuleTime.getTimeId());
		} else {
			long insertId = getDatabase().insert(EMSOpenHelper.TABLE_RULE_TIMES, null, values);
			return getRuleTime(insertId);
		}
	}

	public void deleteRuleTime(RuleTime emsRuleTime) {
		long emsTimeId = emsRuleTime.getTimeId();
		Log.i(Constants.LOG_TAG, "EMS Rule Time deleted with id: " + emsTimeId);
		getDatabase().delete(EMSOpenHelper.TABLE_RULE_TIMES, getRuleTimeIdWhereClause(emsTimeId), null);
	}

	private String getRuleTimeIdWhereClause(long emsRuleId) {
		return EMSOpenHelper.TIME_ID + " = " + emsRuleId;
	}

	private String getRuleIdWhereClause(long emsRuleId) {
		return EMSOpenHelper.RULE_ID + " = " + emsRuleId;
	}

	public RuleTime getRuleTime(long emsTimeId) {
		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_TIMES, allTimeColumns, getRuleTimeIdWhereClause(emsTimeId), null, null, null, null);
		cursor.moveToFirst();
		RuleTime emsRuleTime = convertToRuleTime(cursor);
		cursor.close();
		return emsRuleTime;
	}

	public List<RuleTime> getAllRuleTimes(long emsRuleId) {
		List<RuleTime> emsRuleTimes = new ArrayList<RuleTime>();

		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_TIMES, allTimeColumns, getRuleIdWhereClause(emsRuleId), null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RuleTime emsRule = convertToRuleTime(cursor);
			emsRuleTimes.add(emsRule);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return emsRuleTimes;
	}

	private RuleTime convertToRuleTime(Cursor cursor) {
		RuleTime emsRuleTime = new RuleTime();
		emsRuleTime.setTimeId(cursor.getLong(0));
		emsRuleTime.setRuleId(cursor.getLong(1));
		emsRuleTime.setTimeStart(TimeUtil.getTime24Hour(cursor.getString(2)));
		emsRuleTime.setTimeEnd(TimeUtil.getTime24Hour(cursor.getString(3)));
		return emsRuleTime;
	}

}
