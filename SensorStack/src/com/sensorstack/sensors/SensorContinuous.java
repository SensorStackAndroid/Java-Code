package com.sensorstack.sensors;

import java.lang.reflect.Array;

/**
 * Base class for continuous data sensors
 * @param <T>
 */
public class SensorContinuous<T> extends Metadata{
	T valueArray[];
	int length;
	
	/**
	 * Constructor to initialise data buffer
	 * @param c data type of data buffer 
	 * @param len length of data buffer
	 */
	public SensorContinuous(Class<T> c, int len){
		this.length = len;
		@SuppressWarnings("unchecked")
        final T[] a = (T[]) Array.newInstance(c, len);
		valueArray = a;
	}
	public T[] getValueArray() {
		return valueArray;
	}
	public void setValueArray(T valueArray[]) {
		//copy
		for(int i=0;i<length&&i<valueArray.length;i++)
			this.valueArray[i] = valueArray[i];
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}	
}
