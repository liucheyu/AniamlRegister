package com.lcypj.animalregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdopActivity extends AppCompatActivity {

    public static TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adop);
        tv = findViewById(R.id.testjson);



    }
}
