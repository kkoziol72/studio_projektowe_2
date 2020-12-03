package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;


public class Velocity {

    private List<Double> velocityComponents;

    public Velocity() {
        this.velocityComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.velocityComponents.add(0.0d);
        }
    }

    public Velocity(List<Double> velocityComponents) {
        this.velocityComponents = velocityComponents;
    }

    public List<Double> getVelocityComponents() {
        return velocityComponents;
    }

    public void setVelocityComponents(List<Double> velocityComponents) {
        this.velocityComponents = velocityComponents;
    }

    public void updateVelocity(Acceleration acceleration, Double time) {
        if (acceleration.getAccelerationComponents().size() >= this.velocityComponents.size()) {
            for (int i = 0; i < this.velocityComponents.size(); i++) {
                if(acceleration.getAccelerationComponents().get(i) == 0) {
                    this.velocityComponents.set(i, 0.0d);
                }
                else {
                    this.velocityComponents.set(i, this.velocityComponents.get(i) + (acceleration.getAccelerationComponents().get(i) * time));
                }
            }
        }
        else {
            for (int i = 0; i < acceleration.getAccelerationComponents().size(); i++) {
                if(acceleration.getAccelerationComponents().get(i) == 0) this.velocityComponents.set(i, 0.0d);
                else
                    this.velocityComponents.set(i, this.velocityComponents.get(i) + (acceleration.getAccelerationComponents().get(i) * time));            }
        }
    }

    public void setVelocityTo0() {
        for (int i = 0; i < this.velocityComponents.size(); i++) {
            this.velocityComponents.set(i, 0.0d);
        }
    }

}