package com.jnrcorp.ems.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.sqllite.model.FilterType;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;

public class RuleFiltersDAO extends BaseDAO {

	private String[] allFilterColumns = {
		EMSOpenHelper.RULE_FILTER_ID, EMSOpenHelper.RULE_ID, EMSOpenHelper.FILTER_TYPE, EMSOpenHelper.RULE_TYPE, EMSOpenHelper.FILTER_DATA_0, EMSOpenHelper.FILTER_DATA_1
	};

	public RuleFiltersDAO(SQLiteDatabase database) {
		super(database);
	}

	public RuleFilter createRuleFilter(RuleFilter emsRuleFilter) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.RULE_ID, emsRuleFilter.getRuleId());
		values.put(EMSOpenHelper.FILTER_TYPE, emsRuleFilter.getFilterType().toString());
		values.put(EMSOpenHelper.RULE_TYPE, emsRuleFilter.getRuleType().toString());
		values.put(EMSOpenHelper.FILTER_DATA_0, emsRuleFilter.getFilterData0());
		values.put(EMSOpenHelper.FILTER_DATA_1, emsRuleFilter.getFilterData1());
		if (emsRuleFilter.getFilterId() != 0) {
			getDatabase().update(EMSOpenHelper.TABLE_RULE_FILTERS, values, getRuleFilterIdWhereClause(emsRuleFilter.getFilterId()), null);
			return getRuleFilter(emsRuleFilter.getFilterId());
		} else {
			long insertId = getDatabase().insert(EMSOpenHelper.TABLE_RULE_FILTERS, null, values);
			return getRuleFilter(insertId);
		}
	}

	public void deleteRuleFilter(RuleFilter ruleFilter) {
		long emsFilterId = ruleFilter.getFilterId();
		Log.i(Constants.LOG_TAG, "EMS Rule Filter deleted with id: " + emsFilterId);
		getDatabase().delete(EMSOpenHelper.TABLE_RULE_FILTERS, getRuleFilterIdWhereClause(emsFilterId), null);
	}

	private String getRuleFilterIdWhereClause(long ruleFilterId) {
		return EMSOpenHelper.RULE_FILTER_ID + " = " + ruleFilterId;
	}

	private String getRuleIdWhereClause(long emsRuleId) {
		return EMSOpenHelper.RULE_ID + " = " + emsRuleId;
	}

	public RuleFilter getRuleFilter(long ruleFilterId) {
		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_FILTERS, allFilterColumns, getRuleFilterIdWhereClause(ruleFilterId), null, null, null, null);
		cursor.moveToFirst();
		RuleFilter ruleFilter = convertToRuleFilter(cursor);
		cursor.close();
		return ruleFilter;
	}

	public List<RuleFilter> getAllRuleFilters(long emsRuleId) {
		List<RuleFilter> emsRuleFilters = new ArrayList<RuleFilter>();

		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_RULE_FILTERS, allFilterColumns, getRuleIdWhereClause(emsRuleId), null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RuleFilter emsRule = convertToRuleFilter(cursor);
			emsRuleFilters.add(emsRule);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return emsRuleFilters;
	}

	private RuleFilter convertToRuleFilter(Cursor cursor) {
		RuleFilter emsRuleFilter = new RuleFilter(FilterType.valueOf(cursor.getString(2)));
		emsRuleFilter.setFilterId(cursor.getLong(0));
		emsRuleFilter.setRuleId(cursor.getLong(1));
		emsRuleFilter.setRuleType(RuleType.valueOf(cursor.getString(3)));
		emsRuleFilter.setFilterData0(cursor.getString(4));
		emsRuleFilter.setFilterData1(cursor.getString(5));
		return emsRuleFilter;
	}

}
