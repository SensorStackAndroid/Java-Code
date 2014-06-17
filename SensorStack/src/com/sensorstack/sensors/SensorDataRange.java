package com.sensorstack.sensors;

/**
 * Encapsulates range objects for all supported sensors
 */
public class SensorDataRange {
	private RangeOfValues<Float> temperatureRange;
	private RangeOfValues<Integer> systolicRange;
	private RangeOfValues<Integer> diastolicRange;
	private RangeOfValues<Integer> pulseRateRange;
	private RangeOfValues<Float> oximeterRange;
	private RangeOfValues<Float> gsrConductanceRange;
	private RangeOfValues<Float> gsrResistanceRange;
	private RangeOfValues<Float> ecgRange;
	
	/**
	 * Constructor to initialise data members with default values 
	 */
	public SensorDataRange()
	{
		RangeOfValues<Float> range = new RangeOfValues<Float>(0f,100f);
		setTemperatureRange(range);
		setOximeterRange(range);
		setGsrConductanceRange(new RangeOfValues<Float>(-100000f,100000f));
		setGsrResistanceRange(new RangeOfValues<Float>(-100000f,100000f));
		
		setEcgRange(new RangeOfValues<Float>(1f,2f));
		RangeOfValues<Integer> intrange = new RangeOfValues<Integer>(0,100);
		setPulseRateRange(intrange);
		setDiastolicRange(intrange);
		setSystolicRange(intrange);
	}

	/**
	 * Constructor to initialise data members with specified values 
	 */
	public SensorDataRange(RangeOfValues<Float> temperatureRange, 
			RangeOfValues<Integer> systolicRange, RangeOfValues<Integer> diastolicRange, 
			RangeOfValues<Integer> pulseRateRange, RangeOfValues<Float> oximeterRange,  
			RangeOfValues<Float> gsrConductanceRange, RangeOfValues<Float> gsrResistanceRange,
			RangeOfValues<Float> ecgRange)
	{
		this.temperatureRange=temperatureRange;
		this.systolicRange=systolicRange;
		this.diastolicRange=diastolicRange;
		this.pulseRateRange=pulseRateRange;
		this.oximeterRange=oximeterRange;
		this.gsrConductanceRange=gsrConductanceRange;
		this.gsrResistanceRange=gsrResistanceRange;
		this.ecgRange=ecgRange;
	}
	
	public RangeOfValues<Float> getTemperatureRange() {
		return temperatureRange;
	}
	public void setTemperatureRange(RangeOfValues<Float> temperatureRange) {
		this.temperatureRange = temperatureRange;
	}
	public RangeOfValues<Integer> getSystolicRange() {
		return systolicRange;
	}
	public void setSystolicRange(RangeOfValues<Integer> systolicRange) {
		this.systolicRange = systolicRange;
	}
	public RangeOfValues<Integer> getDiastolicRange() {
		return diastolicRange;
	}
	public void setDiastolicRange(RangeOfValues<Integer> diastolicRange) {
		this.diastolicRange = diastolicRange;
	}
	public RangeOfValues<Integer> getPulseRateRange() {
		return pulseRateRange;
	}
	public void setPulseRateRange(RangeOfValues<Integer> pulseRateRange) {
		this.pulseRateRange = pulseRateRange;
	}
	public RangeOfValues<Float> getOximeterRange() {
		return oximeterRange;
	}
	public void setOximeterRange(RangeOfValues<Float> oximeterRange) {
		this.oximeterRange = oximeterRange;
	}
	public RangeOfValues<Float> getGsrConductanceRange() {
		return gsrConductanceRange;
	}
	public void setGsrConductanceRange(RangeOfValues<Float> gsrConductanceRange) {
		this.gsrConductanceRange = gsrConductanceRange;
	}
	public RangeOfValues<Float> getGsrResistanceRange() {
		return gsrResistanceRange;
	}
	public void setGsrResistanceRange(RangeOfValues<Float> gsrResistanceRange) {
		this.gsrResistanceRange = gsrResistanceRange;
	}
	public RangeOfValues<Float> getEcgRange() {
		return ecgRange;
	}
	public void setEcgRange(RangeOfValues<Float> ecgRange) {
		this.ecgRange = ecgRange;
	}
}
