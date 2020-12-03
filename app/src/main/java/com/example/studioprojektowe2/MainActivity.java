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
import com.example.studioprojektowe2.filter.AccelerationKalmanFilter;
import com.example.studioprojektowe2.filter.RotationKalmanFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    TextView gyroscopeXValue, gyroscopeYValue, gyroscopeZValue, gyroscopeTitle,
            accelerometerXValue, accelerometerYValue, accelerometerZValue, accelerometerTitle,
            coordinatesTitle, coordinatesXValue, velocityXValue, coordinatesYValue, velocityYValue, coordinatesZValue, velocityZValue;

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

    private RotationKalmanFilter xAngleFilter = new RotationKalmanFilter();
    private RotationKalmanFilter yAngleFilter = new RotationKalmanFilter();
    private RotationKalmanFilter zAngleFilter = new RotationKalmanFilter();
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
    double[] lastAngleData;

    private final AccelerationKalmanFilter filter = new AccelerationKalmanFilter();

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double[] filteredData;
            double[] sensorData = convertFloatsToDoubles(sensorEvent.values);
            if (calibrationMeter > CALIBRATIONTIME) {
                List<Double> measurements = new ArrayList<Double>();
                measurements.add(sensorData[0]);
                measurements.add(sensorData[1]);
                measurements.add(sensorData[2]);
                acceleration.setAccelerationComponents(measurements);

                rotation.countNewAccelerationByRotation(acceleration);

                acceleration.addToComponent(0, 0-accelerometerCalibrationX);
                acceleration.addToComponent(1, 0-accelerometerCalibrationY);
                acceleration.addToComponent(2, 0-accelerometerCalibrationZ);


                if(acceleration.getAccelerationComponents().get(0) < 0.1d && acceleration.getAccelerationComponents().get(0) > -0.1d)
                    acceleration.getAccelerationComponents().set(0, 0.0d);

                if(acceleration.getAccelerationComponents().get(1) < 0.1d && acceleration.getAccelerationComponents().get(1) > -0.1d)
                    acceleration.getAccelerationComponents().set(1, 0.0d);

                if(acceleration.getAccelerationComponents().get(2) < 0.1d && acceleration.getAccelerationComponents().get(2) > -0.1d)
                    acceleration.getAccelerationComponents().set(2, 0.0d);

//                if(sensorData[1] < 0.1d && sensorData[1] > -0.1d)
//                    sensorData[1] = 0.0d;
//
//                if(sensorData[2] < 0.1d && sensorData[2] > -0.1d)
//                    sensorData[2] = 0.0d;
//
//                List<Double> measurements = new ArrayList<Double>();
//                measurements.add(sensorData[0]);
//                measurements.add(sensorData[1]);
//                measurements.add(sensorData[2]);

                // Kalman
//                filteredData = filter.estimateMeasurements(measurements);
//                acceleration.readFromArray(filteredData);

//                acceleration.setAccelerationComponents(measurements);

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
                printAll();
                printAll();
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
                showCoordinates(sensorData);
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

                if(sensorData[0] < 0.01d && sensorData[0] > -0.01d)
                    sensorData[0] = 0.0d;

                if(sensorData[1] < 0.01d && sensorData[1] > -0.01d)
                    sensorData[1] = 0.0d;

                if(sensorData[2] < 0.01d && sensorData[2] > -0.01d)
                    sensorData[2] = 0.0d;

                // float[] deltaRotationVector = rotation.getDeltaRotationVector(sensorData, READINGRATE / 1000000d);
                // float[] deltaRotationMatrix = new float[9];

                // SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
                // float[] angleChange = new float[3];
                // SensorManager.getAngleChange(angleChange, deltaRotationMatrix, rotation.getRotationMatrix());

                // rotation.setRotationMatrix(deltaRotationMatrix);

                double [] angles = rotation.getDeltaRotationVector(sensorData, READINGRATE / 1000000d);
                double[] degrees = Rotation.getDegreesFromRadians(angles);
                double[] rate = Rotation.getDegreesPerSec(degrees, READINGRATE / 1000000d);

                double[] fixedDegrees = new double[angles.length];
                fixedDegrees[0] = xAngleFilter.getAngle(degrees[0], rate[0], READINGRATE / 1000000d);
                fixedDegrees[1] = yAngleFilter.getAngle(degrees[1], rate[1], READINGRATE / 1000000d);
                fixedDegrees[2] = zAngleFilter.getAngle(degrees[2], rate[2], READINGRATE / 1000000d);
                double[] updatedAngles = Rotation.getRadiansFromDegrees(fixedDegrees);
                rotation.updateWithSensorData(updatedAngles);

                gyroscopeTitle.setText("Żyroskop: ");

//                coordinates.countCoordinatesOnRotation(rotation);

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
        velocityXValue = findViewById(R.id.velocityXValue);
        coordinatesYValue = findViewById(R.id.coordinatesYValue);
        velocityYValue = findViewById(R.id.velocityYValue);
        coordinatesZValue = findViewById(R.id.coordinatesZValue);
        velocityZValue = findViewById(R.id.velocitysZValue);

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
        velocityXValue.setText("vx: " + String.format("%.4f", velocity.getVelocityComponents().get(0)));
        coordinatesYValue.setText("y: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(1)));
        velocityYValue.setText("vy: " + String.format("%.4f", velocity.getVelocityComponents().get(1)));
        coordinatesZValue.setText("z: " + String.format("%.4f", coordinates.getCoordinatesComponents().get(2)));
        velocityZValue.setText("vz: " + String.format("%.4f", velocity.getVelocityComponents().get(2)));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void showRotationData(double [] gyroscopeData) {
        gyroscopeXValue.setText("x: " + String.format("%.4f rad/s", gyroscopeData[0]));
        gyroscopeYValue.setText("y: " + String.format("%.4f rad/s", gyroscopeData[0]));
        gyroscopeZValue.setText("z: " + String.format("%.4f rad/s", gyroscopeData[1]));
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

    private void printAll() {
        System.out.println("Acceleration:");
        System.out.println(acceleration.getAccelerationComponents().toString());
        System.out.println("Velocity:");
        System.out.println(velocity.getVelocityComponents().toString());
        System.out.println("Distance:");
        System.out.println(distance.getDistanceComponents().toString());
        System.out.println("Coordinates:");
        System.out.println(coordinates.getCoordinatesComponents().toString());
        System.out.println("Rotation:");
        System.out.println(rotation.getRotationComponents().toString());
    }
}