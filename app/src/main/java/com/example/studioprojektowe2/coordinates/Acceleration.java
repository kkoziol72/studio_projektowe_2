package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;


public class Acceleration {

    private List<Double> accelerationComponents;

    public Acceleration() {
        this.accelerationComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.accelerationComponents.add(0.0d);
        }
    }

    public Acceleration(List<Double> accelerationComponents) {
        this.accelerationComponents = accelerationComponents;
    }

    public List<Double> getAccelerationComponents() {
        return accelerationComponents;
    }

    public void setAccelerationComponents(List<Double> accelerationComponents) {
        this.accelerationComponents = accelerationComponents;
    }

    public void setAccelerationTo0() {
        for (int i = 0; i < this.accelerationComponents.size(); i++) {
            this.accelerationComponents.set(i, 0.0d);
        }
    }

    public double[] toArray() {
        double [] array = new double[this.accelerationComponents.size()];
        for (int i = 0; i < this.accelerationComponents.size(); i++) {
            array[i] = this.accelerationComponents.get(i);
        }
        return array;
    }

    public void readFromArray(double[] data) {
        if (this.accelerationComponents.size() >= data.length) {
            for (int i = 0; i < data.length; i++) {
                this.accelerationComponents.set(i, data[i]);
            }
        }
        else {
            for (int i = 0; i < this.accelerationComponents.size(); i++) {
                this.accelerationComponents.set(i, data[i]);
            }
        }
    }

    public void updateAccelerationUsingRotation(Rotation rotation) {
        double xAngleInRadians = Math.toRadians(rotation.getRotationComponents().get(0));
        double yAngleInRadians = Math.toRadians(rotation.getRotationComponents().get(1));
        double zAngleInRadians = Math.toRadians(rotation.getRotationComponents().get(2));

        // matrix
        double [][] xRotation = new double[][] {
                {1, 0, 0},
                {0, Math.cos(xAngleInRadians), 0-Math.sin(xAngleInRadians)},
                {0, Math.sin(xAngleInRadians), Math.cos(xAngleInRadians)}
        };
        double [][] yRotation = new double[][] {
                {Math.cos(yAngleInRadians), 0, Math.sin(yAngleInRadians)},
                {0, 1, 0},
                {0-Math.sin(yAngleInRadians), 0, Math.cos(yAngleInRadians)}
        };
        double [][] zRotation = new double[][] {
                {Math.cos(zAngleInRadians), 0-Math.sin(zAngleInRadians), 0},
                {Math.sin(zAngleInRadians), Math.cos(zAngleInRadians), 0},
                {0, 0, 1}
        };

        // transform by x
        List<Double> finalAcc = new ArrayList<>();
        for (double[] m : xRotation) {
            double value = 0d;
            for (int k = 0; k < m.length; k++) {
                value += m[k] * accelerationComponents.get(k);
            }
            finalAcc.add(value);
        }
        setAccelerationComponents(finalAcc);

        // transform by y
        finalAcc = new ArrayList<>();
        for (double[] m : yRotation) {
            double value = 0d;
            for (int k = 0; k < m.length; k++) {
                value += m[k] * accelerationComponents.get(k);
            }
            finalAcc.add(value);
        }
        setAccelerationComponents(finalAcc);

        // transform by z
        finalAcc = new ArrayList<>();
        for (double[] m : zRotation) {
            double value = 0d;
            for (int k = 0; k < m.length; k++) {
                value += m[k] * accelerationComponents.get(k);
            }
            finalAcc.add(value);
        }
        setAccelerationComponents(finalAcc);
    }

}