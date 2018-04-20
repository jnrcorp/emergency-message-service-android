package com.jnrcorp.ems.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsUtil {

	public static List<SmsMmsMessage> getSmsDetails(Context context) {

		String SORT_ORDER = "date DESC";
		int count = 0;

		List<SmsMmsMessage> smsMessages = new ArrayList<SmsMmsMessage>();
//		String[] messageColumns = new String[] { "thread_id", "address", "person", "date", "body"  };
		Uri uri = Uri.parse("content://mms-sms/conversations?simple=true");
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, SORT_ORDER);

		if (cursor != null) {
			try {
				count = cursor.getCount();
				if (count > 0) {
					do {
						cursor.moveToFirst();

						long messageId = cursor.getLong(cursor.getColumnIndex("_id"));
						long threadId = cursor.getLong(cursor.getColumnIndex("thread_id"));
						String address = cursor.getString(cursor.getColumnIndex("address"));
						long contactId = cursor.getLong(cursor.getColumnIndex("person"));
						String contactId_string = String.valueOf(contactId);
						long timestamp = cursor.getLong(cursor.getColumnIndex("date"));
						String body = cursor.getString(cursor.getColumnIndex("body"));

						SmsMmsMessage smsMessage = new SmsMmsMessage(context, address, contactId_string, body, timestamp, threadId, count, messageId, SmsMmsMessage.MESSAGE_TYPE_SMS);
						smsMessages.add(smsMessage);
					} while(cursor.moveToNext());
				}
			} finally {
				cursor.close();
			}
		}
		return smsMessages;
	}

}
