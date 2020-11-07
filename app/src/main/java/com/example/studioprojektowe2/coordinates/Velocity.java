package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;

public class Velocity {

    private List<Double> velocityComponents;

    public Velocity() {
        this.velocityComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.velocityComponents.add(0.0);
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

    public void updateVelocity(Acceleration acceleration, Double time) throws Exception {
        if (acceleration.getAccelerationComponents().size() >= this.getVelocityComponents().size()) {
            for (int i = 0; i < this.velocityComponents.size(); i++) {
                this.velocityComponents.set(i, this.getVelocityComponents().get(i) + (acceleration.getAccelerationComponents().get(i) * time));
            }
        }
        else {
            throw new Exception("Number of acceleration components < Number of velocity components");
        }
    }

    public void setVelocityTo0() {
        for (int i = 0; i < this.velocityComponents.size(); i++) {
            this.velocityComponents.set(i, 0.0);
        }
    }

}
