package com.example.studioprojektowe2.coordinates;


public class Distance {

    private Double x = 0.0;
    private Double y = 0.0;
    private Double z = 0.0;

    public Distance() {}

    public Distance(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public void updateDistance(Acceleration acceleration, Double time, Velocity velocity) {
        this.x = (velocity.getV_x() * time) + (0.5 * acceleration.getA_x() * time * time);
        this.y = (velocity.getV_y() * time) + (0.5 * acceleration.getA_y() * time * time);
        this.z = (velocity.getV_z() * time) + (0.5 * acceleration.getA_z() * time * time);
    }

    public void setDistanceTo0() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

}