package com.sensorstack.sensors;

/**
 * Exception thrown when data values from an unsupported sensor are requested.
 */
public class SensorNotDefinedException extends Exception {

	private static final long serialVersionUID = 3747281084461477321L;

	public SensorNotDefinedException() {	}

	public SensorNotDefinedException(String detailMessage) {
		super(detailMessage);
	}

	public SensorNotDefinedException(Throwable throwable) {
		super(throwable);
	}

	public SensorNotDefinedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
