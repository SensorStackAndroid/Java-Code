package com.sensorstack.sensors;

/**
 * Used to define upper limit and lower limit.
 * @param <T> Data type 
 */
public class RangeOfValues<T>{
	private T lowerLimit;
	private T upperLimit;
	
	/**
	 * Constructor to set the lower and upper limit of the newly created object.
	 * @param lowerLimit lower limit value
	 * @param upperLimit upper limit value
	 */
	public RangeOfValues(T lowerLimit, T upperLimit){
		this.lowerLimit=lowerLimit;
		this.upperLimit = upperLimit;
	}
	
	/**
	 * Returns the lower limit value.
	 * @return lower limit
	 */
	public T getLowerLimit() {
		return lowerLimit;
	}
	
	/**
	 * Sets the value of lower limit
	 * @param lowerLimit lower limit value
	 */
	public void setLowerLimit(T lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	
	/**
	 * Returns the upper limit value
	 * @return upper limit
	 */
	public T getUpperLimit() {
		return upperLimit;
	}
	
	/**
	 * Sets the value of upper limit
	 * @param upperLimit upper limit value
	 */
	public void setUpperLimit(T upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	/**
	 * Used to set both upper and lower limits
	 * @param lowerLimit lower limit value
	 * @param upperLimit upper limit value
	 */
	public void setLimits(T lowerLimit, T upperLimit){
		this.lowerLimit=lowerLimit;
		this.upperLimit = upperLimit;
	}
	
	/**
	 * Compares two objects for equality
	 * @param range object to be compared with
	 * @return True if both objects are equal, false otherwise
	 */
	public boolean equals(RangeOfValues<T> range)
	{
		return (lowerLimit==range.lowerLimit&&upperLimit==range.upperLimit);
	}
}
