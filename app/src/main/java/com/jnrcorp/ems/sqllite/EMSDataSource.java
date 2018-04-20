package com.jnrcorp.ems.sqllite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EMSDataSource {

	private SQLiteDatabase database;
	private EMSOpenHelper dbHelper;

	private RulesDAO rulesDAO;
	private RuleFiltersDAO ruleFiltersDAO;
	private RuleTimesDAO ruleTimesDAO;
	private RuleTimeDaysDAO ruleTimeDaysDAO;
	private GlobalsDAO globalsDAO;

	public EMSDataSource(Context context) {
		super();
		dbHelper = new EMSOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public RulesDAO getRulesDAO() {
		if (rulesDAO == null) {
			rulesDAO = new RulesDAO(database);
		}
		return rulesDAO;
	}

	public RuleFiltersDAO getRuleFiltersDAO() {
		if (ruleFiltersDAO == null) {
			ruleFiltersDAO = new RuleFiltersDAO(database);
		}
		return ruleFiltersDAO;
	}

	public RuleTimesDAO getRuleTimesDAO() {
		if (ruleTimesDAO == null) {
			ruleTimesDAO = new RuleTimesDAO(database);
		}
		return ruleTimesDAO;
	}

	public RuleTimeDaysDAO getRuleTimeDaysDAO() {
		if (ruleTimeDaysDAO == null) {
			ruleTimeDaysDAO = new RuleTimeDaysDAO(database);
		}
		return ruleTimeDaysDAO;
	}

	public GlobalsDAO getGlobalsDAO() {
		if (globalsDAO == null) {
			globalsDAO = new GlobalsDAO(database);
		}
		return globalsDAO;
	}

}
