/**
 * 
 */
package com.sensorstack.provenancelayer;
/**
 * Encapsulates parameters for defining a geographical location
 */
public class GPSLocation {
	private double latitude;
	private double longitude;
	/**
	 * Returns latitude value of the geographical location
	 * @return latitude value
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets latitude value of the geographical location
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Returns longitude value of the geographical location
	 * @return longitude value
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets longitude value of the geographical location
	 * @param longtitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
