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
import com.example.studioprojektowe2.coordinates.Rotation;
import com.example.studioprojektowe2.coordinates.Velocity;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    final int READINGRATE = 20000000;
    final int CALIBRATIONTIME = 100;
    private final double ALPHA = 0.18;

    final AtomicBoolean pressed = new AtomicBoolean(false);
    private double[] gyroscopeLastData;
    Acceleration acceleration = new Acceleration();
    Distance distance = new Distance();
    Velocity velocity = new Velocity();
    Coordinates coordinates = new Coordinates();
    Rotation rotation = new Rotation();
    private int calibrationMeter = 0;
    private double accelerometerCalibrationX = 0.0;
    private double accelerometerCalibrationY = 0.0;
    private double accelerometerCalibrationZ = 0.0;

    private final SensorEventListener accelerometerListener = new SensorEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double[] sensorData = convertToDouble(sensorEvent.values);
                if(calibrationMeter > CALIBRATIONTIME) {
                    resetData();
                    sensorData[0] -= accelerometerCalibrationX;
                    sensorData[1] -= accelerometerCalibrationY;
                    sensorData[2] -= accelerometerCalibrationZ;

                    acceleration.readFromArray(lowPass(sensorData, acceleration.toArray()));
                    accelerometerXValue.setText("x: " + sensorData[0]);
                    accelerometerYValue.setText("y: " + sensorData[1]);
                    accelerometerZValue.setText("z: " + sensorData[2]);

                    acceleration.setA_x(sensorData[0]);
                    acceleration.setA_y(sensorData[1]);
                    acceleration.setA_z(sensorData[2]);

                    coordinates.setCoordinates(acceleration, READINGRATE / 1000000.0,
                            distance, velocity);

                    coordinatesXValue.setText("x: " + coordinates.getX());
                    coordinatesYValue.setText("y: " + coordinates.getY());
                    coordinatesZValue.setText("z: " + coordinates.getZ());
                } else if(calibrationMeter == CALIBRATIONTIME) {
                    calibrationMeter++;
                    accelerometerCalibrationX = accelerometerCalibrationX / CALIBRATIONTIME;
                    accelerometerCalibrationY = accelerometerCalibrationY / CALIBRATIONTIME;
                    accelerometerCalibrationZ = accelerometerCalibrationZ / CALIBRATIONTIME;
                } else {
                    calibrationMeter++;
                    accelerometerCalibrationX += sensorData[0];
                    accelerometerCalibrationY += sensorData[1];
                    accelerometerCalibrationZ += sensorData[2];
                }
                Log.e("scope", calibrationMeter + "");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

    private final SensorEventListener gyroscopeListener = new SensorEventListener()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] sensorData = convertToDouble(sensorEvent.values);
            gyroscopeLastData = lowPass(sensorData, gyroscopeLastData);
            rotation.updateAngles(sensorData[0], sensorData[1], sensorData[2], sensorEvent.timestamp);
            gyroscopeXValue.setText("x: " + rotation.getXAngle() * 180 / Math.PI + " stopni");
            gyroscopeYValue.setText("y: " + rotation.getYAngle() * 180 / Math.PI + " stopni");
            gyroscopeZValue.setText("z: " + rotation.getZAngle() * 180 / Math.PI + " stopni");
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
            calibrationMeter = 0;
            resetData();
        });
    }

    protected double[] lowPass( double[] input, double[] output ) {
        if ( output == null ) return input;
        for ( int i = 0; i < input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private double[] convertToDouble(float[] values){
        double[] converted = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            converted[i] = Double.valueOf(values[i]);
        }
        return converted;
    }

    private void resetData() {
        distance.setDistanceTo0();
        velocity.setVelocityTo0();
        acceleration.setAccelerationTo0();
        coordinates.setCoordinatesTo0();
        rotation.setAnglesTo0();
    }
}