package com.example.studioprojektowe2.coordinates;

import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class Rotation {

    private List<Double> rotationComponents;

    private float[] rotationMatrix = new float[9];

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

    public float[] getRotationMatrix() { return rotationMatrix; }

    public void setRotationMatrix(float [] rotationMatrix) { this.rotationMatrix = rotationMatrix; }

    public List<Double> getDegreesFromRadians() {
        List <Double> componentsDegress = new ArrayList<>();

        for (int i = 0; i < this.rotationComponents.size(); i++) {
            componentsDegress.add(this.rotationComponents.get(i) * 180.0d / Math.PI);
        }

        return componentsDegress;
    }

    public float[] getDeltaRotationVector(double [] sensorData, double t) {
        float[] deltaRotationVector = new float[4];

        double axisX = sensorData[0];
        double axisY = sensorData[1];
        double axisZ = sensorData[2];

        double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

        axisX /= omegaMagnitude;
        axisY /= omegaMagnitude;
        axisZ /= omegaMagnitude;

        double thetaOverTwo = omegaMagnitude * t / 2.0f;
        double sinThetaOverTwo = Math.sin(thetaOverTwo);
        double cosThetaOverTwo = Math.cos(thetaOverTwo);
        deltaRotationVector[0] = (float)(sinThetaOverTwo * axisX);
        deltaRotationVector[1] = (float)(sinThetaOverTwo * axisY);
        deltaRotationVector[2] = (float)(sinThetaOverTwo * axisZ);
        deltaRotationVector[3] = (float)(cosThetaOverTwo);

        return deltaRotationVector;
    }

    public void updateWithSensorData(float[] angleChange) {
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
