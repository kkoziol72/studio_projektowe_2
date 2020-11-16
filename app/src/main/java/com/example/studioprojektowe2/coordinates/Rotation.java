package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;

public class Rotation {

    private List<Double> rotationComponents;

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

    public List<Double> getDegreesFromRadians() {
        List <Double> componentsDegress = new ArrayList<>();

        for (int i = 0; i < this.rotationComponents.size(); i++) {
            componentsDegress.add(this.rotationComponents.get(i) * 180.0d / Math.PI);
        }

        return componentsDegress;
    }

    public void updateWithSensorData(double [] sensorData) {
        if (sensorData.length <= this.rotationComponents.size()) {
            for (int i = 0; i < sensorData.length; i++) {
                this.rotationComponents.set(i, this.rotationComponents.get(i) + sensorData[i]);
            }
        }
        else {
            for (int i = 0; i < this.rotationComponents.size(); i++) {
                this.rotationComponents.set(i, this.rotationComponents.get(i) + sensorData[i]);
            }
        }
    }

    public void setAnglesTo0() {
        for (int i = 0; i < this.rotationComponents.size(); i++) {
            this.rotationComponents.set(i, 0.0d);
        }
    }

}
