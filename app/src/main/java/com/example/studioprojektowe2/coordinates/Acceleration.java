package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;

public class Acceleration {

    private List<Double> accelerationComponents;

    public Acceleration() {
        this.accelerationComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.accelerationComponents.add(0.0);
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
            this.accelerationComponents.set(i, 0.0);
        }
    }

    public double[] toArray() {
        double [] array = new double[this.accelerationComponents.size()];
        for (int i = 0; i < this.accelerationComponents.size(); i++) {
            array[i] = this.accelerationComponents.get(i);
        }
        return array;
    }

    public void readFromArray(double[] data) throws Exception {
        if (this.accelerationComponents.size() >= data.length) {
            for (int i = 0; i < data.length; i++) {
                this.accelerationComponents.set(i, data[i]);
            }
        }
        else {
            throw new Exception("Number of acceleration components < Number of data elements");
        }
    }

}
