package com.jnrcorp.ems.model;

import java.io.Serializable;

public class AlertMessageContext implements Serializable {

	private static final long serialVersionUID = -3248244686539928281L;

	private String message;
	private String senderPhoneNumber;
	private long emsRuleId;

	public AlertMessageContext(MessageContext messageContext, long emsRuleId) {
		super();
		this.message = messageContext.getMessage();
		this.senderPhoneNumber = messageContext.getSenderPhoneNumber();
		this.emsRuleId = emsRuleId;
	}

	public String getMessage() {
		return message;
	}

	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}

	public long getEmsRuleId() {
		return emsRuleId;
	}

}
