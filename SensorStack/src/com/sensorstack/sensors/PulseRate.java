package com.sensorstack.sensors;

/**
 * Encapsulates the parameters for defining Pulse Rate data
 */
public class PulseRate extends Metadata {
	private int value;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
