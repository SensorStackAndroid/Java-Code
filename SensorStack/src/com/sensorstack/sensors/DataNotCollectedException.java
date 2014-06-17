package com.sensorstack.sensors;

/**
 * Exception thrown when requested data values are not available.
 */
public class DataNotCollectedException extends Exception{

	private static final long serialVersionUID = -7321926839210549235L;

	public DataNotCollectedException() {}
	
	public DataNotCollectedException(String detailMessage) {
		super(detailMessage);
	}
	
	public DataNotCollectedException(Throwable throwable) {
		super(throwable);
	}

	public DataNotCollectedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
