package com.sensorstack.devicelayer;

import com.sensorstack.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is used for connection establishment of android device with the sensor hardware
 * depending on the mode of connectivity. In the current version, Bluetooth connectivity is supported.
 */
public class DeviceLayerActivity extends Activity{

	// Debugging
	private static final String TAG = "BluetoothConnect";
	private static final boolean D = true;
	public static boolean result = false;
	public static Intent intent = null;
	public static boolean conn_flag=false;

	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_CONNECT_DEVICE = 2;

	// Name of the connected device
	private static String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private static BluetoothAdapter mBluetoothAdapter = null;
	private static Context context;
	// public static String address;
	public static Intent serverIntent;
	private static Activity activity;



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = this;
		context=this;
		Bundle b=getIntent().getExtras();
		int request_mode=b.getInt("mode", Mode.BLUETOOTH);
		DeviceLayerClass.setCurrentMode(request_mode);
		try {
			if(DeviceLayerClass.isAvailable(context,request_mode))
			{
				switch(request_mode)
				{
				case Mode.BLUETOOTH:
					initialise(context);
					break;
					/*		case Mode.USB:
						return ;
					case Mode.WIFI:
						return ;
					 */	default:
						 throw new ModeNotSupportedException("This Mode of connectivity is not supported currently.");
				}
			}
			else
			{
				setResult(RESULT_CANCELED);
				Toast.makeText(this, "This Mode of connectivity is currently not supported by device.", Toast.LENGTH_SHORT).show();
				finish();
			}
		} catch (ModeNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "This Mode of connectivity is currently not supported by library.", Toast.LENGTH_SHORT).show();
			setResult(RESULT_CANCELED);
			finish();
		}
		//initialise(this);
	}

	private void initialise(Context c)
	{
		context=c;
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) 
		{
			Toast.makeText(c, "in intialize", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "in intialize()");
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
			/*  if (!mBluetoothAdapter.isEnabled())
            	return false;*/
		}             	
		// Otherwise, setup the chat session
		else
		{
			if (DeviceLayerClass.getBluetoothService() == null) 
				setupBTConnection();
		}
		// return true;
	}

	private void setupBTConnection() {
		Log.d(TAG, "setupChat()");
		DeviceLayerClass.setBluetoothService(new BluetoothService(context, mHandler));
		serverIntent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	/*private void ensureDiscoverable() 
    {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            context.startActivity(discoverableIntent);
        }
    }*/

	/**
	 * Sends a message.
	 * @param message  A string of text to send.
	 */
	/* private static void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }*/

	private final static void setStatus(int resId) {
		Toast.makeText(context, resId,  Toast.LENGTH_SHORT).show();
	}

	private final static void setStatus(CharSequence status) {
		Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
	}        	

	// The Handler that gets information back from the BluetoothService
	private final static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DeviceLayerClass.MESSAGE_STATE_CHANGE:
				if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					DeviceLayerClass.setConnected(true);
					setStatus(context.getString(R.string.title_connected_to, mConnectedDeviceName));
					//mConversationArrayAdapter.clear();
					DeviceLayerClass.getBluetoothService().setHandler(DeviceLayerClass.mHandler);
					activity.setResult(RESULT_OK);
					activity.finish();
					break;
				case BluetoothService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;
				case BluetoothService.STATE_FAILED:
				case BluetoothService.STATE_LOST:
					DeviceLayerClass.setConnected(false);
					DeviceLayerClass.getBluetoothService().setHandler(DeviceLayerClass.mHandler);
					activity.setResult(RESULT_CANCELED);
					activity.finish();
				case BluetoothService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			/*case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				Log.e(TAG, "readMessage = "+readMessage);
				mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
				break;*/
			case DeviceLayerClass.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DeviceLayerClass.DEVICE_NAME);
				Toast.makeText(context, "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case DeviceLayerClass.MESSAGE_TOAST:
				Toast.makeText(context, msg.getData().getString(DeviceLayerClass.TOAST), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data);
			}
			break;

		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupBTConnection();		// @AA@ set result code
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(context, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				// setResult(RESULT_CANCELED);
				finish();
			}
		}
	}

	private void connectDevice(Intent data) {
		// Get the device MAC address
		String address = data.getExtras()
				.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

		// Get the BluetoothDevice object
		/*while(address==null);*/
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		DeviceLayerClass.getBluetoothService().connect(device);
		/*if(mChatService.mState==3)
        	setResult(RESULT_OK);
        else
        	setResult(RESULT_CANCELED);*/
		//finish();
	}

	/*public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        int itemid=item.getItemId();
        if(itemid==R.id.secure_connect_scan)
        {
        	 // Launch the DeviceListActivity to see devices and do scan
        	 serverIntent = new Intent(this,DeviceListActivity.class);
             startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
             return true;
        }
        else if(itemid==R.id.insecure_connect_scan)
        {
        	// Launch the DeviceListActivity to see devices and do scan
        	serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        }
        else if(itemid==R.id.discoverable)
        {
        	// Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
	 */

	/* public void onBackPressed() {
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	            //Yes button clicked
    	        	finish();
    	        	System.exit(0);
    	            break;

    	        case DialogInterface.BUTTON_NEGATIVE:
    	            //No button clicked
    	            break;
    	        }
    	    }
    	};

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Are you sure you want to exit the application?").setPositiveButton("Yes", dialogClickListener)
    	    .setNegativeButton("No", dialogClickListener).show();
    }*/
}