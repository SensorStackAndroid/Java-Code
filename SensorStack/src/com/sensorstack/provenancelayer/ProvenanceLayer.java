package com.sensorstack.provenancelayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.sensorstack.ContinuousStreamInfo;
import com.sensorstack.DiscretePacketInfo;
import com.sensorstack.AAlayer.AALayerII;
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
 * Supports fetching of complete data objects which includes:
 * 1) medical data value from sensors
 * 2) metadata information from sensors
 * 3) GPS location and timestamp from the system
 */
public class ProvenanceLayer {
	static final String SENSORIDKEY = "SensorId";
	static final String MANUFACTURERKEY = "Manufacturer";
	private static HashMap<String, String> temperatureMetadata=null;
	private static HashMap<String, String> bloodPressureMetadata=null;
	private static HashMap<String, String> pulseRateMetadata=null;
	private static HashMap<String, String> oximeterMetadata=null;
	private static HashMap<String, String> GSRMetadata=null;
	private static HashMap<String, String> ECGMetadata=null;
	
	private static GPSLocation getGPS(Context context){	 
		final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
	    if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
			Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(location!=null)
			{
				GPSLocation gps = new GPSLocation();
				gps.setLatitude((float) location.getLatitude());
				gps.setLongitude((float) location.getLongitude());
				return gps;
			}
	    }
	    return null;
	}
	
	private static HashMap<String, String> getMetadata(int sensorId, String variableMetadata[]) throws NoDeviceConnectedException
	{
		HashMap<String, String> metadata = new HashMap<String, String>();
		metadata.put(SENSORIDKEY, null);
		metadata.put(MANUFACTURERKEY, null);
		if(variableMetadata != null)	
		{
			for(int i=0; i<variableMetadata.length; i++)
				metadata.put(variableMetadata[i], null);
		}
		try {
			AALayerII.getMetadata(sensorId,metadata);
		} catch (SensorNotDefinedException e) {
			e.printStackTrace();
		}
		return metadata;
	}
	
	/**
	 * Used to fetch Temperature sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param range Specifies the valid range of sensor hardware. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of Temperature class. See {@link com.sensorstack.sensors.Temperature} 
	 * @throws NoDeviceConnectedException
	 */
	public static Temperature getTemperature(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag)
			temperatureMetadata = getMetadata(Sensor.TEMPERATURE, variableMetadata);
		
		//get medical data
		Temperature temp;
		if(range != null)
			temp = AALayerII.getTemperature(range);
		else
			temp = AALayerII.getTemperature();
		//add metadata
		temp.setMetadataMap(temperatureMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		temp.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		temp.setGps(getGPS(context));
		
		return temp;
	}
	
	/**
	 * Used to fetch Blood Pressure sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param sysRange Specifies the valid range of sensor hardware for Systolic Pressure. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @param diasRange Specifies the valid range of sensor hardware for Diastolic Pressure. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of BloodPressure class. See {@link com.sensorstack.sensors.BloodPressure} 
	 * @throws NoDeviceConnectedException
	 */
	public static BloodPressure getBloodPressure(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			RangeOfValues<Integer> sysRange, RangeOfValues<Integer> diasRange) 
					throws NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag)
			bloodPressureMetadata = getMetadata(Sensor.BLOODPRESSURE, variableMetadata);
		
		//get medical data
		BloodPressure bp;
		if(sysRange != null && diasRange != null)
			bp = AALayerII.getBloodPressure(sysRange, diasRange);
		else
			bp = AALayerII.getBloodPressure();
		//add metadata
		bp.setMetadataMap(bloodPressureMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		bp.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		bp.setGps(getGPS(context));
		
		return bp;
	}
	
	/**
	 * Used to fetch Pulse Rate sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param range Specifies the valid range of sensor hardware. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of PulseRate class. See {@link com.sensorstack.sensors.PulseRate} 
	 * @throws NoDeviceConnectedException
	 */
	public static PulseRate getPulseRate(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			RangeOfValues<Integer> range) throws NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag || pulseRateMetadata == null)
			pulseRateMetadata = getMetadata(Sensor.PULSERATE, variableMetadata);
		
		//get medical data
		PulseRate pulseRate;
		if(range != null)
			pulseRate = AALayerII.getPulseRate(range);
		else
			pulseRate = AALayerII.getPulseRate();
		//add metadata
		pulseRate.setMetadataMap(pulseRateMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		pulseRate.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		pulseRate.setGps(getGPS(context));
		
		return pulseRate;
	}
	
	/**
	 * Used to fetch Oximeter sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param range Specifies the valid range of sensor hardware. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of Oximeter class. See {@link com.sensorstack.sensors.Oximeter} 
	 * @throws NoDeviceConnectedException
	 */
	public static Oximeter getOximeter(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			RangeOfValues<Float> range) throws NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag)
			oximeterMetadata = getMetadata(Sensor.OXIMETER, variableMetadata);
		
		//get medical data
		Oximeter oximeter;
		if(range != null)
			oximeter = AALayerII.getOximeter(range);
		else
			oximeter = AALayerII.getOximeter();
		//add metadata
		oximeter.setMetadataMap(oximeterMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		oximeter.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		oximeter.setGps(getGPS(context));
		
		return oximeter;
	}

	/**
	 * Used to fetch GSR sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param conductanceRange Specifies the valid range of sensor hardware for skin conductance. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @param resistanceRange Specifies the valid range of sensor hardware for skin resistance. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of GSR class. See {@link com.sensorstack.sensors.GSR} 
	 * @throws NoDeviceConnectedException
	 */
	public static GSR getGSR(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			RangeOfValues<Float> conductanceRange, RangeOfValues<Float> resistanceRange) 
					throws NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag)
			GSRMetadata = getMetadata(Sensor.GSR, variableMetadata);
		
		//get medical data
		GSR gsr;
		if(conductanceRange != null && resistanceRange != null)
			gsr = AALayerII.getGSR(conductanceRange, resistanceRange);
		else
			gsr = AALayerII.getGSR();
		//add metadata
		gsr.setMetadataMap(GSRMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		gsr.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		gsr.setGps(getGPS(context));
		
		return gsr;
	}
	
	/**
	 * Used to fetch ECG sensor data value along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param continuousStreamInfo Required to fetch and unpack the data received. See {@link com.sensorstack.ContinuousStreamInfo}
	 * @param range Specifies the valid range of sensor hardware. See {@link com.sensorstack.sensors.RangeOfValues}
	 * @return Fully populated instance of ECG class. See {@link com.sensorstack.sensors.ECG} 
	 * @throws NoDeviceConnectedException
	 * @throws DataNotCollectedException
	 */
	public static ECG getECG(Context context,boolean flag, 
			String variableMetadata[], ContinuousStreamInfo continuousStreamInfo, 
			RangeOfValues<Float> range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		if(continuousStreamInfo != null)
			AALayerII.setContinuousStreamInfo(continuousStreamInfo);
		
		//get metadata
		if(flag)
			ECGMetadata = getMetadata(Sensor.ECG, variableMetadata);
		
		//get medical data
		ECG ecg;
		if(range != null)
			ecg = AALayerII.getECG(range);
		else
			ecg = AALayerII.getECG();
		//add metadata
		ecg.setMetadataMap(ECGMetadata);
		
		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		ecg.setTimestamp(new Timestamp(date,time));
		
		//get GPSLocation
		ecg.setGps(getGPS(context));
		
		return ecg;
	}
	
	/**
	 * Used to fetch discrete sensor data values along with metadata
	 * @param context Context of the app
	 * @param flag If set, a new request to re-fetch metadata from the h/w is made otherwise last fetched metadata is used  
	 * @param variableMetadata Specifies which additional metadata parameters are needed to be fetched from the sensors other than the mandatory sensorID and manufacturerID
	 * @param discretePacketInfo Required to fetch and unpack the data received. See {@link com.sensorstack.DiscretePacketInfo}
	 * @param range Specifies the valid range of sensors' hardware. See {@link com.sensorstack.sensors.SensorDataRange}
	 * @return Fully populated instance of GSR class. See {@link com.sensorstack.sensors.GSR} 
	 * @throws DataNotCollectedException
	 * @throws NoDeviceConnectedException	
	 */
	public static SensorData getDiscrete(Context context,boolean flag, 
			String variableMetadata[], DiscretePacketInfo discretePacketInfo, 
			SensorDataRange range) throws DataNotCollectedException, NoDeviceConnectedException
	{
		if(discretePacketInfo != null)
			AALayerII.setDiscretePacketInfo(discretePacketInfo);
		
		//get metadata
		if(flag)
		{
			boolean req[] = discretePacketInfo.getRequirement();
			for(int i=0; i<Sensor.NUM_DISCRETE; i++)
			{
				if(req[i])
				{
					switch(i)
					{
						case Sensor.TEMPERATURE:
							temperatureMetadata = getMetadata(i, variableMetadata);
							break;
						case Sensor.BLOODPRESSURE:
							bloodPressureMetadata = getMetadata(i, variableMetadata);
							break;
						case Sensor.PULSERATE:
							pulseRateMetadata = getMetadata(i, variableMetadata);
							break;
						case Sensor.OXIMETER:
							oximeterMetadata = getMetadata(i, variableMetadata);
							break;
						case Sensor.GSR:
							GSRMetadata = getMetadata(i, variableMetadata);
							break;
					}
				}
			}
		}
			
		//get medical data
		SensorData sensorData;
		if(range != null)
			sensorData = AALayerII.getDiscrete(range);
		else
			sensorData = AALayerII.getDiscrete();

		//get Timestamp
		Date now = new Date();
		String date  = new SimpleDateFormat("dd-MM-yyyy").format(now);
		String time  = new SimpleDateFormat("hh:mm:ss a z").format(now);
		Timestamp timestamp = new Timestamp(date,time);
		
		//get GPSLocation
		GPSLocation loc = getGPS(context);
		
		//add provenance data
		boolean res[] = sensorData.getResponse();
		for(int i=0; i<Sensor.NUM_DISCRETE; i++)
		{
			if(res[i])
			{
				switch(i)
				{
					case Sensor.TEMPERATURE:
						Temperature temp = sensorData.getTemperature();
						temp.setMetadataMap(temperatureMetadata);
						temp.setTimestamp(timestamp);
						temp.setGps(loc);
						break;
					case Sensor.BLOODPRESSURE:
						BloodPressure bp = sensorData.getBloodPressure();
						bp.setMetadataMap(bloodPressureMetadata);
						bp.setTimestamp(timestamp);
						bp.setGps(loc);
						break;
					case Sensor.PULSERATE:
						PulseRate pulseRate = sensorData.getPulseRate();
						pulseRate.setMetadataMap(pulseRateMetadata);
						pulseRate.setTimestamp(timestamp);
						pulseRate.setGps(loc);
						break;
					case Sensor.OXIMETER:
						Oximeter oximeter = sensorData.getOximeter();
						oximeter.setMetadataMap(oximeterMetadata);
						oximeter.setTimestamp(timestamp);
						oximeter.setGps(loc);
						break;
					case Sensor.GSR:
						GSR gsr = sensorData.getGsr();
						gsr.setMetadataMap(GSRMetadata);
						gsr.setTimestamp(timestamp);
						gsr.setGps(loc);
						break;
				}
			}
		}
		return sensorData;
	}
}