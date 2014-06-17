package com.sensorstack.devicelayer;

/**
 * Exception thrown if no hardware device is connected to query
 */
public class NoDeviceConnectedException extends Exception {

	private static final long serialVersionUID = -8249893570844941139L;

	public NoDeviceConnectedException(String string) {
		super(string);
	}

}
