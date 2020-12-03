package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;


public class Coordinates {

    private List<Double> coordinatesComponents;

    public Coordinates() {
        this.coordinatesComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.coordinatesComponents.add(0.0d);
        }
    }

    public Coordinates(List<Double> coordinatesComponents) {
        this.coordinatesComponents = coordinatesComponents;
    }

    public List<Double> getCoordinatesComponents() {
        return coordinatesComponents;
    }

    public void setCoordinatesComponents(List<Double> coordinatesComponents) {
        this.coordinatesComponents = coordinatesComponents;
    }

    public void updateCoordinates(Distance distance) {
        if (distance.getDistanceComponents().size() >= this.coordinatesComponents.size()) {
            for (int i = 0; i < this.coordinatesComponents.size(); i++) {
                this.coordinatesComponents.set(i, this.coordinatesComponents.get(i) + distance.getDistanceComponents().get(i));
            }
        } else {
            for (int i = 0; i < distance.getDistanceComponents().size(); i++) {
                this.coordinatesComponents.set(i, this.coordinatesComponents.get(i) + distance.getDistanceComponents().get(i));
            }
        }
    }

    public void setCoordinates(Acceleration acceleration, Double time, Distance distance,
                               Velocity velocity) {
        distance.updateDistance(acceleration, time, velocity);
        velocity.updateVelocity(acceleration, time);
        updateCoordinates(distance);
    }

    public void setCoordinatesTo0() {
        for (int i = 0; i < this.coordinatesComponents.size(); i++) {
            this.coordinatesComponents.set(i, 0.0d);
        }
    }

}
