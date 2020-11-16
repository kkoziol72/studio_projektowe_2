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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, coordinatesYValue, coordinatesZValue;

    public final static int READINGRATE = 2000;

    final int CALIBRATIONTIME = 2000;
    final int SLOWERRATE = 50;
    private final double ALPHA = 0.18;

    final int CALIBRATIONTIME_G = 2000;
    final int SLOWERRATE_G  = 50;

    final AtomicBoolean pressed = new AtomicBoolean(false);
    private double[] gyroscopeLastData;

    Acceleration acceleration = new Acceleration();
    Distance distance = new Distance();
    Velocity velocity = new Velocity();
    Coordinates coordinates = new Coordinates();
    Rotation rotation = new Rotation();

    private int calibrationMeter = 0;
    private int calibrationMeter_G = 0;
    private double accelerometerCalibrationX = 0.0;
    private double accelerometerCalibrationY = 0.0;
    private double accelerometerCalibrationZ = 0.0;
    private double gyroscopeCalibrationX = 0.0;
    private double gyroscopeCalibrationY = 0.0;
    private double gyroscopeCalibrationZ = 0.0;
    private int slower = 0;
    private int slower_G = 0;

    private final AccelerationKalmanFilter filter = new AccelerationKalmanFilter();

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] filteredData = null;
            double[] sensorData = convertFloatsToDoubles(sensorEvent.values);

            if (calibrationMeter > CALIBRATIONTIME) {
                sensorData[0] -= accelerometerCalibrationX;
                sensorData[1] -= accelerometerCalibrationY;
                sensorData[2] -= accelerometerCalibrationZ;

                acceleration.readFromArray(sensorData);

                for (int i = 0; i < acceleration.getAccelerationComponents().size(); i++) {
                    System.out.println(String.format("%.4f", acceleration.getAccelerationComponents().get(i)));
                }

                List<Double> measurements = new ArrayList<Double>();
                measurements.add(sensorData[0]);
                measurements.add(sensorData[1]);
                measurements.add(sensorData[2]);
                filteredData = filter.estimateMeasurements(measurements);
                acceleration.readFromArray(new double[]{filteredData[6], filteredData[7], filteredData[8]});

                System.out.println("Dodatkowo przefiltrowany:");
                for (int i = 0; i < acceleration.getAccelerationComponents().size(); i++) {
                    System.out.println(String.format("%.4f", acceleration.getAccelerationComponents().get(i)));
                }

                coordinatesTitle.setText("Współrzędne: ");
                accelerometerTitle.setText("Akcelerometr: ");
                coordinates.setCoordinates(acceleration, READINGRATE / 1000000.0,
                            distance, velocity);


            } else if (calibrationMeter == CALIBRATIONTIME) {
                calibrationMeter++;
                accelerometerCalibrationX = accelerometerCalibrationX / CALIBRATIONTIME;
                accelerometerCalibrationY = accelerometerCalibrationY / CALIBRATIONTIME;
                accelerometerCalibrationZ = accelerometerCalibrationZ / CALIBRATIONTIME;
                resetData();
            } else {
                coordinatesTitle.setText("Współrzędne: TRWA KALIBRACJA");
                accelerometerTitle.setText("Akcelerometr: TRWA KALIBRACJA");
                calibrationMeter++;
                accelerometerCalibrationX += sensorData[0];
                accelerometerCalibrationY += sensorData[1];
                accelerometerCalibrationZ += sensorData[2];
            }
            slower++;
            if (slower > SLOWERRATE) {
                slower = 0;
                if (calibrationMeter > CALIBRATIONTIME) {
                    showCoordinates(new double[]{filteredData[6], filteredData[7], filteredData[8]});
                } else {
                    showCoordinates(sensorData);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    private final SensorEventListener gyroscopeListener = new SensorEventListener() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] sensorData = convertFloatsToDoubles(sensorEvent.values);

            if (calibrationMeter_G > CALIBRATIONTIME_G) {
                sensorData[0] -= gyroscopeCalibrationX;
                sensorData[1] -= gyroscopeCalibrationY;
                sensorData[2] -= gyroscopeCalibrationZ;

                rotation.updateWithSensorData(sensorData);

                for (int i = 0; i < rotation.getRotationComponents().size(); i++) {
                    System.out.println(String.format("%.4f", rotation.getRotationComponents().get(i)));
                }

                gyroscopeTitle.setText("Żyroskop: ");

                coordinates.countCoordinatesOnRotation(rotation);

            } else if (calibrationMeter_G == CALIBRATIONTIME_G) {
                calibrationMeter_G++;
                gyroscopeCalibrationX = gyroscopeCalibrationX / CALIBRATIONTIME_G;
                gyroscopeCalibrationY = gyroscopeCalibrationY / CALIBRATIONTIME_G;
                gyroscopeCalibrationZ = gyroscopeCalibrationZ / CALIBRATIONTIME_G;
                resetData();
            } else {
                gyroscopeTitle.setText("Żyroskop: TRWA KALIBRACJA");
                calibrationMeter_G++;
                gyroscopeCalibrationX += sensorData[0];
                gyroscopeCalibrationY += sensorData[1];
                gyroscopeCalibrationZ += sensorData[2];
            }
            slower_G++;
            if (slower_G > SLOWERRATE_G) {
                slower_G = 0;
                showRotationData(sensorData);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
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
            calibrationMeter_G = 0;
            resetData();
            resetAccelerometerCalibration();
            resetGyroscopeCalibration();
        });
    }

    protected double[] lowPass(double[] input, double[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private double[] convertToDouble(float[] values) {
        double[] converted = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            converted[i] = (double) values[i];
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

    private void resetAccelerometerCalibration() {
        accelerometerCalibrationX = 0;
        accelerometerCalibrationY = 0;
        accelerometerCalibrationZ = 0;
    }

    private void resetGyroscopeCalibration() {
        gyroscopeCalibrationX = 0;
        gyroscopeCalibrationY = 0;
        gyroscopeCalibrationZ = 0;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showCoordinates(double[] sensorData) {
        accelerometerXValue.setText("x: " + String.format("%.4f", sensorData[0]));
        accelerometerYValue.setText("y: " + String.format("%.4f", sensorData[1]));
        accelerometerZValue.setText("z: " + String.format("%.4f", sensorData[2]));

        coordinatesXValue.setText("x: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(0)));
        coordinatesYValue.setText("y: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(1)));
        coordinatesZValue.setText("z: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(2)));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void showRotationData(double [] gyroscopeData) {
        gyroscopeXValue.setText("x: " + String.format("%.4f stopni", Math.toDegrees(gyroscopeData[0])));
        gyroscopeYValue.setText("y: " + String.format("%.4f stopni", Math.toDegrees(gyroscopeData[1])));
        gyroscopeZValue.setText("z: " + String.format("%.4f stopni", Math.toDegrees(gyroscopeData[2])));
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