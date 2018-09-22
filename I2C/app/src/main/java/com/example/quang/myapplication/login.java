package com.example.quang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class login extends Activity implements View.OnClickListener {

    private Button id;
    private Button bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.login);

        id = (Button)findViewById(R.id.id);
        bar = (Button)findViewById(R.id.bar);

    }


        @Override
        public void onClick(View view) {
            if (view == bar) {
                Intent intent = new Intent(login.this,MainActivity2.class);
                Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
            if (view == id) {
                Intent intent = new Intent(login.this,MainActivity.class);
                Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }

}

