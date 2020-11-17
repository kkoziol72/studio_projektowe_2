package com.example.studioprojektowe2.coordinates;

import android.util.Log;

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
        }
        else {
            for (int i = 0; i < distance.getDistanceComponents().size(); i++) {
                this.coordinatesComponents.set(i, this.coordinatesComponents.get(i) + distance.getDistanceComponents().get(i));
            }
        }
    }

    public void setCoordinates(Acceleration acceleration, Double time, Distance distance,
                               Velocity velocity) {
        distance.updateDistance(acceleration, time, velocity);
        velocity.updateVelocity(acceleration, time);
        if(acceleration.getAccelerationComponents().size() >= 3)
            Log.d("TAGG SEnSOR aCCC", "("+acceleration.getAccelerationComponents().get(0)+", "+acceleration.getAccelerationComponents().get(1)+","+acceleration.getAccelerationComponents().get(2)+")");
        if(velocity.getVelocityComponents().size() >= 3)
            Log.d("TAGG SEnSOR vellll", "("+velocity.getVelocityComponents().get(0)+", "+velocity.getVelocityComponents().get(1)+","+velocity.getVelocityComponents().get(2)+")");
        updateCoordinates(distance);
    }

    public void setCoordinatesTo0() {
        for (int i = 0; i < this.coordinatesComponents.size(); i++) {
            this.coordinatesComponents.set(i, 0.0d);
        }
    }

    public void countCoordinatesOnRotation(Rotation rotation) {
        double [][] axes = new double[][]{
                {1d, 0d, 0d},
                {0d, 1d, 0d},
                {0d, 0d, 1d}
        };

        for (int i = 0; i < rotation.getRotationComponents().size(); i++) {
            // angle in radians
            double angle = rotation.getRotationComponents().get(i);

            // s0
            double s0 = Math.cos(angle / 2.0d);

            // length of axis vector
            double sum = 0;
            for (int j = 0; j < axes[i].length; j++) {
                sum += Math.pow(axes[i][j], 2.0d);
            }
            double length_axis = Math.sqrt(sum);

            // x0, y0, z0
            double x0 = axes[i][0] * Math.sin(angle / 2.0d) / length_axis;
            double y0 = axes[i][1] * Math.sin(angle / 2.0d) / length_axis;
            double z0 = axes[i][2] * Math.sin(angle / 2.0d) / length_axis;

            // final matrix
            double [][] matrix = new double[][] {
                    {2d * (Math.pow(s0, 2d) + Math.pow(x0, 2d)) - 1d, 2d * (x0 * y0 - s0 * z0), 2d * (s0 * y0 + x0 * z0)},
                    {2d * (s0 * z0 + x0 * y0), 2d * (Math.pow(s0, 2d) + Math.pow(y0, 2d)) - 1d, 2d * (y0 * z0 - s0 * x0)},
                    {2d * (x0 * z0 - s0 * y0), 2d * (s0 * x0 + y0 * z0), 2d * (Math.pow(s0, 2d) + Math.pow(z0, 2d)) - 1d}
            };

            List<Double> finalCoords = new ArrayList<>();
            for (double[] m : matrix) {
                double value = 0d;
                for (int k = 0; k < m.length; k++) {
                    value += m[k] * this.coordinatesComponents.get(k);
                }
                finalCoords.add(value);
            }

            // final result
            this.coordinatesComponents = finalCoords;
        }
    }

}
