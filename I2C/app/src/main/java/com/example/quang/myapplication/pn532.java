package com.example.quang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import static android.content.ContentValues.TAG;

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
public class pn532 extends Activity implements View.OnClickListener {

    private Button btn;

    private static final String LED_PIN_NAME = "BCM20"; // GPIO port wired to the LED
    private Gpio mLedGpio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.pn532);

        btn = (Button)findViewById(R.id.btn);

        initGPIO();
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            Intent intent = new Intent(pn532.this,login.class);
            Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    private void initGPIO()
    {
        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            mLedGpio2 = manager.openGpio(LED_PIN_NAME);
            // Step 2. Configure as an output.
           // mLedGpio2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            //LedGpio2.setValue(true);

        } catch (IOException e) {
            Log.d(TAG, "Error on PeripheralIO API");
        }
    }
}