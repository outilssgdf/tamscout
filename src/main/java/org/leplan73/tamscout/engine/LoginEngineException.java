package org.leplan73.tamscout.engine;

public class LoginEngineException extends Exception {

	public LoginEngineException(String message, Exception e) {
		super(e);
	}

	public LoginEngineException(String message) {
		super(message);
	}

}
