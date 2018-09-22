package com.example.quang.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static java.sql.Types.NULL;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private static final int USB_VENDOR_ID = 0x2341; // 9025
    private static final int USB_PRODUCT_ID = 0x0001;

    private UsbManager usbManager;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialDevice;
    private String buffer = "";

    //BUTTONS CONFIGURATION
    //private int b=5;
    private TextView rsp;
    private Switch sw;
    private Button back;


    //LED CONFIGURATION
    private static final String LED_PIN_NAME1 = "BCM26"; // GPIO port wired to the LED
    private Gpio mLedGpio;

    private UsbSerialInterface.UsbReadCallback callback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] data) {
            try {
                String dataUtf8 = new String(data, "UTF-8");
                buffer += dataUtf8;
                int index;
                while ((index = buffer.indexOf('\n')) != -1) {
                    final String dataStr = buffer.substring(0, index + 1).trim();
                    buffer = buffer.length() == index ? "" : buffer.substring(index + 1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onSerialDataReceived(dataStr);
                            System.out.println("helllllo "+ dataStr);
                        }
                    });
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error receiving USB data", e);
            }
        }
    };

    private final BroadcastReceiver usbDetachedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null && device.getVendorId() == USB_VENDOR_ID && device.getProductId() == USB_PRODUCT_ID) {
                    Log.i(TAG, "USB device detached");
                    stopUsbConnection();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.ui);

        usbManager = getSystemService(UsbManager.class);

        // Detach events are sent as a system-wide broadcast
        IntentFilter filter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbDetachedReceiver, filter);

        //button
        rsp = (TextView)findViewById(R.id.rsp);
        sw = (Switch)findViewById(R.id.sw);
        back = (Button)findViewById(R.id.back);

        //initGPIO();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startUsbConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbDetachedReceiver);
        stopUsbConnection();
    }

    private void startUsbConnection() {
        Map<String, UsbDevice> connectedDevices = usbManager.getDeviceList();

        if (!connectedDevices.isEmpty()) {
            for (UsbDevice device : connectedDevices.values()) {
                if (device.getVendorId() != NULL) {
                    Log.i(TAG, "Device found: " + device.getDeviceName());
                    startSerialConnection(device);
                    return;
                }
            }
        }
        Log.w(TAG, "Could not start USB connection - No devices found");
    }

    private void startSerialConnection(UsbDevice device) {
        Log.i(TAG, "Ready to open USB device connection");
        connection = usbManager.openDevice(device);
        serialDevice = UsbSerialDevice.createUsbSerialDevice(device, connection);
        if (serialDevice != null) {
            if (serialDevice.open()) {
                serialDevice.setBaudRate(115200);
                serialDevice.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialDevice.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialDevice.setParity(UsbSerialInterface.PARITY_NONE);
                serialDevice.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serialDevice.read(callback);
                Log.i(TAG, "Serial connection opened");
            } else {
                Log.w(TAG, "Cannot open serial connection");
            }
        } else {
            Log.w(TAG, "Could not create Usb Serial Device");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            Intent intent = new Intent(MainActivity.this, login.class);
            Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    private void onSerialDataReceived(String data) {
        // Add whatever you want here
        String id ="7C985EF0";
        Log.i(TAG, "Serial data received: " + data);
        if(data.equals(id)){
            Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
            rsp.setText("WELCOME");
            System.out.println("1111111111111111111111");
            Intent intent = new Intent(MainActivity .this,pn532.class);
            //stopUsbConnection();
            startActivity(intent);
        }else{
            System.out.println("22222222222" + data);
            rsp.setText("Please insert your ID card!");
            //rsp.setText("Number of attemps remaining: "+ String.valueOf(b));
            //Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"ACCESS DENIED",Toast.LENGTH_LONG).show();
        }
    }

    private void stopUsbConnection() {
        try {
            if (serialDevice != null) {
                connection.close();
            }

            if (connection != null) {
                connection.close();
            }
        } finally {
            serialDevice = null;
            connection = null;
        }
    }
    ///////// SETUP LED //////////
    private void initGPIO()
    {
        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            mLedGpio = manager.openGpio(LED_PIN_NAME1);
            // Step 2. Configure as an output.
            //mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            //mLedGpio.setValue(true);
        } catch (IOException e) {
            Log.d(TAG, "Error on PeripheralIO API");
        }
    }
}
