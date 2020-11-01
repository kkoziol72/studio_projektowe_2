package com.example.studioprojektowe2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.studioprojektowe2.coordinates.Acceleration;
import com.example.studioprojektowe2.coordinates.Coordinates;
import com.example.studioprojektowe2.coordinates.Distance;
import com.example.studioprojektowe2.coordinates.Velocity;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    final int READINGRATE = 200000;

    Acceleration acceleration = new Acceleration();
    Distance distance = new Distance();
    Velocity velocity = new Velocity();
    Coordinates coordinates = new Coordinates();

    private final SensorEventListener accelerometerListener = new SensorEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accelerometerXValue.setText("x: " + sensorEvent.values[0]);
                accelerometerYValue.setText("y: " + sensorEvent.values[1]);
                accelerometerZValue.setText("z: " + sensorEvent.values[2]);

                acceleration.setA_x(sensorEvent.values[0]);
                acceleration.setA_y(sensorEvent.values[1]);
                acceleration.setA_z(sensorEvent.values[2]);

                coordinates.setCoordinates(acceleration, READINGRATE / 1000000.0F,
                        distance, velocity);
                coordinatesXValue.setText("x: " + coordinates.getX());
                coordinatesYValue.setText("y: " + coordinates.getY());
                coordinatesZValue.setText("z: " + coordinates.getZ());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

    private final SensorEventListener gyroscopeListener = new SensorEventListener()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            gyroscopeXValue.setText("x: " + sensorEvent.values[0]);
            gyroscopeYValue.setText("y: " + sensorEvent.values[1]);
            gyroscopeZValue.setText("z: " + sensorEvent.values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    @SuppressLint({"CutPasteId", "SetTextI18n"})
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

        coordinatesTitle = findViewById(R.id.coordinates);
        coordinatesTitle.setText("Współrzędne: ");
        coordinatesXValue = findViewById(R.id.coordinatesXValue);
        coordinatesYValue = findViewById(R.id.coordinatesYValue);
        coordinatesZValue = findViewById(R.id.coordinatesZValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(accelerometerListener, accelerometer, READINGRATE);
        sensorManager.registerListener(gyroscopeListener, gyroscope, READINGRATE);

        Button setPositionButton = findViewById(R.id.setPositionButton);
        setPositionButton.setOnClickListener(v -> {
            distance.setDistanceTo0();
            velocity.setVelocityTo0();
            acceleration.setAccelerationTo0();
            coordinates.setCoordinatesTo0();
            Log.e("Button", "clicked");
        });
    }



}