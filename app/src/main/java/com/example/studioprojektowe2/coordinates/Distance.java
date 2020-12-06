package com.example.studioprojektowe2.coordinates;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Distance {

    private List<Double> distanceComponents;

    public Distance() {
        this.distanceComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.distanceComponents.add(0.0d);
        }
    }

    public Distance(List<Double> distanceComponents) {
        this.distanceComponents = distanceComponents;
    }

    public List<Double> getDistanceComponents() {
        return distanceComponents;
    }

    public void setDistanceComponents(List<Double> distanceComponents) {
        this.distanceComponents = distanceComponents;
    }
    private static int i = 0;
    public void updateDistance(Acceleration acceleration, Double time, Velocity velocity) {
        if (acceleration.getAccelerationComponents().size() >= this.distanceComponents.size() && velocity.getVelocityComponents().size() >= this.distanceComponents.size()) {
            for (int i = 0; i < this.distanceComponents.size(); i++) {
                this.distanceComponents.set(i, (velocity.getVelocityComponents().get(i) * time) + (0.5d * acceleration.getAccelerationComponents().get(i) * time * time));
            }
        }
        else {
            for (int i = 0; i < acceleration.getAccelerationComponents().size(); i++) {
                this.distanceComponents.set(i, (velocity.getVelocityComponents().get(i) * time) + (0.5d * acceleration.getAccelerationComponents().get(i) * time * time));
            }
        }
    }

    public void setDistanceTo0() {
        for (int i = 0; i < this.distanceComponents.size(); i++) {
            this.distanceComponents.set(i, 0.0d);
        }
    }

}