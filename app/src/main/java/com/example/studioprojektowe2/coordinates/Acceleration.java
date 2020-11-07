package com.example.studioprojektowe2.coordinates;


public class Acceleration {

    private Double a_x = 0.0;
    private Double a_y = 0.0;
    private Double a_z = 0.0;

    public Acceleration() {}

    public Acceleration(Double a_x, Double a_y, Double a_z) {
        this.a_x = a_x;
        this.a_y = a_y;
        this.a_z = a_z;
    }

    public Double getA_x() {
        return a_x;
    }

    public void setA_x(Double a_x) {
        this.a_x = a_x;
    }

    public Double getA_y() {
        return a_y;
    }

    public void setA_y(Double a_y) {
        this.a_y = a_y;
    }

    public Double getA_z() {
        return a_z;
    }

    public void setA_z(Double a_z) {
        this.a_z = a_z;
    }

    public void setAccelerationTo0() {
        this.a_x = 0.0;
        this.a_y = 0.0;
        this.a_z = 0.0;
    }

    public double[] toArray() {
        return new double[]{a_x, a_y, a_z};
    }

    public void readFromArray(double[] data) {
        this.a_x = data[0];
        this.a_y = data[1];
        this.a_z = data[2];
    }
}
