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
import com.kircherelectronics.fsensor.observer.SensorSubject;
import com.kircherelectronics.fsensor.sensor.FSensor;
import com.kircherelectronics.fsensor.sensor.acceleration.KalmanLinearAccelerationSensor;
import com.kircherelectronics.fsensor.sensor.gyroscope.KalmanGyroscopeSensor;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    final int READINGRATE = 200000;
    final AtomicBoolean pressed = new AtomicBoolean(false);
    private final float ALPHA = 0.18f;
    private float[] gyroscopeLastData;
    Acceleration acceleration = new Acceleration();
    Distance distance = new Distance();
    Velocity velocity = new Velocity();
    Coordinates coordinates = new Coordinates();

    private final SensorEventListener accelerometerListener = new SensorEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                acceleration.readFromArray(lowPass(sensorEvent.values, acceleration.toArray()));

                accelerometerXValue.setText("x: " + sensorEvent.values[0]);
                accelerometerYValue.setText("y: " + sensorEvent.values[1]);
                accelerometerZValue.setText("z: " + sensorEvent.values[2]);

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
            gyroscopeLastData = lowPass(sensorEvent.values, gyroscopeLastData);
            gyroscopeXValue.setText("x: " + gyroscopeLastData[0]);
            gyroscopeYValue.setText("y: " + gyroscopeLastData[1]);
            gyroscopeZValue.setText("z: " + gyroscopeLastData[2]);
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


    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i = 0; i < input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}