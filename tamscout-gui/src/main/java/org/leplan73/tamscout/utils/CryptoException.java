package org.leplan73.tamscout.utils;

public class CryptoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CryptoException(Exception e) {
		super(e);
	}

	public CryptoException(String string) {
		super(string);
	}
}
