package com.example.studioprojektowe2.coordinates;


public class Velocity {

    private Double v_x = 0.0;
    private Double v_y = 0.0;
    private Double v_z = 0.0;

    public Velocity() {}

    public Velocity(Double v_x, Double v_y, Double v_z) {
        this.v_x = v_x;
        this.v_y = v_y;
        this.v_z = v_z;
    }

    public Double getV_x() {
        return v_x;
    }

    public void setV_x(Double v_x) {
        this.v_x = v_x;
    }

    public Double getV_y() {
        return v_y;
    }

    public void setV_y(Double v_y) {
        this.v_y = v_y;
    }

    public Double getV_z() {
        return v_z;
    }

    public void setV_z(Double v_z) {
        this.v_z = v_z;
    }

    public void updateVelocity(Acceleration acceleration, Double time) {
        this.v_x = this.v_x + (acceleration.getA_x() * time);
        this.v_y = this.v_y + (acceleration.getA_y() * time);
        this.v_z = this.v_z + (acceleration.getA_z() * time);
    }

    public void setVelocityTo0() {
        this.v_x = 0.0;
        this.v_y = 0.0;
        this.v_z = 0.0;
    }

}
