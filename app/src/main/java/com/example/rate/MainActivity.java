package com.example.rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rate.view.Rate;

public class MainActivity extends AppCompatActivity {

    private Rate mRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRate = (Rate) findViewById(R.id.mRate);
        mRate.setActivRate(2);
        mRate.setRateChangeListener(new Rate.RateChangeListener() {
            @Override
            public void change(int size) {
                Toast.makeText(MainActivity.this,"选中的数目："+size,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
