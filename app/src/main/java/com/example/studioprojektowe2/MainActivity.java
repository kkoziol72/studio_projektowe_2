package com.example.studioprojektowe2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import com.kircherelectronics.fsensor.sensor.acceleration.LowPassLinearAccelerationSensor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    // final int READINGRATE = 2000;

    Acceleration acceleration = new Acceleration();
    Distance distance = new Distance();
    Velocity velocity = new Velocity();
    Coordinates coordinates = new Coordinates();

    Instant now;

    private FSensor fSensor;

    private int slower = 0;

    private final SensorSubject.SensorObserver sensorObserver = new SensorSubject.SensorObserver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(float[] values) {
            acceleration.readFromArray(convertFloatsToDoubles(values));
/*
            if(acceleration.getAccelerationComponents().get(0) < 0.001 && acceleration.getAccelerationComponents().get(0) > -0.001d)
                acceleration.getAccelerationComponents().set(0, 0.0d);

            if(acceleration.getAccelerationComponents().get(1) < 0.001 && acceleration.getAccelerationComponents().get(1) > -0.001)
                acceleration.getAccelerationComponents().set(1, 0.0d);

            if(acceleration.getAccelerationComponents().get(2) < 0.001 && acceleration.getAccelerationComponents().get(2) > -0.001)
                acceleration.getAccelerationComponents().set(2, 0.0d);
*/
            if(values.length == 4 && !(values[3] != values[3])) {
                float frequency = values[3];
                double timeFreq = 1/frequency;
                Log.d("TIME", frequency + ",,,,, " +timeFreq + " " + Thread.currentThread() + "  " + Instant.now());
                Log.d("BEFORE VELOCITY" ,"X: "+velocity.getVelocityComponents().get(0)+", "+"X: "+velocity.getVelocityComponents().get(1)+", "+"X: "+velocity.getVelocityComponents().get(2));
                Log.d("BEFoRE ACCELLERATION" ,"X: "+acceleration.getAccelerationComponents().get(0)+", "+"X: "+acceleration.getAccelerationComponents().get(1)+", "+"X: "+acceleration.getAccelerationComponents().get(2));
                coordinates.setCoordinates(acceleration, timeFreq,
                        distance, velocity);
                Log.d("AFTER VELOCITY" ,"X: "+velocity.getVelocityComponents().get(0)+", "+"X: "+velocity.getVelocityComponents().get(1)+", "+"X: "+velocity.getVelocityComponents().get(2));
                Log.d("AFTER ACCELLERATION" ,"X: "+acceleration.getAccelerationComponents().get(0)+", "+"X: "+acceleration.getAccelerationComponents().get(1)+", "+"X: "+acceleration.getAccelerationComponents().get(2));

            }
            if(slower > 50) {
                accelerometerXValue.setText("x: " + String.format("%.4f", values[0]));
                accelerometerYValue.setText("y: " + String.format("%.4f", values[1]));
                accelerometerZValue.setText("z: " + String.format("%.4f", values[2]));

                coordinatesXValue.setText("x: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(0)));
                coordinatesYValue.setText("y: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(1)));
                coordinatesZValue.setText("z: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(2)));
                slower = 0;
            }
            slower++;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LowPassLinearAccelerationSensor lowPass = new LowPassLinearAccelerationSensor(this);
        //lowPass.s(0.01f);
        fSensor = lowPass;
        fSensor.register(sensorObserver);
        fSensor.start();
    }

    @Override
    public void onPause() {
        fSensor.unregister(sensorObserver);
        fSensor.stop();
        super.onPause();
    }


//    private final SensorEventListener accelerometerListener = new SensorEventListener()
//        {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                accelerometerXValue.setText("x: " + sensorEvent.values[0]);
//                accelerometerYValue.setText("y: " + sensorEvent.values[1]);
//                accelerometerZValue.setText("z: " + sensorEvent.values[2]);
//
//                acceleration.readFromArray(convertFloatsToDoubles(sensorEvent.values));
//
//                coordinates.setCoordinates(acceleration, READINGRATE / 1000000.0D,
//                        distance, velocity);
//                coordinatesXValue.setText("x: " + coordinates.getCoordinatesComponents().get(0));
//                coordinatesYValue.setText("y: " + coordinates.getCoordinatesComponents().get(1));
//                coordinatesZValue.setText("z: " + coordinates.getCoordinatesComponents().get(2));
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {}
//        };

//    private final SensorEventListener gyroscopeListener = new SensorEventListener()
//    {
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            gyroscopeXValue.setText("x: " + sensorEvent.values[0]);
//            gyroscopeYValue.setText("y: " + sensorEvent.values[1]);
//            gyroscopeZValue.setText("z: " + sensorEvent.values[2]);
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {}
//    };

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        Button setPositionButton = findViewById(R.id.setPositionButton);
        setPositionButton.setOnClickListener(v -> {
            distance.setDistanceTo0();
            velocity.setVelocityTo0();
            acceleration.setAccelerationTo0();
            coordinates.setCoordinatesTo0();
            Log.e("Button", "clicked");
        });

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // sensorManager.registerListener(accelerometerListener, accelerometer, READINGRATE);
        // sensorManager.registerListener(gyroscopeListener, gyroscope, READINGRATE);

        fSensor = new LowPassLinearAccelerationSensor(this);
        fSensor.register(sensorObserver);
        fSensor.start();
    }

    public static double[] convertFloatsToDoubles(float[] input) {
        if (input == null) {
            return null;
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

}