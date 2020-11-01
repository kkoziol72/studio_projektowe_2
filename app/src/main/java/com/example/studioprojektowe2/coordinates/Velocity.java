package com.example.studioprojektowe2.coordinates;


public class Velocity {

    private Float v_x = 0.0F;
    private Float v_y = 0.0F;
    private Float v_z = 0.0F;

    public Velocity() {}

    public Velocity(Float v_x, Float v_y, Float v_z) {
        this.v_x = v_x;
        this.v_y = v_y;
        this.v_z = v_z;
    }

    public Float getV_x() {
        return v_x;
    }

    public void setV_x(Float v_x) {
        this.v_x = v_x;
    }

    public Float getV_y() {
        return v_y;
    }

    public void setV_y(Float v_y) {
        this.v_y = v_y;
    }

    public Float getV_z() {
        return v_z;
    }

    public void setV_z(Float v_z) {
        this.v_z = v_z;
    }

    public void updateVelocity(Acceleration acceleration, Float time) {
        this.v_x = this.v_x + (acceleration.getA_x() * time);
        this.v_y = this.v_y + (acceleration.getA_y() * time);
        this.v_z = this.v_z + (acceleration.getA_z() * time);
    }

    public void setVelocityTo0() {
        this.v_x = 0.0F;
        this.v_y = 0.0F;
        this.v_z = 0.0F;
    }

}
