package com.example.studioprojektowe2.coordinates;

import android.hardware.SensorManager;

import static java.lang.Math.sin;


public class Rotation {

    long lastTimestamp = 0;
    float[] prevRotationMatrix = new float[9];

    private Float x = 0.0F;
    private Float y = 0.0F;
    private Float z = 0.0F;

    private double minOmega = 0;

    public Rotation() {}

    public Rotation(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getXAngle() {
        return x;
    }

    public void setXAngle(Float x) {
        this.x = x;
    }

    public Float getYAngle() {
        return y;
    }

    public void setYAngle(Float y) {
        this.y = y;
    }

    public Float getZAngle() {
        return z;
    }

    public void setZAngle(Float z) {
        this.z = z;
    }

    public void updateAngles(double dataX, double dataY, double dataZ, long timestamp) {

        float NS2S = 1.0f / 1000000000.0f;
        float[] deltaRotationVector = new float[4];
        float[] deltaRotationMatrix = new float[9];

        if (lastTimestamp != 0) {
            float diffInSeconds = (timestamp - this.lastTimestamp) * NS2S;
            double omega = Math.sqrt(dataX*dataX + dataY*dataY + dataZ*dataZ);
//            if (dataX> 0.01 || dataY > 0.01 || dataZ > 0.01) {
//                if (minOmega == 0) {
//                    minOmega = omega;
//                    System.out.println(minOmega);
//                }
//                else if (minOmega > omega) {
//                    minOmega = omega;
//                    System.out.println(minOmega);
//                }
//            }

            dataX /= omega;
            dataY /= omega;
            dataZ /= omega;

            double thetaOverTwo = omega * diffInSeconds / 2.0f;
            double sinThetaOverTwo = sin(thetaOverTwo);
            double cosThetaOverTwo = Math.cos(thetaOverTwo);
            deltaRotationVector[0] = (float) (sinThetaOverTwo * dataX);
            deltaRotationVector[1] = (float) (sinThetaOverTwo * dataY);
            deltaRotationVector[2] = (float) (sinThetaOverTwo * dataZ);
            deltaRotationVector[3] = (float) (cosThetaOverTwo);

            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            float[] angleChange = new float[3];
            SensorManager.getAngleChange(angleChange, deltaRotationMatrix, prevRotationMatrix);

            this.x = this.x + angleChange[1];
            this.y = this.y + angleChange[2];
            this.z = this.z + angleChange[0];

//            this.x = this.x + dataX * diffInSeconds;//- previousDataX;
//            this.y = this.y + dataY * diffInSeconds;//- previousDataY;
//            this.z = this.z + dataZ * diffInSeconds;//- previousDataZ;
        }
        this.lastTimestamp = timestamp;
        this.prevRotationMatrix = deltaRotationMatrix;

    }

    public void setAnglesTo0() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

}
