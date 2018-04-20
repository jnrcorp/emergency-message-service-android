package com.jnrcorp.ems.model;

import java.io.Serializable;

public class MessageContext implements Serializable {

	private static final long serialVersionUID = 3440833469977147500L;

	private String message;
	private String senderPhoneNumber;

	public MessageContext(String message, String senderPhoneNumber) {
		super();
		this.message = message;
		this.senderPhoneNumber = senderPhoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}

}
