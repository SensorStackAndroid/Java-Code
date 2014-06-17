package com.sensorstack.sensors;

/**
 * Encapsulates parameters for defining Blood Pressure data
 */
public class BloodPressure extends Metadata{
	private int systolicValue;
	private int diastolicValue;
	
	/**
	 * Returns the value of Systolic Pressure
	 * @return Systolic Pressure value
	 */
	public int getSystolicValue() {
		return systolicValue;
	}
	
	/**
	 * Sets the value of Systolic Pressure
	 * @param systoleValue Systolic Pressure value
	 */
	public void setSystolicValue(int systoleValue) {
		this.systolicValue = systoleValue;
	}
	
	/**
	 * Returns the value of Diastolic Pressure
	 * @return Diastolic Pressure value
	 */
	public int getDiastolicValue() {
		return diastolicValue;
	}
	
	/**
	 * Sets the value of Diastolic Pressure
	 * @param diastolicValue Diastolic Pressure value
	 */
	public void setDiastolicValue(int diastolicValue) {
		this.diastolicValue = diastolicValue;
	}
}
