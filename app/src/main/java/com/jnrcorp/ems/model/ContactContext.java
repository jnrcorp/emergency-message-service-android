package com.jnrcorp.ems.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class ContactContext {

	private long contactId;
	private String contactLookupKey;
	private Uri contactUri;
	private String name;
	private String phoneNumber;
	private boolean foundContact;

	public ContactContext(Cursor contactLookup, String phoneNumber) {
		super();
		this.contactId = contactLookup.getLong(contactLookup.getColumnIndex(Contacts._ID));
		this.contactLookupKey = contactLookup.getString(contactLookup.getColumnIndex(Contacts.LOOKUP_KEY));
		this.contactUri = Contacts.getLookupUri(contactId, contactLookupKey);
		this.name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
	    if (name == null || name.equals("")) {
	    	name = phoneNumber;
	    } else {
	    	name += " [" + phoneNumber + "]";
	    }
		this.phoneNumber = phoneNumber;
		this.foundContact = true;
	}

	public ContactContext(String phoneNumber) {
		super();
    	this.name = phoneNumber;
		this.phoneNumber = phoneNumber;
		this.foundContact = false;
	}

	public long getContactId() {
		return contactId;
	}

	public String getContactLookupKey() {
		return contactLookupKey;
	}

	public Uri getContactUri() {
		return contactUri;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public boolean isFoundContact() {
		return foundContact;
	}

}
