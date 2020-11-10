package com.example.studioprojektowe2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.studioprojektowe2.coordinates.Acceleration;
import com.example.studioprojektowe2.coordinates.Coordinates;
import com.example.studioprojektowe2.coordinates.Distance;
import com.example.studioprojektowe2.coordinates.Rotation;
import com.example.studioprojektowe2.coordinates.Velocity;
import com.example.studioprojektowe2.filter.AccelerationKalmanFilter;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    public final static int READINGRATE = 2000;
    final int CALIBRATIONTIME = 2000;
    final int SLOWERRATE = 50;
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
    private int slower = 0;

    private final AccelerationKalmanFilter filter = new AccelerationKalmanFilter();

    private final SensorEventListener accelerometerListener = new SensorEventListener()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] sensorData = filter.estimateCoordinates(sensorEvent.values);

            if(calibrationMeter > CALIBRATIONTIME) {
                coordinatesTitle.setText("Współrzędne: ");
                accelerometerTitle.setText("Akcelerometr: ");
                resetData();
                sensorData[0] -= accelerometerCalibrationX;
                sensorData[1] -= accelerometerCalibrationY;
                sensorData[2] -= accelerometerCalibrationZ;

                try {
                    acceleration.readFromArray(sensorData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < acceleration.getAccelerationComponents().size(); i++) {
                    acceleration.getAccelerationComponents().set(i, sensorData[i]);
                }

                try {
                    coordinates.setCoordinates(acceleration, READINGRATE / 1000000.0,
                            distance, velocity);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if(calibrationMeter == CALIBRATIONTIME) {
                calibrationMeter++;
                accelerometerCalibrationX = accelerometerCalibrationX / CALIBRATIONTIME;
                accelerometerCalibrationY = accelerometerCalibrationY / CALIBRATIONTIME;
                accelerometerCalibrationZ = accelerometerCalibrationZ / CALIBRATIONTIME;
            } else {
                coordinatesTitle.setText("Współrzędne: TRWA KALIBRACJA");
                accelerometerTitle.setText("Akcelerometr: TRWA KALIBRACJA");
                calibrationMeter++;
                accelerometerCalibrationX += sensorData[0];
                accelerometerCalibrationY += sensorData[1];
                accelerometerCalibrationZ += sensorData[2];
            }
            slower++;
            if(slower > SLOWERRATE){
                slower = 0;
                showCoordinates(sensorData);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    private final SensorEventListener gyroscopeListener = new SensorEventListener()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] sensorData = filter.estimateCoordinates(sensorEvent.values);

            gyroscopeLastData = lowPass(sensorData, gyroscopeLastData);
            rotation.updateAngles(sensorData[0], sensorData[1], sensorData[2], sensorEvent.timestamp);
            gyroscopeXValue.setText("x: " + rotation.getRotationComponents().get(0) * 180 / Math.PI + " stopni");
            gyroscopeYValue.setText("y: " + rotation.getRotationComponents().get(1) * 180 / Math.PI + " stopni");
            gyroscopeZValue.setText("z: " + rotation.getRotationComponents().get(2) * 180 / Math.PI + " stopni");
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
            converted[i] = (double) values[i];
        }
        return converted;
    }

    private void resetData() {
        accelerometerCalibrationX = 0;
        accelerometerCalibrationY = 0;
        accelerometerCalibrationZ = 0;
        distance.setDistanceTo0();
        velocity.setVelocityTo0();
        acceleration.setAccelerationTo0();
        coordinates.setCoordinatesTo0();
        rotation.setAnglesTo0();
    }

    @SuppressLint("SetTextI18n")
    private void showCoordinates(double[] sensorData){
        accelerometerXValue.setText("x: " + sensorData[0]);
        accelerometerYValue.setText("y: " + sensorData[1]);
        accelerometerZValue.setText("z: " + sensorData[2]);

        coordinatesXValue.setText("x: " + coordinates.getCoordinatesComponents().get(0));
        coordinatesYValue.setText("y: " + coordinates.getCoordinatesComponents().get(1));
        coordinatesZValue.setText("z: " + coordinates.getCoordinatesComponents().get(2));
    }
}