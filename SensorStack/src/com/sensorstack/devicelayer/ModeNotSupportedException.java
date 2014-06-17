package com.sensorstack.devicelayer;

/**
 * Exception thrown when particular mode of connection with the hardware is not supported
 */
public class ModeNotSupportedException extends Exception{

	private static final long serialVersionUID = -7587248890432724247L;

	public ModeNotSupportedException() {
		// TODO Auto-generated constructor stub
	}
	
	public ModeNotSupportedException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}
	
	public ModeNotSupportedException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public ModeNotSupportedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
