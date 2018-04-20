package com.jnrcorp.ems.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import com.jnrcorp.ems.model.ContactContext;

public class ContactUtil {

	private ContactUtil() {
		super();
	}

	public static String getEmailById(Context context, Uri contactUri) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] {ContactsContract.CommonDataKinds.Email.ADDRESS};
	    Cursor contactLookup = contentResolver.query(contactUri, projection, null, null, null);
	    String normalizedNumber = null;
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            normalizedNumber = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    return normalizedNumber;
	}

	public static String getPhoneNumberById(Context context, Uri contactUri) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] {Contacts.HAS_PHONE_NUMBER, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
	    Cursor contactLookup = contentResolver.query(contactUri, projection, null, null, null);
	    String normalizedNumber = null;
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
//	            int hasPhoneNumber = contactLookup.getInt(contactLookup.getColumnIndex(Contacts.HAS_PHONE_NUMBER));
	            normalizedNumber = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    return normalizedNumber;
	}

	public static Uri getQuickBadgeContactUri(Context context, String number) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    Uri contactUri = null;

	    ContentResolver contentResolver = context.getContentResolver();
	    String[] projection = new String[] {Contacts._ID, Contacts.LOOKUP_KEY, Contacts.Photo.PHOTO };
	    Cursor contactLookup = contentResolver.query(uri, projection, null, null, null);
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            long contactId = contactLookup.getLong(contactLookup.getColumnIndex(Contacts._ID));
	            String lookupKey = contactLookup.getString(contactLookup.getColumnIndex(Contacts.LOOKUP_KEY));
	            contactUri = Contacts.getLookupUri(contactId, lookupKey);
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    return contactUri;
	}

	public static ContactContext getContactContextByNumber(Context context, String number) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		ContactContext contactContext = null;

	    ContentResolver contentResolver = context.getContentResolver();
	    String[] projection = new String[] {Contacts._ID, Contacts.LOOKUP_KEY, ContactsContract.PhoneLookup.DISPLAY_NAME };
	    Cursor contactLookup = contentResolver.query(uri, projection, null, null, null);
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            contactContext = new ContactContext(contactLookup, number);
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    if (contactContext == null) {
	    	contactContext = new ContactContext(number);
	    }
	    return contactContext;
	}

	public static Bitmap getPhotoThumbnail(Context context, long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri, new String[] { Contacts.Photo.PHOTO }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					InputStream is = new ByteArrayInputStream(data);
					return BitmapFactory.decodeStream(is);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

	public static Bitmap getDisplayPhoto(Context context, long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Uri displayPhotoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO);
		try {
			AssetFileDescriptor fd = context.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
			return BitmapFactory.decodeStream(fd.createInputStream());
		} catch (IOException e) {
			return null;
		}
	}

}
