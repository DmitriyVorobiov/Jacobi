package jacobi;


import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Created by Palladin on 28.11.2017.
 */
public abstract class Ortho {

    protected final static int c = 2;

    public Double fact(int numerator, int denominator) {
        if (numerator < 0 || denominator < 0) return null;
        if (numerator == denominator) return Double.valueOf(1);

        int res = 1;

        if (numerator > denominator) {
            for (int i = denominator + 1; i <= numerator; i++)
                res *= i;
            return Double.valueOf(res);
        }

        for (int i = numerator + 1; i <= denominator; i++)
            res *= i;
        return 1.0 / res;
    }

    public Double fact(int num) {
        return fact(num, 1);
    }

    protected Double combinations(int k, int s) {
        Double res;
        if (k - s <= s) {
            res = fact(k + s, k - s);
            Double fact_s = fact(s);
            res /= fact_s;
            res /= fact_s;
        } else {
            res = fact(k + s, s);
            res /= fact(k - s);
            res /= fact(s);
        }

        return res;
    }

    protected int minus_1_pow(int pow) {
        if (pow % 2 == 0) return 1;
        return -1;
    }

    protected Complex summationComplex(int from, int to, Function<Integer, Complex> func) {
        Complex sum = new Complex();

        for (int i = from; i <= to; i++)
            sum.addSelf(func.apply(i));

        return sum;
    }

    protected Complex productionComplex(int from, int to, Function<Integer, Complex> func) {
        Complex prod = new Complex(1);

        for (int i = from; i <= to; i++)
            prod.multSelf(func.apply(i));

        return prod;
    }

    protected double summation(int from, int to, Function<Integer, Double> func) {
        double sum = 0;

        for (int i = from; i <= to; i++)
            sum += func.apply(i);

        return sum;
    }

    protected double production(int from, int to, Function<Integer, Double> func) {
        double prod = 1;

        for (int i = from; i <= to; i++)
            prod *= func.apply(i);

        return prod;
    }

    public abstract Complex val(int k, int b, double g, double w);

    public double derivative(double point, double delta, Function<Double, Double> func) {
        return (func.apply(point) - func.apply(point + delta)) / delta;
    }

    public double derivative(double current, double next, double delta) {
        return (current - next) / delta;
    }

    public double derivative2(double prev, double cur, double next, double delta) {
        return (prev - 2 * cur + next) / (delta * delta);
    }

    private int sign(double val) {
        return val < 0 ? -1 : 1;
    }

    private boolean inCorridor(double d, double val) {
        return val <= d && val >= -d;
    }

    public Threshold getDwN(double d, int k, int b, double g) {
        Threshold res = new Threshold();
        res.setK(k);
        double dw = 0.1;
        int i;
        double prev = val(k, b, g, 0).Re;
        double cur = val(k, b, g, 1 * dw).Re;
        double next;
        double der2_max = 0;
        double sn_prev = sign(prev);
        double sn_cur;
        double der_sn = 0;

        boolean entered = false;
        for (i = 1; true; i++) {
            next = val(k, b, g, dw * (i + 1)).Re;
            der2_max = Math.max(Math.abs(derivative2(prev, cur, next, dw)), der2_max);

            //here wi try to count how many times function crosses 0.
            //if it`s eq. k or more, it means we don`t need to explore further
            //********************************
            sn_cur = sign(cur);
            if (sn_prev != sn_cur) res.setZeroCrossCounter(res.getZeroCrossCounter() + 1);

            if (res.getZeroCrossCounter() >= k && inCorridor(d, cur)) break;

            sn_prev = sn_cur;
            //********************************


            //but sometimes we,ve already entered the corridor, but haven`t crossed 0 enough times.
            //So here we check if our func is inside the corridor and wait for changing sign of derivative
            //********************************
            if (inCorridor(d, prev)) {
                if (!entered) {
                    der_sn = sign(derivative(prev, cur, dw));
                    entered = true;
                } else if (der_sn != sign(derivative(prev, cur, dw))) break;
            } else entered = false;

            prev = cur;
            cur = next;
            //********************************
        }

        res.setDw(Math.sqrt((8 * d) / der2_max));
        double w_max = i * dw;
        res.setN((int) Math.floor(w_max / res.getDw() + 0.5));

        return res;
    }


}
