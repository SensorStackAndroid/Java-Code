package com.sensorstack.sensors;

import com.sensorstack.sensors.BloodPressure;
import com.sensorstack.sensors.ECG;
import com.sensorstack.sensors.GSR;
import com.sensorstack.sensors.Oximeter;
import com.sensorstack.sensors.PulseRate;
import com.sensorstack.sensors.Sensor;
import com.sensorstack.sensors.SensorNotDefinedException;
import com.sensorstack.sensors.Temperature;
/**
 * Encapsulates various discrete sensor objects
 */
public class SensorData {
	private Temperature temperature;
	private BloodPressure bloodPressure;
	private PulseRate pulseRate; 
	private Oximeter oximeter;
	private GSR gsr;
	private boolean response[] = new boolean[Sensor.NUM_DISCRETE];
	
	public SensorData() {
		for(int i=0;i<Sensor.NUM_DISCRETE;i++)
			response[i]=false;
	}
	
	public Temperature getTemperature() throws DataNotCollectedException
	{
		if(response[Sensor.TEMPERATURE])
			return temperature;
		else
			throw new DataNotCollectedException("Temperature Sensor Not Available");
	}
	
	/**
	 * Returns a boolean flag indicating whether a particular discrete sensor data has been set
	 * @param sensor Integer referring to index of a particular discrete sensor as defined in {@link Sensor}
	 * @return true if instance of the corresponding discrete sensor have been populated, false otherwise
	 * @throws SensorNotDefinedException
	 */
	public boolean getResponse(int sensor) throws SensorNotDefinedException
	{
		if(sensor>=0&&sensor<Sensor.NUM_DISCRETE)
			return response[sensor];
		else
			throw new SensorNotDefinedException("INVALID SENSOR - No Discrete Sensor exists with such Id.");
	}
	
	/**
	 * Returns a boolean array flagging discrete sensors whose instances have been populated.
	 * @return response array
	 */
	public boolean[] getResponse()
	{
		return response;
	}
	
	public void setTemperature(Temperature temp) throws SensorNotDefinedException
	{
		response[Sensor.TEMPERATURE] = true;
		temperature=temp;
	}
	
	public BloodPressure getBloodPressure() throws DataNotCollectedException {
		if(response[Sensor.BLOODPRESSURE])
			return bloodPressure;
		throw new DataNotCollectedException("Blood Pressure Sensor Not Available");
	}

	public void setBloodPressure(BloodPressure bloodpressure) {
		response[Sensor.BLOODPRESSURE] = true;
		this.bloodPressure = bloodpressure;
	}

	public PulseRate getPulseRate() throws DataNotCollectedException {
		if(response[Sensor.PULSERATE])
			return pulseRate;
		throw new DataNotCollectedException("Pulse Rate Sensor Not Available");
	}

	public void setPulseRate(PulseRate pulserate) {
		response[Sensor.PULSERATE] = true;
		this.pulseRate = pulserate;
	}

	public Oximeter getOximeter() throws DataNotCollectedException {
		if(response[Sensor.OXIMETER])
			return oximeter;
		throw new DataNotCollectedException("Oximeter Sensor Not Available");
	}

	public void setOximeter(Oximeter oximeter) {
		response[Sensor.OXIMETER] = true;
		this.oximeter = oximeter;
	}

	public GSR getGsr() throws DataNotCollectedException {
		if(response[Sensor.GSR])
			return gsr;
		throw new DataNotCollectedException("GSR Sensor Not Available");
	}

	public void setGsr(GSR gsr) {
		response[Sensor.GSR] = true;
		this.gsr = gsr;
	}
}
