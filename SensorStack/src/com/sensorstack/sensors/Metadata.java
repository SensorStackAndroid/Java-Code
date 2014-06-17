package com.sensorstack.sensors;

import java.util.HashMap;

import com.sensorstack.provenancelayer.GPSLocation;
import com.sensorstack.provenancelayer.Timestamp;

/**
 * Encapsulates metadata parameters supported
 */
public abstract class Metadata {
	HashMap<String, String> metadataMap = new HashMap<String, String>();
	GPSLocation gps;
	Timestamp timestamp;
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public GPSLocation getGps() {
		return gps;
	}
	public void setGps(GPSLocation gps) {
		this.gps = gps;
	}
	public HashMap<String, String> getMetadataMap() {
		return metadataMap;
	}
	public void setMetadataMap(HashMap<String, String> metadataMap) {
		this.metadataMap = metadataMap;
	}
}