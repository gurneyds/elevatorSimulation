package org.kuali.careers.elevator.exceptions;

/**
 * Created by gurneyds on 8/29/16.
 */
public class ElevatorException extends Exception {
	String message;
	public ElevatorException() {}

	public ElevatorException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
