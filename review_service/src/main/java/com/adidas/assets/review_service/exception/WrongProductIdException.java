/**
 * 
 */
package com.adidas.assets.review_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongProductIdException extends RuntimeException {

	public WrongProductIdException(String message) {
			super(message);
	}

}
