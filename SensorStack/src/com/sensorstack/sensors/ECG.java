package com.sensorstack.sensors;

//single lead ecg (lead-II)
/**
 * Encapsulates the parameters for defining ECG data
 */
public class ECG extends SensorContinuous<Float>{
	/**
	 * Constructor to set the length of ECG data buffer 
	 * @param length Defines the length of the ECG data buffer
	 */
	public ECG(int length)
	{
		super(Float.class, length);
	}
	
	/**
	 * Constructor to initialise ECG data buffer
	 * @param sc data buffer
	 */
	public ECG(SensorContinuous<Float> sc)
	{
		super(Float.class, sc.length);
		this.setValueArray(sc.valueArray);
	}
}
