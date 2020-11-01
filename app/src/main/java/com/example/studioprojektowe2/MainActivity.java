package com.example.studioprojektowe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private Button setPositionButton;

    private SensorEventListener accelerometerListener = new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accelerometerXValue.setText("x: " + sensorEvent.values[0]);
                accelerometerYValue.setText("y: " + sensorEvent.values[1]);
                accelerometerZValue.setText("z: " + sensorEvent.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

    private SensorEventListener gyroscopeListener = new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            gyroscopeXValue.setText("x: " + sensorEvent.values[0]);
            gyroscopeYValue.setText("y: " + sensorEvent.values[1]);
            gyroscopeZValue.setText("z: " + sensorEvent.values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gyroscopeTitle = findViewById(R.id.gyroscope);
        gyroscopeTitle.setText(("Żyroskop:"));
        gyroscopeXValue = findViewById(R.id.gyroscopeXValue);
        gyroscopeYValue = findViewById(R.id.gyroscopeYValue);
        gyroscopeZValue = findViewById(R.id.gyroscopeZValue);

        accelerometerTitle = findViewById(R.id.accelerometer);
        accelerometerTitle.setText("Akcelerometr: ");
        accelerometerXValue = findViewById(R.id.accelerometerXValue);
        accelerometerYValue = findViewById(R.id.accelerometerYValue);
        accelerometerZValue = findViewById(R.id.accelerometerZValue);

        int READINGRATE = 200000;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(accelerometerListener, accelerometer, READINGRATE);
        sensorManager.registerListener(gyroscopeListener, gyroscope, READINGRATE);

        setPositionButton = findViewById(R.id.setPositionButton);
        setPositionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Set Pos
                Log.e("Button", "clicked");
            }
        });
    }



}