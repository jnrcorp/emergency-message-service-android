package com.jnrcorp.ems.util;

import android.content.Context;

public class SmsMmsMessage {

	public static final String MESSAGE_TYPE_SMS = "sms";

	private Context context;
	private String address;
	private String contactId_string;
	private String body;
	private long timestamp;
	private long threadId;
	private int count;
	private long messageId;
	private String messageTypeSms;

	public SmsMmsMessage(Context context, String address, String contactId_string, String body, long timestamp, long threadId, int count, long messageId, String messageTypeSms) {
		super();
		this.context = context;
		this.address = address;
		this.contactId_string = contactId_string;
		this.body = body;
		this.timestamp = timestamp;
		this.threadId = threadId;
		this.count = count;
		this.messageId = messageId;
		this.messageTypeSms = messageTypeSms;
	}

	@Override
	public String toString() {
		return address + " - " + body;
	}

	public Context getContext() {
		return context;
	}

	public String getAddress() {
		return address;
	}

	public String getContactId_string() {
		return contactId_string;
	}

	public String getBody() {
		return body;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getThreadId() {
		return threadId;
	}

	public int getCount() {
		return count;
	}

	public long getMessageId() {
		return messageId;
	}

	public String getMessageTypeSms() {
		return messageTypeSms;
	}

}
