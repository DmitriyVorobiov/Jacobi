package jacobi;

import java.io.Serializable;

public class Threshold implements Serializable {
    private double dw = 0;
    private int N = 0;
    private int zeroCrossCounter = 0;
    private int k = 0;

    public double getDw() {
        return dw;
    }

    public void setDw(double dw) {
        this.dw = dw;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getZeroCrossCounter() {
        return zeroCrossCounter;
    }

    public void setZeroCrossCounter(int zeroCrossCounter) {
        this.zeroCrossCounter = zeroCrossCounter;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}