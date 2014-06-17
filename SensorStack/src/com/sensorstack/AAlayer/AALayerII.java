package com.sensorstack.AAlayer;

import java.util.HashMap;

import com.sensorstack.ContinuousStreamInfo;
import com.sensorstack.DiscretePacketInfo;
import com.sensorstack.devicelayer.NoDeviceConnectedException;
import com.sensorstack.sensors.BloodPressure;
import com.sensorstack.sensors.DataNotCollectedException;
import com.sensorstack.sensors.ECG;
import com.sensorstack.sensors.GSR;
import com.sensorstack.sensors.Oximeter;
import com.sensorstack.sensors.PulseRate;
import com.sensorstack.sensors.RangeOfValues;
import com.sensorstack.sensors.Sensor;
import com.sensorstack.sensors.SensorData;
import com.sensorstack.sensors.SensorDataRange;
import com.sensorstack.sensors.SensorNotDefinedException;
import com.sensorstack.sensors.Temperature;

/**
 * This layer manages AALayer-I. The application layer must invoke calls to this layer for fetching medical data 
 * from the sensors (i.e. without any metadata). 
 * Note - If you need metadata along with medical data, you must invoke the provenance layer class methods instead,
 * see {@link com.sensorstack.provenancelayer.ProvenanceLayer}
 */
public class AALayerII {
	/**
	 * Defines standard range for Temperature sensor. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect data from any temperature sensor having values mapped to this range.
	 */
	public static final RangeOfValues<Float> STANDARD_TEMPERATURE_RANGE = new RangeOfValues<Float>(0.0f,100.0f);
	/**
	 * Defines standard range for Systolic Blood Pressure values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect any Systolic Blood Pressure data values mapped to this range.
	 */
	public static final RangeOfValues<Integer> STANDARD_SYSTOLIC_RANGE = new RangeOfValues<Integer>(50,250);
	/**
	 * Defines standard range for Diastolic Blood Pressure values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect any Diastolic Blood Pressure data values mapped to this range.
	 */
	public static final RangeOfValues<Integer> STANDARD_DIASTOLIC_RANGE = new RangeOfValues<Integer>(20,150);
	/**
	 * Defines standard range for Pulse Rate values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect data from any Pulse rate sensor having values mapped to this range.
	 */
	public static final RangeOfValues<Integer> STANDARD_PULSERATE_RANGE = new RangeOfValues<Integer>(20,200);
	/**
	 * Defines standard range for Oximeter values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect data from any Oximeter sensor having values mapped to this range.
	 */
	public static final RangeOfValues<Float> STANDARD_OXIMETER_RANGE = new RangeOfValues<Float>(0.0f,100.0f);
	/**
	 * Defines standard range for skin conductance values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect any skin conductance data values mapped to this range.
	 */
	public static final RangeOfValues<Float> STANDARD_GSR_CONDUCTANCE_RANGE = new RangeOfValues<Float>(0.0f,5.0f);
	/**
	 * Defines standard range for skin resistance values. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect any skin resistance data values mapped to this range.
	 */
	public static final RangeOfValues<Float> STANDARD_GSR_RESISTANCE_RANGE = new RangeOfValues<Float>(-100000.0f,1000000.0f);
	/**
	 * Defines standard range for ECG sensor. The values obtained from the hardware as the the hardware
	 * data range are mapped to this range before getting returned.
	 * The application layer should expect any ECG data values mapped to this range.
	 */
	public static final RangeOfValues<Float> STANDARD_ECG_RANGE = new RangeOfValues<Float>(0.0f,5.0f);
	static SensorDataRange hardwareDataRange = new SensorDataRange(
			STANDARD_TEMPERATURE_RANGE,
			STANDARD_SYSTOLIC_RANGE,
			STANDARD_DIASTOLIC_RANGE,
			STANDARD_PULSERATE_RANGE,
			STANDARD_OXIMETER_RANGE,
			STANDARD_GSR_CONDUCTANCE_RANGE,
			STANDARD_GSR_RESISTANCE_RANGE,
			STANDARD_ECG_RANGE
			);
	static DiscretePacketInfo discretePacketInfo = new DiscretePacketInfo();
	static ContinuousStreamInfo continuousStreamInfo = new ContinuousStreamInfo();
	
	/**
	 * Hardware Range defines the valid range for the sensor hardware. This object is initialised 
	 * with standard range for each of the sensors. This function allows user to set this range.
	 * If the range passed for a sensor is null, previous value is retained.
	 * @param range specify range for different sensors
	 */
	public static void setHardwareRange(SensorDataRange range)
	{
		if(range.getTemperatureRange()!=null)
			hardwareDataRange.setTemperatureRange(range.getTemperatureRange());
		if(range.getSystolicRange()!=null)
			hardwareDataRange.setSystolicRange(range.getSystolicRange());
		if(range.getDiastolicRange()!=null)
			hardwareDataRange.setDiastolicRange(range.getDiastolicRange());
		if(range.getPulseRateRange()!=null)
			hardwareDataRange.setPulseRateRange(range.getPulseRateRange());
		if(range.getOximeterRange()!=null)
			hardwareDataRange.setOximeterRange(range.getOximeterRange());
		if(range.getGsrConductanceRange()!=null)
			hardwareDataRange.setGsrConductanceRange(range.getGsrConductanceRange());
		if(range.getGsrResistanceRange()!=null)
			hardwareDataRange.setGsrResistanceRange(range.getGsrResistanceRange());
		if(range.getEcgRange()!=null)
			hardwareDataRange.setEcgRange(range.getEcgRange());
	}
	
	/**
	 * Returns the Hardware range used for validating data values received from sensor hardware.
	 * For more information on Hardware range, see {@link #setHardwareRange(SensorDataRange)} 
	 * @return Hardware range
	 */
	public static SensorDataRange getHardwareDataRange()
	{
		return hardwareDataRange;
	}
	
	/**
	 * Sets the DiscretePacketInfo object. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param discretePacketInfo DiscretePacketInfo class object
	 */
	public static void setDiscretePacketInfo(DiscretePacketInfo discretePacketInfo)
	{
		AALayerII.discretePacketInfo=discretePacketInfo;
	}
	
	/**
	 * Returns the current DiscretePacketInfo object. See {@link com.sensorstack.DiscretePacketInfo}
	 * @return DiscretePacketInfo class object
	 */
	public static DiscretePacketInfo getDiscretePacketInfo()
	{
		return discretePacketInfo;
	}
	
	/**
	 * Sets the ContinuousStreamInfo object. See {@link com.sensorstack.ContinuousStreamInfo}
	 * @param continuousStreamInfo ContinuousStreamInfo class object
	 */
	public static void setContinuousStreamInfo(ContinuousStreamInfo continuousStreamInfo)
	{
		AALayerII.continuousStreamInfo=continuousStreamInfo;
	}
	
	/**
	 * Returns the current ContinuousStreamInfo object. See {@link com.sensorstack.ContinuousStreamInfo}
	 * @return ContinuousStreamInfo class object
	 */
	public static ContinuousStreamInfo getContinuousStreamInfo()
	{
		return continuousStreamInfo;
	}
	
	/**
	 * Fetches a set of discrete sensor values as specified in the discretePacketInfo object.
	 * Uses the current hardware range values for range validation.
	 * @param discretePacketInfo
	 * @return an object of SensorData Class with data values populated as per requirement
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static SensorData getDiscrete(DiscretePacketInfo discretePacketInfo) throws NoDeviceConnectedException, DataNotCollectedException
	{
		AALayerII.discretePacketInfo = discretePacketInfo;
		return getDiscrete();
	}
	
	/**
	 * Fetches a set of discrete sensor values as specified in the discretePacketInfo object, and
	 * range specified by the SensorDataRange object for range validation
	 * @param discretePacketInfo
	 * @param range
	 * @return an object of SensorData Class with data values populated as per requirement
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static SensorData getDiscrete(DiscretePacketInfo discretePacketInfo, SensorDataRange range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		AALayerII.discretePacketInfo = discretePacketInfo;
		return getDiscrete(range);
	}
	
	/**
	 * Fetches a set of discrete sensor values using the range specified by the 
	 * SensorDataRange object for range validation.
	 * Uses the current DiscretePacketInfo values for the sensors.
	 * @param range
	 * @return an object of SensorData Class with data values populated as per requirement
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static SensorData getDiscrete(SensorDataRange range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		setHardwareRange(range);
		return getDiscrete();
	}
	
	/**
	 * Fetches a set of discrete sensor values.
	 * Uses the current DiscretePacketInfo values for the sensors and
	 * Uses the current hardware range values for range validation.
	 * @return an object of SensorData Class with data values populated as per requirement
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static SensorData getDiscrete() throws NoDeviceConnectedException, DataNotCollectedException
	{
		SensorData sd = AALayerI.getDiscrete();
		if(sd==null)
			return null;
		
		boolean reqt[]=discretePacketInfo.getRequirement();
		if(reqt[Sensor.TEMPERATURE])
		{ 
			Temperature temp=sd.getTemperature();
			if(temp!=null&&!STANDARD_TEMPERATURE_RANGE.equals(hardwareDataRange.getTemperatureRange()))				
				temp.setValue(mapValue(temp.getValue(), hardwareDataRange.getTemperatureRange(), STANDARD_TEMPERATURE_RANGE));
		}
		if(reqt[Sensor.BLOODPRESSURE])
		{ 
			BloodPressure bp = sd.getBloodPressure();
			if(bp!=null&&!STANDARD_DIASTOLIC_RANGE.equals(hardwareDataRange.getDiastolicRange()))
				bp.setDiastolicValue(mapValue(bp.getDiastolicValue(), hardwareDataRange.getDiastolicRange(), STANDARD_DIASTOLIC_RANGE));
			if(bp!=null&&!STANDARD_SYSTOLIC_RANGE.equals(hardwareDataRange.getSystolicRange()))
				bp.setSystolicValue(mapValue(bp.getSystolicValue(), hardwareDataRange.getSystolicRange(), STANDARD_SYSTOLIC_RANGE));
		}
		if(reqt[Sensor.PULSERATE])
		{
			PulseRate pr = sd.getPulseRate();
			if(pr!=null&&!STANDARD_PULSERATE_RANGE.equals(hardwareDataRange.getPulseRateRange()))
				pr.setValue(mapValue(pr.getValue(), hardwareDataRange.getPulseRateRange(), STANDARD_PULSERATE_RANGE));
			
		}
		if(reqt[Sensor.OXIMETER])
		{
			Oximeter oximeter = sd.getOximeter();
			if(oximeter!=null&&!STANDARD_OXIMETER_RANGE.equals(hardwareDataRange.getOximeterRange()))
				oximeter.setValue(mapValue(oximeter.getValue(), hardwareDataRange.getOximeterRange(), STANDARD_OXIMETER_RANGE));			
		}
		if(reqt[Sensor.GSR])
		{
			GSR gsr = sd.getGsr();
			if(gsr!=null&&!STANDARD_GSR_CONDUCTANCE_RANGE.equals(hardwareDataRange.getGsrConductanceRange()))
				gsr.setConductanceValue(mapValue(gsr.getConductanceValue(), hardwareDataRange.getGsrConductanceRange(), STANDARD_GSR_CONDUCTANCE_RANGE));
			if(gsr!=null&&!STANDARD_GSR_RESISTANCE_RANGE.equals(hardwareDataRange.getGsrResistanceRange()))
				gsr.setResistanceValue(mapValue(gsr.getResistanceValue(), hardwareDataRange.getGsrResistanceRange(), STANDARD_GSR_RESISTANCE_RANGE));
		}
		
		return sd;
	}
	
	/**
	 * Fetches a stream of data values from the ECG sensor as specified in the ContinuousStreamInfo and validating
	 * the values received against the range specified.
	 * @param c an object of ContinuousStreamInfo class
	 * @param range valid range for ECG sensor hardware
	 * @return an object of ECG Class with populated data values
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static ECG getECG(ContinuousStreamInfo c,RangeOfValues<Float> range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		hardwareDataRange.setEcgRange(range);
		return getECG(c);
	}
	
	/**
	 * Fetches a stream of data values from the ECG sensor validating the values received against the range specified.
	 * Uses the current ContinuousStreamInfo values for the ECG sensor
	 * @param range valid range for ECG sensor hardware
	 * @return an object of ECG Class with populated data values
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static ECG getECG(RangeOfValues<Float> range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		hardwareDataRange.setEcgRange(range);
		return getECG();
	}
	
	/**
	 * Fetches a stream of data values from the ECG sensor as specified in the ContinuousStreamInfo.
	 * Uses the current hardware range values for range validation.
	 * @param c an object of ContinuousStreamInfo class
	 * @return an object of ECG Class with populated data values
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static ECG getECG(ContinuousStreamInfo c) throws NoDeviceConnectedException, DataNotCollectedException
	{
		AALayerII.continuousStreamInfo=c;
		return getECG();
	}
	
	/**
	 * Fetches a stream of data values from the ECG sensor.
	 * Uses the current ContinuousStreamInfo values for the ECG sensor
	 * Uses the current hardware range values for range validation.
	 * @return an object of ECG Class with populated data values
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static ECG getECG() throws NoDeviceConnectedException, DataNotCollectedException
	{
		ECG ecg= AALayerI.getECG();
		if(ecg==null)
			return ecg;
		if(!STANDARD_ECG_RANGE.equals(hardwareDataRange.getEcgRange()))
			ecg.setValueArray(mapValue(ecg.getValueArray(), hardwareDataRange.getEcgRange(), STANDARD_ECG_RANGE));
		return ecg;
	}
	
	/**
	 * Fetches medical data from the Temperature sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param range valid range for Temperature sensor hardware
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setTemperatureRange(range);
		return getTemperature();
	}
	
	/**
	 * Fetches medical data from the Temperature sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature() throws NoDeviceConnectedException
	{
		try {
			return getTemperature(String.valueOf(discretePacketInfo.getSensorIdentifier(Sensor.TEMPERATURE)), String.valueOf(discretePacketInfo.getSensorDataDelimiter()), discretePacketInfo.getDiscreteTimeout());
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fetches medical data from the Temperature sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Temperature sensor hardware
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(String sensorId, String delimiter, RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setTemperatureRange(range);
		return getTemperature(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Temperature sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(String sensorId, String delimiter) throws NoDeviceConnectedException
	{
		return getTemperature(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Temperature sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Temperature sensor hardware
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(String sensorId, String delimiter, RangeOfValues<Float> range, int timeout) throws NoDeviceConnectedException
	{
		hardwareDataRange.setTemperatureRange(range);
		return getTemperature(sensorId, delimiter, timeout);
	}
	
	/**
	 * Fetches medical data from the Temperature sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of Temperature Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		Temperature temp = AALayerI.getTemperature(sensorId, delimiter, timeout);
		if(temp==null)
			return temp;
		
		if(!STANDARD_TEMPERATURE_RANGE.equals(hardwareDataRange.getTemperatureRange()))
			temp.setValue(mapValue(temp.getValue(), hardwareDataRange.getTemperatureRange(), STANDARD_TEMPERATURE_RANGE));
		return temp;
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sysrange valid range for Systolic Blood Pressure
	 * @param dirange valid range for Diastolic Blood Pressure
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(RangeOfValues<Integer> sysrange, RangeOfValues<Integer> dirange) throws NoDeviceConnectedException
	{
		hardwareDataRange.setSystolicRange(sysrange);
		hardwareDataRange.setDiastolicRange(dirange);
		return getBloodPressure();
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure() throws NoDeviceConnectedException
	{
		try {
			return getBloodPressure(String.valueOf(discretePacketInfo.getSensorIdentifier(Sensor.BLOODPRESSURE)), String.valueOf(discretePacketInfo.getSensorDataDelimiter()), String.valueOf(discretePacketInfo.getMultiValueDelimiter()), discretePacketInfo.getDiscreteTimeout());
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(String sensorId, String dataDelimiter, String multiValueDelimiter) throws NoDeviceConnectedException
	{
		return getBloodPressure(sensorId, dataDelimiter, multiValueDelimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param sysrange valid range for Systolic Blood Pressure
	 * @param dirange valid range for Diastolic Blood Pressure
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(String sensorId, String dataDelimiter, String multiValueDelimiter, RangeOfValues<Integer> sysrange, RangeOfValues<Integer> dirange, int timeout) throws NoDeviceConnectedException
	{
		hardwareDataRange.setSystolicRange(sysrange);
		hardwareDataRange.setDiastolicRange(dirange);
		return getBloodPressure(sensorId, dataDelimiter, multiValueDelimiter, timeout);
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param sysrange valid range for Systolic Blood Pressure
	 * @param dirange valid range for Diastolic Blood Pressure
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(String sensorId, String dataDelimiter, String multiValueDelimiter, RangeOfValues<Integer> sysrange, RangeOfValues<Integer> dirange) throws NoDeviceConnectedException
	{
		hardwareDataRange.setSystolicRange(sysrange);
		hardwareDataRange.setDiastolicRange(dirange);
		return getBloodPressure(sensorId, dataDelimiter, multiValueDelimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Blood Pressure sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of BloodPressure Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(String sensorId, String dataDelimiter, String multiValueDelimiter, int timeout) throws NoDeviceConnectedException
	{
		BloodPressure bp = AALayerI.getBloodPressure(sensorId, dataDelimiter, multiValueDelimiter, timeout);
		if(bp==null)
			return bp;
		
		if(!STANDARD_DIASTOLIC_RANGE.equals(hardwareDataRange.getDiastolicRange()))
			bp.setDiastolicValue(mapValue(bp.getDiastolicValue(), hardwareDataRange.getDiastolicRange(), STANDARD_DIASTOLIC_RANGE));
		if(!STANDARD_SYSTOLIC_RANGE.equals(hardwareDataRange.getSystolicRange()))
			bp.setSystolicValue(mapValue(bp.getSystolicValue(), hardwareDataRange.getSystolicRange(), STANDARD_SYSTOLIC_RANGE));
		return bp;
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param range valid range for Pulse Rate sensor hardware
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(RangeOfValues<Integer> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setPulseRateRange(range);
		return getPulseRate();
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate() throws NoDeviceConnectedException
	{
		try {
			return getPulseRate(String.valueOf(discretePacketInfo.getSensorIdentifier(Sensor.PULSERATE)), String.valueOf(discretePacketInfo.getSensorDataDelimiter()), discretePacketInfo.getDiscreteTimeout());
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(String sensorId, String delimiter) throws NoDeviceConnectedException
	{
		return getPulseRate(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Pulse Rate sensor hardware
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(String sensorId, String delimiter, RangeOfValues<Integer> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setPulseRateRange(range);
		return getPulseRate(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Pulse Rate sensor hardware
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(String sensorId, String delimiter, RangeOfValues<Integer> range, int timeout) throws NoDeviceConnectedException
	{
		hardwareDataRange.setPulseRateRange(range);
		return getPulseRate(sensorId, delimiter, timeout);
	}
	
	/**
	 * Fetches medical data from the Pulse Rate sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of PulseRate Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		PulseRate pr= AALayerI.getPulseRate(sensorId, delimiter, timeout);
		if(pr==null)
			return pr;
		
		if(!STANDARD_PULSERATE_RANGE.equals(hardwareDataRange.getPulseRateRange()))
			pr.setValue(mapValue(pr.getValue(), hardwareDataRange.getPulseRateRange(), STANDARD_PULSERATE_RANGE));
		return pr;
	}
	
	/**
	 * Fetches medical data from the Oximter sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param range valid range for Oximeter sensor hardware
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setOximeterRange(range);
		return getOximeter();
	}
	
	/**
	 * Fetches medical data from the Oximeter sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter() throws NoDeviceConnectedException
	{
		try {
			return getOximeter(String.valueOf(discretePacketInfo.getSensorIdentifier(Sensor.OXIMETER)), String.valueOf(discretePacketInfo.getSensorDataDelimiter()), discretePacketInfo.getDiscreteTimeout());
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fetches medical data from the Oximeter sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Oximeter sensor hardware
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(String sensorId, String delimiter, RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		hardwareDataRange.setOximeterRange(range);
		return getOximeter(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Oximeter sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(String sensorId, String delimiter) throws NoDeviceConnectedException
	{
		return getOximeter(sensorId, delimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the Oximeter sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param range valid range for Oximeter sensor hardware
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(String sensorId, String delimiter, RangeOfValues<Float> range, int timeout) throws NoDeviceConnectedException
	{
		hardwareDataRange.setOximeterRange(range);
		return getOximeter(sensorId, delimiter, timeout);
	}
	
	/**
	 * Fetches medical data from the Oximeter sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo objec
	 * @param sensorId sensor identifiers in the response packet
	 * @param delimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of Oximeter Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		Oximeter oximeter= AALayerI.getOximeter(sensorId, delimiter, timeout);
		if(oximeter==null)
			return oximeter;
		
		if(!STANDARD_OXIMETER_RANGE.equals(hardwareDataRange.getOximeterRange()))
			oximeter.setValue(mapValue(oximeter.getValue(), hardwareDataRange.getOximeterRange(), STANDARD_OXIMETER_RANGE));
		return oximeter;
	}
	
	/**
	 * Fetches medical data from the GSR sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param conductanceRange valid range for Skin Conductance
	 * @param resistanceRange valid range for Skin Resistance
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(RangeOfValues<Float> conductanceRange, RangeOfValues<Float> resistanceRange) throws NoDeviceConnectedException
	{
		hardwareDataRange.setGsrConductanceRange(conductanceRange);
		hardwareDataRange.setGsrResistanceRange(resistanceRange);
		return getGSR();
	}
	
	/**
	 * Fetches medical data from the GSR sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR() throws NoDeviceConnectedException
	{
		try {
			return getGSR(String.valueOf(discretePacketInfo.getSensorIdentifier(Sensor.GSR)), String.valueOf(discretePacketInfo.getSensorDataDelimiter()), String.valueOf(discretePacketInfo.getMultiValueDelimiter()), discretePacketInfo.getDiscreteTimeout());
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fetches medical data from the GSR sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(String sensorId, String dataDelimiter, String multiValueDelimiter) throws NoDeviceConnectedException
	{
		return getGSR(sensorId, dataDelimiter, multiValueDelimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the GSR sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param conductanceRange valid range for Skin Conductance
	 * @param resistanceRange valid range for Skin Resistance
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(String sensorId, String dataDelimiter, String multiValueDelimiter, RangeOfValues<Float> conductanceRange, RangeOfValues<Float> resistanceRange, int timeout) throws NoDeviceConnectedException
	{
		hardwareDataRange.setGsrConductanceRange(conductanceRange);
		hardwareDataRange.setGsrResistanceRange(resistanceRange);
		return getGSR(sensorId, dataDelimiter, multiValueDelimiter, timeout);
	}
	
	/**
	 * Fetches medical data from the GSR sensor using the specified range for data validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param conductanceRange valid range for Skin Conductance
	 * @param resistanceRange valid range for Skin Resistance
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(String sensorId, String dataDelimiter, String multiValueDelimiter, RangeOfValues<Float> conductanceRange, RangeOfValues<Float> resistanceRange) throws NoDeviceConnectedException
	{
		hardwareDataRange.setGsrConductanceRange(conductanceRange);
		hardwareDataRange.setGsrResistanceRange(resistanceRange);
		return getGSR(sensorId, dataDelimiter, multiValueDelimiter, discretePacketInfo.getDiscreteTimeout());
	}
	
	/**
	 * Fetches medical data from the GSR sensor.
	 * Uses the current hardware range values for range validation.
	 * Rest of the specification is used from current DiscretePacketInfo object.
	 * @param sensorId sensor identifiers in the response packet.
	 * @param dataDelimiter used to delimit beginning of data values from the sensor identifier in the response packet
	 * @param multiValueDelimiter used to delimit multiple values sent by the sensor in the response packet
	 * @param timeout Timeout Duration (in milliseconds) till the fetch request waits for the hardware 
	 * to complete transmission.
	 * @return an object of GSR Class with populated data values
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(String sensorId, String dataDelimiter, String multiValueDelimiter, int timeout) throws NoDeviceConnectedException
	{
		GSR gsr = AALayerI.getGSR(sensorId, dataDelimiter, multiValueDelimiter, timeout);
		if(gsr==null)
			return gsr;
		
		if(!STANDARD_GSR_CONDUCTANCE_RANGE.equals(hardwareDataRange.getGsrConductanceRange()))
			gsr.setConductanceValue(mapValue(gsr.getConductanceValue(), hardwareDataRange.getGsrConductanceRange(), STANDARD_GSR_CONDUCTANCE_RANGE));
		if(!STANDARD_GSR_RESISTANCE_RANGE.equals(hardwareDataRange.getGsrResistanceRange()))
			gsr.setResistanceValue(mapValue(gsr.getResistanceValue(), hardwareDataRange.getGsrResistanceRange(), STANDARD_GSR_RESISTANCE_RANGE));
		return gsr;
	}
	
	
	private static float mapValue(float value, RangeOfValues<Float> input, RangeOfValues<Float> output)
	{
		float inputRange = input.getUpperLimit() - input.getLowerLimit();
		float outputRange = output.getUpperLimit()-output.getLowerLimit();
		float newValue = output.getLowerLimit() + (value-input.getLowerLimit())*outputRange/inputRange;
		return newValue;
	}
	
	private static int mapValue(int value, RangeOfValues<Integer> input, RangeOfValues<Integer> output)
	{
		int inputRange = input.getUpperLimit() - input.getLowerLimit();
		int outputRange = output.getUpperLimit()-output.getLowerLimit();
		int newValue = output.getLowerLimit() + (int)((value-input.getLowerLimit())*(float)outputRange/inputRange);
		return newValue;
	}
	
	private static int[] mapValue(int value[], RangeOfValues<Integer> input, RangeOfValues<Integer> output)
	{
		int inputRange = input.getUpperLimit() - input.getLowerLimit();
		int outputRange = output.getUpperLimit()-output.getLowerLimit();
		int outputLL = output.getLowerLimit();
		int inputLL = input.getLowerLimit();
		int newValue[] = new int[value.length];
		for(int i=0; i<value.length; i++)
			newValue[i] = outputLL + (int)((value[i]-inputLL)*(float)outputRange/inputRange);
		return newValue;
	}
	
	private static Float[] mapValue(Float[] value, RangeOfValues<Float> input, RangeOfValues<Float> output)
	{
		float inputRange = input.getUpperLimit() - input.getLowerLimit();
		float outputRange = output.getUpperLimit()-output.getLowerLimit();
		float outputLL = output.getLowerLimit();
		float inputLL = input.getLowerLimit();
		Float newValue[] = new Float[value.length];
		for(int i=0; i<value.length; i++)
			newValue[i] = outputLL + (value[i]-inputLL)*outputRange/inputRange;
		return newValue;
	}
	
	/**
	 * Fetches metadata values from the sensor hardware.
	 * @param sensorNum Indicates the sensor whose metadata is requested
	 * @param metadata Defines what is needed
	 * @throws NoDeviceConnectedException
	 * @throws SensorNotDefinedException
	 */
	public static void getMetadata(int sensorNum, HashMap<String, String> metadata) throws NoDeviceConnectedException, SensorNotDefinedException
	{
		if(sensorNum < Sensor.NUM_DISCRETE)
		{
			AALayerI.getMetadata(sensorNum, 
				metadata, 
				String.valueOf(discretePacketInfo.getSensorIdentifier(sensorNum)), 
				String.valueOf(discretePacketInfo.getSensorDataDelimiter()), 
				String.valueOf(discretePacketInfo.getMultiValueDelimiter()),
				discretePacketInfo.getDiscreteTimeout());
		}
		else
		{
			AALayerI.getMetadata(sensorNum, 
					metadata, 
					String.valueOf(continuousStreamInfo.getSensorIdentifier()), 
					String.valueOf(continuousStreamInfo.getSensorDataDelimiter()), 
					String.valueOf(continuousStreamInfo.getMultiValueDelimiter()),
					1000);
		}
	}
}