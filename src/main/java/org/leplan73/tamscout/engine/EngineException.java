package org.leplan73.tamscout.engine;

public class EngineException extends Exception {

	private static final long serialVersionUID = 1L;

	public EngineException(String message, Exception e) {
		super(message, e);
	}

	public EngineException(String message) {
		super(message);
	}

}
