package com.jnrcorp.ems.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	private boolean valid = true;
	private List<String> errorMessages = new ArrayList<String>();

	public ValidationResult() {
		super();
	}

	public void addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
		this.valid = false;
	}

	public boolean isValid() {
		return valid;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

}
