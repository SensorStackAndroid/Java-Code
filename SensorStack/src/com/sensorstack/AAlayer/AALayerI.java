package com.sensorstack.AAlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

import com.sensorstack.ContinuousStreamInfo;
import com.sensorstack.DiscretePacketInfo;
import com.sensorstack.devicelayer.DeviceLayerClass;
import com.sensorstack.devicelayer.NoDeviceConnectedException;
import com.sensorstack.sensors.BloodPressure;
import com.sensorstack.sensors.DataNotCollectedException;
import com.sensorstack.sensors.ECG;
import com.sensorstack.sensors.GSR;
import com.sensorstack.sensors.Oximeter;
import com.sensorstack.sensors.PulseRate;
import com.sensorstack.sensors.RangeOfValues;
import com.sensorstack.sensors.Sensor;
import com.sensorstack.sensors.SensorContinuous;
import com.sensorstack.sensors.SensorData;
import com.sensorstack.sensors.SensorNotDefinedException;
import com.sensorstack.sensors.Temperature;

/**
 * This layer manages DeviceLayerClass about when and what to fetch. 
 * It unpacks the response packet received from the device layer, validates the various enclosed values 
 * using the hardware range set in AA Layer-II. See {@link AALayerII#setHardwareRange(com.sensorstack.sensors.SensorDataRange)}
 * This layer performs data assurance in that once a request for a set  of values has been made, this layer 
 * will fetch, validate and re-fetch invalid/missing/corrupt values till a complete valid response is generated
 * or the maximum number of attempts has been exhausted. The final populated object is then returned to the 
 * upper layer, AALayer-II.
 */
public class AALayerI {
	static int remainingAttempts;
	private static byte generateRequestByte(boolean req[]) throws DataNotCollectedException
	{
		byte request=0;
		for(int i=0;i<Sensor.NUM_DISCRETE;i++)
			if(req[i])
				request = (byte) (request | (1 << i));		//set bit i in reqyest byte				
		return request;
	}
	
	private static void populateDiscreteResponse(SensorData sd, String response, DiscretePacketInfo p) 
			throws DataNotCollectedException
	{
		String sensorDelimiter = String.valueOf(p.getSensorDelimiter());
		boolean request[]=p.getRequirement();
		for(int i=0;i<request.length;i++)
		{
			if(request[i])
			{
				String sensorid=null;
				try {
					if(p.getSensorIdentifier(i)=='\0')
						throw new DataNotCollectedException("Sensor Identifier is not defined for "+Sensor.sensorName(i)+" sensor");
					sensorid=String.valueOf(p.getSensorIdentifier(i));
				} catch (SensorNotDefinedException e1) {
					e1.printStackTrace();
				}
				String splitter = sensorid + p.getSensorDataDelimiter();
				String data[]= response.split(splitter);
				boolean success=false;
				for(int index=data.length-1;index>0;index--)		//if response contain multiple values for same sensor, try to extract the latest valid value
				{
					String sensorValue = data[index].split(sensorDelimiter)[0];
					try
					{
						switch(i)
						{
							case Sensor.TEMPERATURE:
								Temperature temp=extractTemp(sensorValue);
								if(temp!=null)
								{
									sd.setTemperature(temp);
									success=true;
								}
								break;
							case Sensor.BLOODPRESSURE:
								BloodPressure bp=extractBP(sensorValue,String.valueOf(p.getMultiValueDelimiter()));
								if(bp!=null)
								{
									sd.setBloodPressure(bp);
									success = true;
								}
								break;
							case Sensor.PULSERATE:
								PulseRate pr = extractPulseRate(sensorValue);
								if(pr!=null)
								{
									sd.setPulseRate(pr);
									success = true;
								}
								break;
							case Sensor.OXIMETER:
								Oximeter oxi = extractSPO2(sensorValue);
								if(oxi != null)
								{
									sd.setOximeter(oxi);
									success = true;
								}
								break;
							case Sensor.GSR:
								GSR gsr = extractGSR(sensorValue,String.valueOf(p.getMultiValueDelimiter()));
								if(gsr != null)
								{
									sd.setGsr(gsr);
									success = true;
								}
								break;
						}
						if(success)
						{
							p.setRequirement(i, false);
							break;
						}
					}
					catch(SensorNotDefinedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static GSR extractGSR(String sensorValue, String delimiter) 
	{
		try
		{
			Log.e("extractGSR", "sensorvalue= "+sensorValue+" splitter= "+delimiter);
			String value[]=sensorValue.split(delimiter);
			if(value.length>=2)
			{
				Log.e("extractGSR", "valuelength>=2");
				float conductance=Float.parseFloat(value[0]);
				float resistance=Float.parseFloat(value[1]);
				if(isValid(conductance,resistance, AALayerII.hardwareDataRange.getGsrConductanceRange(), AALayerII.hardwareDataRange.getGsrResistanceRange()))
				{
					Log.e("extractGSR", "GSR valid");
					GSR temp = new GSR();
					temp.setConductanceValue(conductance);
					temp.setResistanceValue(resistance);
					return temp;
				}
			}
			Log.e("extractGSR", "valuelength<2");
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Oximeter extractSPO2(String sensorValue) 
	{
		try
		{
			float value = Float.parseFloat(sensorValue);
			if(isValid(value, AALayerII.hardwareDataRange.getOximeterRange()))
			{
				Oximeter temp = new Oximeter();
				temp.setValue(value);
				return temp;
			}
			else
				return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static PulseRate extractPulseRate(String sensorValue) 
	{
		try
		{
			int value = Integer.parseInt(sensorValue);
			if(isValid(value, AALayerII.hardwareDataRange.getPulseRateRange()))
			{
				PulseRate temp = new PulseRate();
				temp.setValue(value);
				return temp;
			}
			else
				return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static BloodPressure extractBP(String sensorValue, String delimiter) 
	{
		try
		{
			String value[]=sensorValue.split(delimiter);
			if(value.length>=2)
			{
				int sysvalue=Integer.parseInt(value[0]);
				int divalue=Integer.parseInt(value[1]);
				if(isValid(sysvalue,divalue, AALayerII.hardwareDataRange.getSystolicRange(), AALayerII.hardwareDataRange.getDiastolicRange()))
				{
					BloodPressure bp = new BloodPressure();
					bp.setSystolicValue(sysvalue);
					bp.setDiastolicValue(divalue);
					return bp;
				}
			}
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Temperature extractTemp(String sensorValue) 
	{
		Log.e("extractTemp : ","SensorValue is "+sensorValue);
		try
		{
			float value = Float.parseFloat(sensorValue);
			if(isValid(value, AALayerII.hardwareDataRange.getTemperatureRange()))
			{
				Temperature temp = new Temperature();
				temp.setValue(value);
				Log.e("extractTemp: ","Valid reading: "+value);
				return temp;
			}
			else
			{
				Log.e("extractTemp: ","inValid reading: "+value);
				return null;
			}
		}
		catch(Exception e)
		{
			Log.e("extractTemp: ","exception");
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean isValid(float value, RangeOfValues<Float> r)
	{
		if(value<=r.getUpperLimit() && value>=r.getLowerLimit())
			return true;
		return false;
	}
	
	private static boolean isValid(int value, RangeOfValues<Integer> r)
	{
		if(value<=r.getUpperLimit() && value>=r.getLowerLimit())
			return true;
		return false;
	}

	private static boolean isValid(float value1, float value2, RangeOfValues<Float> r1, RangeOfValues<Float> r2)
	{
		if(value1<=r1.getUpperLimit() && value1>=r1.getLowerLimit())
			if(value2<=r2.getUpperLimit() && value2>=r2.getLowerLimit())
				return true;
		return false;
	}
	
	private static boolean isValid(int value1, int value2, RangeOfValues<Integer> r1, RangeOfValues<Integer> r2)
	{
		if(value1<=r1.getUpperLimit() && value1>=r1.getLowerLimit())
			if(value2<=r2.getUpperLimit() && value2>=r2.getLowerLimit())
				return true;
		return false;
	}
	
	static SensorData getDiscrete() throws NoDeviceConnectedException, DataNotCollectedException
	{
		SensorData sd=new SensorData();
		byte requestByte;
		DiscretePacketInfo pi=AALayerII.discretePacketInfo;
		remainingAttempts = pi.getAttempts();
		while(remainingAttempts>0)
		{
			requestByte = generateRequestByte(pi.getRequirement());
			if(requestByte==0)
				break;
			String discreteResponse = DeviceLayerClass.getDiscretePacket(requestByte,pi.getDiscreteTimeout());
			populateDiscreteResponse(sd, discreteResponse, pi);
			remainingAttempts--;
		}
		return sd;
	}
	
	private static byte generateRequestByte(int sensorNum) throws DataNotCollectedException
	{
		byte request=0;
		request = (byte) (request | (1 << sensorNum));	//set bit i in request byte	for (i)th Sensor			
		return request;
	}
	
	private static SensorContinuous<Integer> getIntContinuous(ContinuousStreamInfo c,RangeOfValues<Integer> range) throws NoDeviceConnectedException, DataNotCollectedException{
		SensorContinuous<Integer> sensor;
		String response[];
		byte start=generateRequestByte(c.getSensorNum());
		byte stop=c.getStop();
		String stopAck=String.valueOf(c.getStopAck());
		int length=c.getDataLength();
		int timeout=c.getTimeout();
		if(length==0) {
			length=c.getDataLength(c.getSamplingRate(),c.getDataDuration());
		}
		if(length==0)
			throw new DataNotCollectedException("Length or SamplingRate & Duration Not specified correctly in ContinuousStreamInfo");
		
		Integer[] data=new Integer[length];
		int dataIndex=0;
		response=DeviceLayerClass.getContinuousPacket(start, stop, stopAck, length, timeout);		
		for(int i=0;i<response.length;i++)
		{
			try
			{
				int val=Integer.parseInt(response[i]);
				if(isValid(val,range))
				{
					data[dataIndex]=val;
					dataIndex++;
				}
			}
			catch(NumberFormatException n)
			{
				continue;
			}
		}
		sensor=new SensorContinuous<Integer>(Integer.class,dataIndex);
		sensor.setValueArray(data);
		return sensor;
	}
	
	private static SensorContinuous<Float> getFloatContinuous(ContinuousStreamInfo c,RangeOfValues<Float> range) throws NoDeviceConnectedException, DataNotCollectedException
	{
		SensorContinuous<Float> sensor;
		String response[];
		byte start=generateRequestByte(c.getSensorNum());
		byte stop=c.getStop();
		String stopAck=String.valueOf(c.getStopAck());
		int length=c.getDataLength();
		int timeout=c.getTimeout();
		if(length==0) {
			length=c.getDataLength(c.getSamplingRate(),c.getDataDuration());
		}
		if(length==0)
			throw new DataNotCollectedException("Length or SamplingRate & Duration Not specified correctly in ContinuousStreamInfo");
		
		Float data[]=new Float[length];
		int dataIndex=0;
		response=DeviceLayerClass.getContinuousPacket(start, stop, stopAck, length, timeout);		
		for(int i=0;i<response.length;i++)
		{
			try
			{
				float val=Float.parseFloat(response[i]);
				if(isValid(val,range))
				{
					data[dataIndex]=val;
					dataIndex++;
				}
			}
			catch(NumberFormatException n)
			{
				continue;
			}
		}
		
		sensor=new SensorContinuous<Float>(Float.class,dataIndex);
		sensor.setValueArray(data);
		return sensor;
	}

	
	//methods for geting data from individual sensors
	//static Temperature getTemperature(String requestTemp, String sensorId, String delimiter, RangeOfValues<Float> range, int timeout) throws NoDeviceConnectedException
	static Temperature getTemperature(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		Byte request=0;
		request = (byte) (request | 1 << Sensor.TEMPERATURE);
		//packet design: SensorId|delimiter|value
		String discreteResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
		Log.e("Discrete Response Temp : ", discreteResponse);
		String splitter = sensorId + delimiter;
		String value[] = discreteResponse.split(splitter);
		if(value.length>1)
		{
			Log.e("getTemp : ", "inside if");
			return extractTemp(value[1]);
		}
		return null;
	}
	
	static BloodPressure getBloodPressure(String sensorId, String dataDelimiter, String multiValueDelimiter, int timeout) throws NoDeviceConnectedException
	{
		Byte request=0;
		request = (byte) (request | 1 << Sensor.BLOODPRESSURE);
		//packet design: SensorId|dataDelimiter|value1|multiValueDelimiter|value2
		String discreteResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
		String splitter = sensorId + dataDelimiter;
		String value[] = discreteResponse.split(splitter);
		if(value.length>1)
		{
			return extractBP(value[1], multiValueDelimiter);
		}
		return null;
	}
	
	static PulseRate getPulseRate(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		Byte request=0;
		request = (byte) (request | 1 << Sensor.PULSERATE);
		//packet design: SensorId|delimiter|value
		String discreteResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
		String splitter = sensorId + delimiter;
		String value[] = discreteResponse.split(splitter);
		if(value.length>1)
			return extractPulseRate(value[1]);
		return null;
	}
	
	static Oximeter getOximeter(String sensorId, String delimiter, int timeout) throws NoDeviceConnectedException
	{
		Byte request=0;
		request = (byte) (request | 1 << Sensor.OXIMETER);
		//packet design: SensorId|delimiter|value
		String discreteResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
		String splitter = sensorId + delimiter;
		String value[] = discreteResponse.split(splitter);
		if(value.length>1)
			return extractSPO2(value[1]);
		return null;
	}
	
	static GSR getGSR( String sensorId, String dataDelimiter, String multiValueDelimiter, int timeout) throws NoDeviceConnectedException
	{
		Byte request=0;
		request = (byte) (request | 1 << Sensor.GSR);
		//packet design: SensorId|dataDelimiter|value1|multiValueDelimiter|value2
		String discreteResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
		String splitter = sensorId + dataDelimiter;
		Log.e("getGSR", "response: "+discreteResponse+" splitter= "+splitter);
		String value[] = discreteResponse.split(splitter);
		if(value.length>1)
		{
			Log.e("getGSR", "value[1] = "+value[1]);
			return extractGSR(value[1], multiValueDelimiter);
		}
		Log.e("getGSR", "value.length<=1");
		return null;
	}
	
	static ECG getECG() throws NoDeviceConnectedException, DataNotCollectedException
	{
		ContinuousStreamInfo c=AALayerII.continuousStreamInfo;
		SensorContinuous<Float> response = getFloatContinuous(c, AALayerII.hardwareDataRange.getEcgRange());
		if(response == null)
			return null;
		ECG ecg = new ECG(response);
		return ecg;
	}
	
	static void getMetadata( int sensorNum, HashMap<String, String> metadata, String sensorId, String dataDelimiter, String multiValueDelimiter, int timeout) throws NoDeviceConnectedException
	{
		remainingAttempts = 2;
		if(remainingAttempts > 0)
		{
			Byte request=0;
			request = (byte) (request | 1 << 7);
			request = (byte) (request | 1 << sensorNum);
			//packet design: SensorId|dataDelimiter|value1|multiValueDelimiter|value2|...|multiValueDelimiter|valuen
			String metadataResponse = DeviceLayerClass.getDiscretePacket(request,timeout);
			String splitter = sensorId + dataDelimiter;
			Log.e("getMetadata", "response: "+metadataResponse+" splitter= "+splitter);
			String value[] = metadataResponse.split(splitter);
			if(value.length>1)
			{
				Log.e("getMetadata", "value[1] = "+value[1]);
				if(extractMetada(value[1], multiValueDelimiter, metadata))
					return;
				remainingAttempts--;
			}
		}
		Log.e("getMetadata(AALayer-I)", "Data not collected");
		return;
	}
	
	static boolean extractMetada(String response, String delimiter, HashMap<String, String> metadata)
	{
		String value[]=response.split(delimiter);
		if(value.length == metadata.size())
		{
			Iterator iter = metadata.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) 
			{
				Map.Entry mEntry = (Map.Entry) iter.next();
				metadata.put((String) mEntry.getKey(), value[i]);
				i++;
			}
			return true;
		}
		return false;
	}
}