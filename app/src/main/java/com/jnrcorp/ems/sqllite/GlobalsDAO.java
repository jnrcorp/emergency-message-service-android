package com.jnrcorp.ems.sqllite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GlobalsDAO extends BaseDAO {

	public enum GlobalNames {
		GLOBAL_INTERCEPTION_ACTIVE
	};

	private String[] allGlobalColumns = {
		EMSOpenHelper.GLOBAL_NAME, EMSOpenHelper.GLOBAL_TYPE, EMSOpenHelper.GLOBAL_VALUE
	};

	public GlobalsDAO(SQLiteDatabase database) {
		super(database);
	}

	public Boolean getGlobalBoolean(GlobalNames globalName) {
		Cursor cursor = getDatabase().query(EMSOpenHelper.TABLE_GLOBALS, allGlobalColumns, getGlobalNameWhereClause(globalName), null, null, null, null);
		cursor.moveToFirst();
		String type = cursor.getString(1);
		Boolean value = null;
		if (type.equals("boolean")) {
			value = Boolean.valueOf(cursor.getString(2));
		}
		cursor.close();
		return value;
	}

	public void updateGlobal(GlobalNames globalName, String value) {
		ContentValues values = new ContentValues();
		values.put(EMSOpenHelper.GLOBAL_VALUE, value);
		getDatabase().update(EMSOpenHelper.TABLE_GLOBALS, values, getGlobalNameWhereClause(globalName), null);
	}

	private String getGlobalNameWhereClause(GlobalNames globalName) {
		return EMSOpenHelper.GLOBAL_NAME + " = '" + globalName.toString() + "'";
	}

}
