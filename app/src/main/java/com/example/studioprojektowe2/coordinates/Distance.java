package com.example.studioprojektowe2.coordinates;


public class Distance {

    private Float x = 0.0F;
    private Float y = 0.0F;
    private Float z = 0.0F;

    public Distance() {}

    public Distance(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public void updateDistance(Acceleration acceleration, Float time, Velocity velocity) {
        this.x = this.x + velocity.getV_x() * time + 0.5F * acceleration.getA_x() * time * time;
        this.y = this.y + velocity.getV_y() * time + 0.5F * acceleration.getA_y() * time * time;
        this.z = this.z + velocity.getV_z() * time + 0.5F * acceleration.getA_z() * time * time;
    }

}