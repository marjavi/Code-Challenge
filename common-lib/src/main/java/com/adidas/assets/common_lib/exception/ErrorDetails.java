package com.adidas.assets.common_lib.exception;

import java.util.Date;
import java.util.List;

public class ErrorDetails {

	private Date timestamp;
	private String message;
	private String details;
	List<String> errors;
	
	
	/**
	 * 
	 * @param timestamp
	 * @param message
	 * @param details
	 * @param errors
	 */
	public ErrorDetails(Date timestamp, String message, String details, List<String> errors) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.errors = errors;
	}
		

	/**
	 * @param timestamp
	 * @param message
	 * @param details
	 */
	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}



	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErrorDetails [timestamp=" + timestamp + ", message=" + message + ", details=" + details + ", errors="
				+ errors + "]";
	}
	
	
}
