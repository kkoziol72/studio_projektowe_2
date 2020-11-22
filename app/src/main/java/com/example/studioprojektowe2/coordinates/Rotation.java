package com.example.studioprojektowe2.coordinates;

import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class Rotation {

    private List<Double> rotationComponents;

    // private float[] rotationMatrix = new float[9];

    public Rotation() {
        rotationComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            rotationComponents.add(0.0d);
        }
    }

    public Rotation(List<Double> rotationComponents) {
        this.rotationComponents = rotationComponents;
    }

    public Rotation(double[] sensorData) {
        rotationComponents = new ArrayList<>();
        for (double sensorDatum : sensorData) {
            rotationComponents.add(sensorDatum);
        }
    }

    public List<Double> getRotationComponents() {
        return rotationComponents;
    }

    public void setRotationComponents(List<Double> rotationComponents) {
        this.rotationComponents = rotationComponents;
    }

//    public float[] getRotationMatrix() { return rotationMatrix; }
//
//    public void setRotationMatrix(float [] rotationMatrix) { this.rotationMatrix = rotationMatrix; }

    public List<Double> getDegreesFromRadians() {
        List <Double> componentsDegress = new ArrayList<>();

        for (int i = 0; i < this.rotationComponents.size(); i++) {
            componentsDegress.add(this.rotationComponents.get(i) * 180.0d / Math.PI);
        }

        return componentsDegress;
    }

    public static double[] getRadiansFromDegrees(double[] degrees) {
        double[] radians = new double[degrees.length];
        for (int i = 0; i < radians.length; i++) {
            radians[i] = degrees[i] / 180.0d * Math.PI;
        }
        return radians;
    }

    public static double[] getDegreesFromRadians(double[] radians) {
        double[] degrees = new double[radians.length];
        for (int i = 0; i < radians.length; i++) {
            degrees[i] = radians[i] * 180.0d/Math.PI;
        }
        return degrees;
    }

    public static double[] getDegreesPerSec(double[] degrees, double time) {
        double[] degreesPerSec = new double[degrees.length];
        for (int i = 0; i < degreesPerSec.length; i++) {
            degreesPerSec[i] = degrees[i] / time;
        }
        return degreesPerSec;
    }

    public double[] getDeltaRotationVector(double [] sensorData, double t) {
//        float[] deltaRotationVector = new float[4];
//
//        double axisX = sensorData[0];
//        double axisY = sensorData[1];
//        double axisZ = sensorData[2];
//
//        double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);
//
//        if(omegaMagnitude != 0){
//            axisX /= omegaMagnitude;
//            axisY /= omegaMagnitude;
//            axisZ /= omegaMagnitude;
//        }
//
//        double thetaOverTwo = omegaMagnitude * t / 2.0f;
//        double sinThetaOverTwo = Math.sin(thetaOverTwo);
//        double cosThetaOverTwo = Math.cos(thetaOverTwo);
//        deltaRotationVector[0] = (float)(sinThetaOverTwo * axisX);
//        deltaRotationVector[1] = (float)(sinThetaOverTwo * axisY);
//        deltaRotationVector[2] = (float)(sinThetaOverTwo * axisZ);
//        deltaRotationVector[3] = (float)(cosThetaOverTwo);
//
//        return deltaRotationVector;
        double [] angles = new double[sensorData.length];
        for (int i = 0; i < sensorData.length; i++) {
            angles[i] = sensorData[i] * t;
        }
        return angles;
    }

    public void updateWithSensorData(double[] angleChange) {
        if (angleChange.length <= this.rotationComponents.size()) {
            for (int i = 0; i < angleChange.length; i++) {
                this.rotationComponents.set(i, this.rotationComponents.get(i) + angleChange[i]);
            }
        }
        else {
            for (int i = 0; i < this.rotationComponents.size(); i++) {
                this.rotationComponents.set(i, this.rotationComponents.get(i) + angleChange[i]);
            }
        }
    }

    public void setAnglesTo0() {
        for (int i = 0; i < this.rotationComponents.size(); i++) {
            this.rotationComponents.set(i, 0.0d);
        }
    }

}
