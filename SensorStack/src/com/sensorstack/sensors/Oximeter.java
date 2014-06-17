package com.sensorstack.sensors;

/**
 * Encapsulates the parameters for defining Oximeter data
 */
public class Oximeter extends Metadata {
	private float value;
	
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
