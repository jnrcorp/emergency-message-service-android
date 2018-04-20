package com.jnrcorp.ems.util;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jnrcorp.ems.sqllite.EMSOpenHelper;
import com.jnrcorp.ems.sqllite.RuleFiltersDAO;
import com.jnrcorp.ems.sqllite.model.FilterType;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;

public class DatabaseUpgradeUtil {

	private static final String CREATE_TABLE_RULES_V2 = "create table rules ("
			+ EMSOpenHelper.RULE_ID + " integer primary key autoincrement, "
			+ EMSOpenHelper.ALERT_TYPE + " varchar(50) not null, "
			+ EMSOpenHelper.RINGTONE + " varchar(100) null, "
			+ EMSOpenHelper.VOLUME + " integer not null, "
			+ EMSOpenHelper.VIBRATE + " varchar(50) not null, "
			+ EMSOpenHelper.TURN_SCREEN_ON + " varchar(50) not null, "
			+ EMSOpenHelper.FLASH_LIGHT + " varchar(50) not null, "
			+ EMSOpenHelper.ENABLED + " tinyint(4) not null "
			+ ");";

	private DatabaseUpgradeUtil() {
		super();
	}

	public static void upgradeToVersion2(SQLiteDatabase db) {
		db.execSQL(EMSOpenHelper.CREATE_TABLE_RULE_FILTERS);
		List<RuleFilter> ruleFilters = createRuleFiltersToInsert(db);
		RuleFiltersDAO ruleFiltersDAO = new RuleFiltersDAO(db);
		for (RuleFilter ruleFilter : ruleFilters) {
			ruleFiltersDAO.createRuleFilter(ruleFilter);
		}
		db.execSQL("ALTER TABLE rules RENAME TO rules_old;");
		db.execSQL(CREATE_TABLE_RULES_V2);
		db.execSQL("INSERT INTO rules SELECT rule_id, alert_type, ringtone, volume, vibrate, screen_on, flash_light, enabled FROM rules_old;");
		db.execSQL("DROP TABLE rules_old;");
	}

	private static List<RuleFilter> createRuleFiltersToInsert(SQLiteDatabase db) {
		List<RuleFilter> emsRuleFilters = new ArrayList<RuleFilter>();

		Cursor cursor = db.query(EMSOpenHelper.TABLE_RULES, new String[] { EMSOpenHelper.RULE_ID, EMSOpenHelper.RULE_TYPE, "telephone_start", "telephone_end" }, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long ruleId = cursor.getLong(0);
			RuleType ruleType = RuleType.valueOf(cursor.getString(1));
			String telephoneStart = cursor.getString(2);
			String telephoneEnd = cursor.getString(3);
			RuleFilter ruleFilter = new RuleFilter(FilterType.PHONE);
			ruleFilter.setRuleId(ruleId);
			ruleFilter.setRuleType(ruleType);
			ruleFilter.setFilterData0(telephoneStart);
			ruleFilter.setFilterData1(telephoneEnd);
			emsRuleFilters.add(ruleFilter);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return emsRuleFilters;
	}

}
