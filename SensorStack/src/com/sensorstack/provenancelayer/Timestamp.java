package com.sensorstack.provenancelayer;

/**
 * Encapsulates parameters for defining a timestamp.
 * Date is depicted in "dd-MM-yyyy" format while time is depicted in "hh:mm:ss a z" format.
 */
public class Timestamp {
	private String date;
	private String time;
	
	/**
	 * Constructor for setting the date and time data members
	 * @param date Date
	 * @param time Time
	 */
	public Timestamp(String date, String time)
	{
		this.date=date;
		this.time=time;
	}
	
	/**
	 * Returns the date
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Sets the date
	 * @param date Date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Returns the time
	 * @return time
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * Sets the time
	 * @param time Time
	 */
	public void setTime(String time) {
		this.time = time;
	}
}
