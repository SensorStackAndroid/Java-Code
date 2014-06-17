package com.sensorstack;

import com.sensorstack.sensors.Sensor;

/***
 * ContinuousStreamInfo represents the parameters needed to fetch, unpack and validate 
 * continuous streaming data. All data members have been initialised.
 */
public class ContinuousStreamInfo {
	/***
	 * Represents the sensor identifier(as defined in the Sensor class) of the requested sensor.
	 * Initialised to ECG sensor. 
	 */
	private int sensorNum = Sensor.ECG;
	/***
	 * Byte sent to the hardware for stopping transmission of streaming data.
	 * Initialised to 'Q'.
	 */
	private byte stop='Q';		//stop request sent to h/w
	/***
	 * Byte received from the hardware as an acknowledgement of stop request for streaming data.
	 * Initialised to '!'.
	 */
	private char stopAck='!';		//ack received from h/w after end of data stream
	/***
	 * Represents the count of data values to be collected at a time for the specified sensor.
	 * Needs to be specified by the user if dataDuration and samplingRate are not specified. 
	 * Default value is 0. 
	 */
	private int dataLength=0;		//length of continuous stream sensor array
	/***
	 * Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission for the requested stream.
	 * Initialised to 120000 ms.
	 */
	private int timeout = 120000;		//in milliseconds
	/***
	 * Represents the duration (in seconds) of the streaming data to be collected.
	 * Needs to be specified along with the samplingRate of the hardware if the dataLength is not specified.
	 */
	private int dataDuration;	//in seconds
	/***
	 * Represents the samplingRate (in samples per second) of the streaming data sensor.
	 * Needs to be specified along with the dataDuration of the stream request if the dataLength is not specified.
	 */
	private int samplingRate;	//in samples per second
	
	//delimiters for metadata
	/***
	 * Represents sensor identifier for unpacking metadata.
	 * Initialised to 'E' corresponding to ECG sensor
	 */
	private char sensorIdentifier = 'E';
	/***
	 * Represents sensor data delimiter for unpacking metadata.
	 * Initialised to '@'
	 */
	private char sensorDataDelimiter = '@';
	/***
	 * Represents multi value delimiter for unpacking metadata.
	 * Initialised to '%'
	 */
	private char multiValueDelimiter = '%';
	
	public int getSamplingRate() {
		return samplingRate;
	}
	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}
	public int getDataDuration() {
		return dataDuration;
	}
	public void setDataDuration(int dataDuration) {
		this.dataDuration = dataDuration;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getDataLength() {
		return dataLength;
	}
	public int getDataLength(int samplingrate, int duration) {
		return samplingRate*duration;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public void setDataLength(int samplingrate, int duration) {
		this.dataLength = samplingRate*duration;
	}
	public char getStopAck() {
		return stopAck;
	}
	public void setStopAck(char stopAck) {
		this.stopAck = stopAck;
	}
	public byte getStop() {
		return stop;
	}
	public void setStop(byte stop) {
		this.stop = stop;
	}
	public char getMultiValueDelimiter() {
		return multiValueDelimiter;
	}
	public void setMultiValueDelimiter(char multiValueDelimiter) {
		this.multiValueDelimiter = multiValueDelimiter;
	}
	public int getSensorNum() {
		return sensorNum;
	}
	public void setSensorNum(int sensorNum) {
		this.sensorNum = sensorNum;
	}
	public char getSensorIdentifier() {
		return sensorIdentifier;
	}
	public void setSensorIdentifier(char sensorIdentifier) {
		this.sensorIdentifier = sensorIdentifier;
	}
	public char getSensorDataDelimiter() {
		return sensorDataDelimiter;
	}
	public void setSensorDataDelimiter(char sensorDataDelimiter) {
		this.sensorDataDelimiter = sensorDataDelimiter;
	}
	
}
