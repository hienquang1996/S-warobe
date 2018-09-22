package com.example.quang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
public class MainActivity2 extends Activity implements View.OnClickListener {

    private Button clear;
    private Button btn;
    private Button back;
    private EditText code;
    private TextView rs;
    int b=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.ui2);

        btn = (Button)findViewById(R.id.btn);
        clear = (Button)findViewById(R.id.clear);
        back = (Button)findViewById(R.id.back);
        code = (EditText)findViewById(R.id.code);
        rs = (TextView)findViewById(R.id.rs);

        //forgot.setText("Number of attempts remainning: 5");
    }

    private void verify(String id){
        if((id.equals("a"))){
            Intent intent = new Intent(MainActivity2.this,pn532.class);
            Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        else{
            b--;
            rs.setText("Number of attemps remaining: "+ String.valueOf(b));
            Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_LONG).show();
            if(b==0){
                btn.setEnabled(false);
                rs.setText("FAILED TO LOGIN");
                //Toast.makeText(getApplicationContext(),"an lin roi",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            verify(code.getText().toString().trim());
        }
        if (view == back){
            Intent intent = new Intent(MainActivity2.this,login.class);
            Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        if (view == clear){
            code.setText("");
        }
    }
}
