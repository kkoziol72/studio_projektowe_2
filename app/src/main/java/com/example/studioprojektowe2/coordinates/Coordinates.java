package com.example.studioprojektowe2.coordinates;


public class Coordinates {

    private Double x = 0.0;
    private Double y = 0.0;
    private Double z = 0.0;

    public Coordinates() {}

    public Coordinates(Double x, Double y, Double z) {
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

    public void updateCoordinates(Distance distance) {
        this.x = this.x + distance.getX();
        this.y = this.y + distance.getY();
        this.z = this.z + distance.getZ();
    }

    public void getCoordinates(Acceleration acceleration, Double time, Distance distance,
                               Velocity velocity) {
        printCoordinates();

        distance.updateDistance(acceleration, time, velocity);
        velocity.updateVelocity(acceleration, time);
        updateCoordinates(distance);
    }

    public void printCoordinates() {
        System.out.println("x = " + this.x.toString());
        System.out.println("y = " + this.y.toString());
        System.out.println("z = " + this.z.toString());
        System.out.println();
    }

    // how to use it, in while loop - "stop button" not pressed or something like that
    public static void main(String [] args) {
        Acceleration acceleration = new Acceleration(1.0,1.0,1.0);
        Distance distance = new Distance();
        Velocity velocity = new Velocity();

        Coordinates coordinates = new Coordinates();

        int iter = 10;

        while (iter > 0) {
            coordinates.getCoordinates(acceleration, 1.0, distance, velocity);
            iter--;
        }
    }

}
