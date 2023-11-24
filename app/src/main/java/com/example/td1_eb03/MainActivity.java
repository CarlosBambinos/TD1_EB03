package com.example.td1_eb03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mtv;
    Slider msl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtv = findViewById(R.id.tv);
        msl = findViewById(R.id.slider3);

        msl.setSliderChangeListener((value) ->  mtv.setText(String.format("%2f",value)));
    }
}

