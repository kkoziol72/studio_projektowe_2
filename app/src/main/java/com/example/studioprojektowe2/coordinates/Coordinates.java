package com.example.studioprojektowe2.coordinates;


public class Coordinates {

    private Float x = 0.0F;
    private Float y = 0.0F;
    private Float z = 0.0F;

    public Coordinates() {}

    public Coordinates(Float x, Float y, Float z) {
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

    public void updateCoordinates(Distance distance) {
        this.x = this.x + distance.getX();
        this.y = this.y + distance.getY();
        this.z = this.z + distance.getZ();
    }

    public void updateCoordinateX(Distance distance) {
        this.x = this.x + distance.getX();
    }

    public void updateCoordinateY(Distance distance) {
        this.y = this.y + distance.getY();
    }

    public void updateCoordinateZ(Distance distance) {
        this.z = this.z + distance.getZ();
    }

    public void setCoordinates(Acceleration acceleration, Float time, Distance distance,
                               Velocity velocity) {
        distance.updateDistance(acceleration, time, velocity);
        velocity.updateVelocity(acceleration, time);
        updateCoordinates(distance);
    }

    public void setCoordinateX(Acceleration acceleration, Float time, Distance distance,
                               Velocity velocity) {
        distance.updateDistanceX(acceleration, time, velocity);
        velocity.updateVelocityX(acceleration, time);
        updateCoordinateX(distance);
    }

    public void setCoordinateY(Acceleration acceleration, Float time, Distance distance,
                               Velocity velocity) {
        distance.updateDistanceY(acceleration, time, velocity);
        velocity.updateVelocityY(acceleration, time);
        updateCoordinateY(distance);
    }

    public void setCoordinateZ(Acceleration acceleration, Float time, Distance distance,
                               Velocity velocity) {
        distance.updateDistanceZ(acceleration, time, velocity);
        velocity.updateVelocityZ(acceleration, time);
        updateCoordinateZ(distance);
    }

    public void printCoordinates() {
        System.out.println("x = " + this.x.toString());
        System.out.println("y = " + this.y.toString());
        System.out.println("z = " + this.z.toString());
        System.out.println();
    }

    public void setCoordinatesTo0() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

}
