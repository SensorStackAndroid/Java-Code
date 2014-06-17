package com.sensorstack.sensors;

/**
 * Encapsulates the parameters for defining Temperature data
 */
public class Temperature extends Metadata {
	private float value;
	
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
