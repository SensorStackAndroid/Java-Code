package com.sensorstack;

import com.sensorstack.sensors.Sensor;
import com.sensorstack.sensors.SensorNotDefinedException;

/**
 * DiscretePacketInfo class represents the parameters needed to fetch, unpack and validate 
 * discrete data packet comprising values from multiple discrete sensors. 
 * All data members have been initialised.
 */
public class DiscretePacketInfo {
	/**
	 * Specifies the sensors whose data values are required to be fetched. 
	 * The available sensors are defined in the Sensor class.
	 */
	private boolean requirement[] = new boolean[Sensor.NUM_DISCRETE];
	/**
	 * Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission before re-sending the request byte.
	 * Initialised to 1000 ms.
	 */
	private int timeout = 1000;				//timeout period for re-sending request
	/**
	 * Number of times request byte is sent to hardware until a data packet fetch request completes.
	 * Initialised to 5.
	 */
	private int numAttempts = 5;		//number of numAttempts for requesting data from h/w per request
	
	//variables for unpacking response packet
	/**
	 * Character used to delimit multiple values sent by a sensor in the response packet
	 * Initialized to '%'
	 */
	private char multiValueDelimiter = '%';		//delimiter for extracting different values from same sensor
	/**
	 * Character used to delimit data of two different sensors in the response packet.
	 * Initialised to '#'
	 */
	private char sensorDelimiter = '#';			//delimiter b/w 2 sensors data
	/**
	 * Character used to delimit beginning of data values from the sensor identifier for a sensor in the response packet
	 * Initialised to '@'
	 */
	private char sensorDataDelimiter = '@';		//delimiter b/w sensor id and data value 
	/**
	 * Character array specifying the sensor identifiers in the response packet for each requested sensor.
	 * Needs to be specified by the user.
	 */
	private char sensorIdentifier[] = new char[Sensor.NUM_DISCRETE]; //sensorId in the packet

	//private char packetStartDelimiter='S';  //not used yet
	
	public void setRequirement(int sensorId, boolean val) throws SensorNotDefinedException
	{
		if(sensorId>=0&&sensorId<Sensor.NUM_DISCRETE)
			requirement[sensorId]=val;
		else
			throw new SensorNotDefinedException("INVALID SENSOR - No Discrete Sensor exists with such Id.");
	}
	
	public void setRequirement(boolean[] array)
	{
		//copy values into requirement array
		for(int index=0;index<array.length&&index<Sensor.NUM_DISCRETE;index++)
			requirement[index]=array[index];
	}
	
	public boolean getRequirement(int sensor) throws SensorNotDefinedException
	{
		if(sensor>=0&&sensor<Sensor.NUM_DISCRETE)
			return requirement[sensor];
		else
			throw new SensorNotDefinedException("INVALID SENSOR - No Discrete Sensor exists with such Id.");
	}
	
	public boolean[] getRequirement()
	{
		return requirement;
	}
	
	public int getDiscreteTimeout() {
		return timeout;
	}

	public void setDiscreteTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getAttempts() {
		return numAttempts;
	}

	public void setNumAttempts(int  numAttempts) {
		this.numAttempts = numAttempts;
	}

	public char getSensorDelimiter() {
		return sensorDelimiter;
	}

	public void setSensorDelimiter(char sensorDelimiter) {
		this.sensorDelimiter = sensorDelimiter;
	}

	public char getSensorDataDelimiter() {
		return sensorDataDelimiter;
	}

	public void setSensorDataDelimiter(char sensorDataDelimiter) {
		this.sensorDataDelimiter = sensorDataDelimiter;
	}

	public char[] getSensorIdentifier() {
		return sensorIdentifier;
	}
	
	public char getSensorIdentifier(int sensorId) throws SensorNotDefinedException {
		if(sensorId>=0&&sensorId<=Sensor.NUM_DISCRETE)
			return sensorIdentifier[sensorId];
		throw new SensorNotDefinedException("INVALID SENSOR - No Discrete Sensor exists with such Id.");
	}

	public void setSensorIdentifier(char sensorIdentifier[]) {
		this.sensorIdentifier = sensorIdentifier;
	}

	/*public char getPacketStartDelimiter() {
		return packetStartDelimiter;
	}

	public void setPacketStartDelimiter(char packetStartDelimiter) {
		this.packetStartDelimiter = packetStartDelimiter;
	}*/

	public char getMultiValueDelimiter() {
		return multiValueDelimiter;
	}

	public void setMultiValueDelimiter(char multiValueDelimiter) {
		this.multiValueDelimiter = multiValueDelimiter;
	}
}
