package com.jnrcorp.ems.sqllite;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDAO {

	private SQLiteDatabase database;

	public BaseDAO(SQLiteDatabase database) {
		super();
		this.database = database;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	protected boolean isTrue(int value) {
		return value != 0;
	}

}
