package com.sensorstack.sensors;

/**
 * Encapsulates the parameters for defining GSR data
 */
public class GSR extends Metadata {
	private float conductanceValue;
	private float resistanceValue;

	public float getConductanceValue() {
		return conductanceValue;
	}
	public void setConductanceValue(float value) {
		this.conductanceValue = value;
	}
	public float getResistanceValue() {
		return resistanceValue;
	}
	public void setResistanceValue(float resistanceValue) {
		this.resistanceValue = resistanceValue;
	}
}
