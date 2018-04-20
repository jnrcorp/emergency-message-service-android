package com.jnrcorp.ems.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jnrcorp.ems.constant.Constants;

public class SharedPreferencesService {

	private Context context;

	public SharedPreferencesService(Context context) {
		this.context = context;
	}

	public String getItem(String name) {
    	SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
    	String value = sp.getString(name, "");
    	return value;
	}

	public void storeItem(String name, String value) {
		SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public int getInt(String name) {
    	SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
    	int value = sp.getInt(name, 0);
    	return value;
	}

	public void storeInt(String name, int value) {
		SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	public void removeItem(String name) {
		SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(name);
		editor.commit();
	}

	public void clearAllPreferences() {
    	SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFS_NAMESPACE, Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.clear();
    	editor.commit();
	}

}
