package com.sensorstack.devicelayer;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class facilitates data exchange between the android device and sensor hardware over a dedicated channel 
 * created during the connection establishment phase. Connection establishment phase must have been completed
 * before any actual data transfer.
 */
public class DeviceLayerClass{

	private static int currentMode = -1;
	private static boolean isConnected = false;
	static BluetoothAdapter mBluetoothAdapter = null;		//similarly add adapters for other modes of connectivity as well
	UsbDevice mUsbDevice = null;
	
	private static BluetoothService bluetoothService = null;
	static String readDiscrete = new String();
	static final Object responseLock = new Object();			//
	static final Object requestLock = new Object();
	static String readContinuous[];
	static int writeIndex;
	static String stopAck; //Continuous stream StopAck received from h/w

	private static volatile boolean isContinuous = false;
	
	// Message types sent from the BluetoothService Handler
	static final int MESSAGE_STATE_CHANGE = 1;
	static final int MESSAGE_READ = 2;
	static final int MESSAGE_WRITE = 3;
	static final int MESSAGE_DEVICE_NAME = 4;
	static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothService Handler
	static final String DEVICE_NAME = "device_name";
	static final String TOAST = "toast";

	final static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					DeviceLayerClass.setConnected(true);
					break;
				case BluetoothService.STATE_CONNECTING:
					break;
				case BluetoothService.STATE_FAILED:
				case BluetoothService.STATE_LOST:
					DeviceLayerClass.setConnected(false);
					break;

				case BluetoothService.STATE_NONE:
					break;
				}
				break;
			//case MESSAGE_READ:
			}	
		}
	};
		
	/**
	 * Check if specified mode of connectivity is supported by the android device
	 * @param context Context of app
	 * @param mode Mode of connectivity with the sensor hardware
	 * @return true if specified mode of connectivity is supported by the android device, false otherwise
	 * @throws ModeNotSupportedException 
	 */
	public static boolean isAvailable(Context context, int mode) throws ModeNotSupportedException
	{
		switch(mode)
		{
			case Mode.BLUETOOTH:
				if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
				{
					mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					// If the adapter is null, then Bluetooth is not supported
					if (mBluetoothAdapter == null)
					{
						//setResult(RESULT_CANCELED);
						return false;
					}
					else
					{
						//setResult(RESULT_OK);									
						return true;
					}
				}
				else
				{
					//setResult(RESULT_CANCELED);
					return false;
				}
			case Mode.USB:
				/*
				if(context.getPackageManager().hasSystemFeature("android.hardware.usb.host"))
					setResult(RESULT_OK);
				else
					setResult(RESULT_CANCELED);
					*/
				return context.getPackageManager().hasSystemFeature("android.hardware.usb.host");
			case Mode.WIFI:
				/*
				if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
					setResult(RESULT_OK);
				else
					setResult(RESULT_CANCELED);
					*/
				return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
			default:
				throw new ModeNotSupportedException();
		}
	}
	
	static BluetoothService getBluetoothService() {
		return bluetoothService;
	}
	static void setBluetoothService(BluetoothService btService) {
		DeviceLayerClass.bluetoothService = btService;
	}

	/* Only Device layer class can read mode
	 * public static int getCurrentMode() {
		return currentMode;
	}*/

	static void setCurrentMode(int currentMode) {
		DeviceLayerClass.currentMode = currentMode;
	}

	public static boolean isConnected() {
		return isConnected;
	}

	static void setConnected(boolean isConnected) {
		DeviceLayerClass.isConnected = isConnected;
	}
	
	
	/* getDiscretePacket() at DLC (device layer class) is called from the AA-I layer whenever a 
	 * request for a discrete data packet from h/w is made.
	 * The function checks if device is connected, sends request byte and waits for signal from connected thread
	 * Finally, the read line is returned to the caller.
	 */
	/**
	 * Fetches a response packet from the hardware within a timeout period for multiple discrete sensors
	 * @param requestByte Request byte sent to the hardware specifying the kind of data requested
	 * @param timeout Timeout period in milliseconds
	 * @return Response packet received from the sensor hardware
	 * @throws NoDeviceConnectedException
	 */
	public static String getDiscretePacket(byte requestByte, int timeout) throws NoDeviceConnectedException
	{
		setContinuous(false);
		if(!isConnected())
			throw new NoDeviceConnectedException("No Device Connected");
		switch(currentMode)
		{
			case Mode.BLUETOOTH:
				synchronized(responseLock){
					try {
						//Log.e("DeviceLayerClass","Waiting for pkt");
						readDiscrete="";
						synchronized (requestLock) {
							//bluetoothService.write(requestByte.getBytes());
							bluetoothService.write(requestByte);
							requestLock.notifyAll();
						}
						responseLock.wait(timeout);
						//Log.e("DeviceLayerClass","the wait is over");
						return readDiscrete;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
				
			case Mode.USB:
			case Mode.WIFI:
				break;					
		}
		return readDiscrete;
	}
	
	/**
	 * Fetches a response stream from the hardware within a timeout period for particular continuous data sensor
	 * @param start Request byte sent to the hardware requesting to start streaming data of the specified sensor
	 * @param stop Byte sent to the hardware requesting it to stop streaming
	 * @param stopAck Byte received from hardware as an acknowledgement to the stop byte
	 * @param numLines Specifies the length of data buffer
	 * @param timeout Timeout period in milliseconds
	 * @return Response buffer received from the sensor hardware
	 * @throws NoDeviceConnectedException
	 */
	public static String[] getContinuousPacket(byte start, byte stop, String stopAck, int numLines, int timeout) throws NoDeviceConnectedException
	{
		setContinuous(true);
		DeviceLayerClass.stopAck=stopAck;
		if(!isConnected())
			throw new NoDeviceConnectedException("No Device Connected");
		switch(currentMode)
		{
			case Mode.BLUETOOTH:
				
				synchronized(responseLock){
					try {
						readContinuous=new String[numLines];
						writeIndex=0;
						synchronized (requestLock) {
							bluetoothService.write(start);
							requestLock.notifyAll();
						}
						responseLock.wait(timeout);
						bluetoothService.write(stop);
						return readContinuous;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case Mode.USB:
			case Mode.WIFI:
				break;					
		}
		return readContinuous;
	}

	static boolean isContinuous() {
		return isContinuous;
	}

	static void setContinuous(boolean isContinuous) {
		DeviceLayerClass.isContinuous = isContinuous;
	}
	
}
