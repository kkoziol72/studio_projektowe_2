package com.example.studioprojektowe2.filter;

import com.example.studioprojektowe2.MainActivity;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.lang.reflect.Array;
import java.util.List;

public class AccelerationKalmanFilter {

    //macierz stanu
    private RealMatrix A;
    //macierz przejścia dla wejścia
    private RealMatrix B;
    //macierz przejścia dla mierzonej wielkości
    private RealMatrix H;
    //kowariancja szumu procesu
    private RealMatrix Q;
    //kowariancja szumu pomiaru
    private RealMatrix R;
    //stan
    private RealVector x;
    //PO - macierz kowariancji błędu
    private RealMatrix PO;

    //czas
    private final double dt = MainActivity.READINGRATE / 1000000.0d;

    private final double measurementNoise = 10d;

    private RealVector u = new ArrayRealVector(new double[] { 0.1d });

    private KalmanFilter filter;

    public AccelerationKalmanFilter() {
//        A = new Array2DRowRealMatrix(new double[][]{
//                { 1d, 0d, 0d, dt, 0d, 0d },    // A = [ 1 0 0 t 0 0 ]
//                { 0d, 1d, 0d, 0d, dt, 0d },    //     [ 0 1 0 0 t 0 ]
//                { 0d, 0d, 1d, 0d, 0d, dt },    //     [ 0 0 1 0 0 t ]
//                { 0d, 0d, 0d, 1d, 0d, 0d },    //     [ 0 0 0 1 0 0 ]
//                { 0d, 0d, 0d, 0d, 1d, 0d },    //     [ 0 0 0 0 1 0 ]
//                { 0d, 0d, 0d, 0d, 0d, 1d }     //     [ 0 0 0 0 0 1 ]
//        });

        A = new Array2DRowRealMatrix(new double[][] {
                {1d, 0d, 0d, dt, 0d, 0d, Math.pow(dt, 2d) / 2d, 0d, 0d},
                {0d, 1d, 0d, 0d, dt, 0d, 0d, Math.pow(dt, 2d) / 2d, 0d},
                {0d, 0d, 1d, 0d, 0d, dt, 0d, 0d, Math.pow(dt, 2d) / 2d},
                {0d, 0d, 0d, 1d, 0d, 0d, dt, 0d, 0d},
                {0d, 0d, 0d, 0d, 1d, 0d, 0d, dt, 0d},
                {0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, dt},
                {0d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d},
                {0d, 0d, 0d, 0d, 0d, 0d, 0d, 1d, 0d},
                {0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 1d}
        });

//        B = new Array2DRowRealMatrix(new double[][]{
//                { Math.pow(dt, 2d)/2d },       // B = [ t^2/2 ]
//                { Math.pow(dt, 2d)/2d },       //     [ t^2/2 ]
//                { Math.pow(dt, 2d)/2d },       //     [ t^2/2 ]
//                { dt },                        //     [   t   ]
//                { dt },                        //     [   t   ]
//                { dt }                         //     [   t   ]
//        });

        H = new Array2DRowRealMatrix(new double[][]{
                { 0d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d },   // H = [ 1 0 0 0 0 0 ]
                { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 1d, 0d },   //     [ 0 1 0 0 0 0 ]
                { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 1d }    //     [ 0 0 1 0 0 0 ]
        });

        Q = new Array2DRowRealMatrix(new double[][]{
                { 0.1d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
                { 0d, 0.1d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
                { 0d, 0d, 0.1d, 0d, 0d, 0d, 0d, 0d, 0d },
                { 0d, 0d, 0d, 0.1d, 0d, 0d, 0d, 0d, 0d },
                { 0d, 0d, 0d, 0d, 0.1d, 0d, 0d, 0d, 0d },
                { 0d, 0d, 0d, 0d, 0d, 0.1d, 0d, 0d, 0d },
                { 0d, 0d, 0d, 0d, 0d, 0d, 0.1d, 0d, 0d },
                { 0d, 0d, 0d, 0d, 0d, 0d, 0d,0.1d, 0d },
                { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0.1d},
                 //     [ 0       0   T^3/2   0     0    T^2   ]
        });

        R = new Array2DRowRealMatrix(new double[][] {
                { Math.pow(measurementNoise, 2d), 0d, 0d },           // R = [ n^2  0   0  ]
                { 0d, Math.pow(measurementNoise, 2d), 0d },           //     [ 0   n^2  0  ]
                { 0d, 0d, Math.pow(measurementNoise, 2d) }            //     [ 0    0  n^2 ]
        });

//        PO = new Array2DRowRealMatrix(new double[][]{
//                { 1d, 1d, 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d, 1d, 1d }
//        });
        x = new ArrayRealVector(new double[] { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d });

        ProcessModel processModel = new DefaultProcessModel(A, null, Q, x, null);
        MeasurementModel measurementModel = new DefaultMeasurementModel(H, R);
        filter = new KalmanFilter(processModel, measurementModel);
    }


    public double[] estimateMeasurements(List<Double> measurements) {
        filter.predict();
        Double[] array = new Double[measurements.size()];
        measurements.toArray(array);
        RealVector z = new ArrayRealVector(measurements.toArray(array));
        filter.correct(z);
        return filter.getStateEstimation();
    }
}