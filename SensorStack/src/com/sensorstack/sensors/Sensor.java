package com.sensorstack.sensors;

/**
 * Encapsulates parameters for defining and identifying various supported discrete and continuous data type sensors.
 */
public final class Sensor {
	//Sensors requiring discrete data values have id before any of the sensor requiring continuous stream data
	
	//Discrete Data Sensors
	public static final int TEMPERATURE=0;
	public static final int BLOODPRESSURE=1;
	public static final int PULSERATE=2;
	public static final int OXIMETER=3;
	public static final int GSR=4;
	//Continuous Data Sensors
	public static final int ECG=5;	
	
	/**
	 * Defines the number of discrete sensors supported. Used for setting the length of requirement array in {@link com.sensorstack.DiscretePacketInfo}
	 */
	public static final int NUM_DISCRETE=5;
	
	/*
	 * Defines the total number of sensors supported.
	 
	public static final int LENGTH = 6;
	*/
	
	/**
	 * Returns the sensor name corresponding to the sensorID
	 * @param sensorId Integer value referring to a particular sensor 
	 * @return sensor name
	 */
	public static String sensorName(int sensorId)
	{
		switch(sensorId)
		{
			case 0:
				return "Temperature";
			case 1:
				return "Blood Pressure";
			case 2:
				return "Pulse Rate";
			case 3:
				return "Oximeter";
			case 4:
				return "GSR";
			case 5:
				return "ECG";
			default:
				return null;
		}
	}
}
