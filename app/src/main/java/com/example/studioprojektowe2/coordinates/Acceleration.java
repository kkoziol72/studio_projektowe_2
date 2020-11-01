package com.example.studioprojektowe2.coordinates;


public class Acceleration {

    private Float a_x;
    private Float a_y;
    private Float a_z;

    public Acceleration() {}

    public Acceleration(Float a_x, Float a_y, Float a_z) {
        this.a_x = a_x;
        this.a_y = a_y;
        this.a_z = a_z;
    }

    public Float getA_x() {
        return a_x;
    }

    public void setA_x(Float a_x) {
        this.a_x = a_x;
    }

    public Float getA_y() {
        return a_y;
    }

    public void setA_y(Float a_y) {
        this.a_y = a_y;
    }

    public Float getA_z() {
        return a_z;
    }

    public void setA_z(Float a_z) {
        this.a_z = a_z;
    }

}
