package com.jnrcorp.ems.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jnrcorp.ems.sqllite.GlobalsDAO.GlobalNames;
import com.jnrcorp.ems.util.DatabaseUpgradeUtil;

public class EMSOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_RULES = "rules";
	public static final String RULE_ID = "rule_id";
	public static final String ALERT_TYPE = "alert_type";
	public static final String RINGTONE = "ringtone";
	public static final String VOLUME = "volume";
	public static final String VIBRATE = "vibrate";
	public static final String TURN_SCREEN_ON = "screen_on";
	public static final String FLASH_LIGHT = "flash_light";
	public static final String ENABLED = "enabled";

	private static final String CREATE_TABLE_RULES = "create table "
		+ TABLE_RULES + "("
			+ RULE_ID + " integer primary key autoincrement, "
			+ ALERT_TYPE + " varchar(50) not null, "
			+ RINGTONE + " varchar(100) null, "
			+ VOLUME + " integer not null, "
			+ VIBRATE + " varchar(50) not null, "
			+ TURN_SCREEN_ON + " varchar(50) not null, "
			+ FLASH_LIGHT + " varchar(50) not null, "
			+ ENABLED + " tinyint(4) not null "
			+ ");";

	public static final String TABLE_RULE_FILTERS = "rule_filters";
	public static final String RULE_FILTER_ID = "rule_filter_id";
	public static final String RULE_TYPE = "rule_type";
	public static final String FILTER_TYPE = "filter_type";
	public static final String FILTER_DATA_0 = "filter_data_0";
	public static final String FILTER_DATA_1 = "filter_data_1";
	public static final String CREATE_TABLE_RULE_FILTERS = "create table "
		+ TABLE_RULE_FILTERS + "("
			+ RULE_FILTER_ID + " integer primary key autoincrement, "
			+ RULE_ID + " integer not null, "
			+ FILTER_TYPE + " varchar(50) not null, "
			+ RULE_TYPE + " varchar(50) not null, "
			+ FILTER_DATA_0 + " varchar(50) not null, "
			+ FILTER_DATA_1 + " varchar(50) null "
			+ ");";

	public static final String TABLE_RULE_TIMES = "rule_times";
	public static final String TIME_ID = "time_id";
	public static final String TIME_START = "time_start";
	public static final String TIME_END = "time_end";
	private static final String CREATE_TABLE_RULE_TIMES = "create table "
		+ TABLE_RULE_TIMES + " ("
			+ TIME_ID + " integer primary key autoincrement, "
			+ RULE_ID + " integer not null, "
			+ TIME_START + " string not null, "
			+ TIME_END + " string not null "
			+ ");";

	public static final String TABLE_RULE_TIME_DAYS = "rule_time_days";
	public static final String DAY_ID = "day_id";
	public static final String DAY = "day";
	private static final String CREATE_TABLE_RULE_TIME_DAYS = "create table "
		+ TABLE_RULE_TIME_DAYS + " ("
			+ DAY_ID + " integer primary key autoincrement, "
			+ TIME_ID + " integer not null, "
			+ DAY + " string not null "
			+ ");";

	public static final String TABLE_GLOBALS = "globals";
	public static final String GLOBAL_NAME = "name";
	public static final String GLOBAL_TYPE = "type";
	public static final String GLOBAL_VALUE = "value";

	private static final String CREATE_TABLE_GLOBALS = "create table "
		+ TABLE_GLOBALS + " ("
			+ GLOBAL_NAME + " varchar(50) primary key, "
			+ GLOBAL_TYPE + " varchar(50) not null, "
			+ GLOBAL_VALUE + " varchar(50) not null "
			+ ");";

	private static final String DATABASE_NAME = "emergency_message_service.db";
	private static final int DATABASE_VERSION = 2;

	public EMSOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_GLOBALS);
		db.execSQL(CREATE_TABLE_RULES);
		db.execSQL(CREATE_TABLE_RULE_FILTERS);
		db.execSQL(CREATE_TABLE_RULE_TIMES);
		db.execSQL(CREATE_TABLE_RULE_TIME_DAYS);
		insertActiveFlag(db);
	}

	private void insertActiveFlag(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.GLOBAL_NAME, GlobalNames.GLOBAL_INTERCEPTION_ACTIVE.toString());
		values.put(EMSOpenHelper.GLOBAL_TYPE, "boolean");
		values.put(EMSOpenHelper.GLOBAL_VALUE, Boolean.TRUE.toString());
		db.insert(EMSOpenHelper.TABLE_GLOBALS, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(EMSOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will migrate data to the newest format.");
		if (oldVersion < 2) {
			DatabaseUpgradeUtil.upgradeToVersion2(db);
		}
	}

	public void clearDB(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_FILTERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_TIMES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_TIME_DAYS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOBALS);
		onCreate(db);
	}

}
