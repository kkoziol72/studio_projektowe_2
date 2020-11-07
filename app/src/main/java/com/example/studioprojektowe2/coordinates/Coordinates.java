package com.example.studioprojektowe2.coordinates;

import java.util.ArrayList;
import java.util.List;

public class Coordinates {

    private List<Double> coordinatesComponents;

    public Coordinates() {
        this.coordinatesComponents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.coordinatesComponents.add(0.0);
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

    public void updateCoordinates(Distance distance) throws Exception {
        if (distance.getDistanceComponents().size() >= this.coordinatesComponents.size()) {
            for (int i = 0; i < this.getCoordinatesComponents().size(); i++) {
                this.getCoordinatesComponents().set(i, this.coordinatesComponents.get(i) + distance.getDistanceComponents().get(i));
            }
        }
        else {
            throw new Exception("Number of distance components < Number of coordinates components");
        }
    }

    public void setCoordinates(Acceleration acceleration, Double time, Distance distance,
                               Velocity velocity) throws Exception {
        distance.updateDistance(acceleration, time, velocity);
        velocity.updateVelocity(acceleration, time);
        updateCoordinates(distance);
    }

    public void setCoordinatesTo0() {
        for (int i = 0; i < this.coordinatesComponents.size(); i++) {
            this.coordinatesComponents.set(i, 0.0);
        }
    }

}
