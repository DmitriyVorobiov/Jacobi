package jacobi;


import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by Palladin on 28.11.2017.
 */
public abstract class Ortho implements Serializable {

    protected static final int C = 2;
    private int k = 2;
    private int b = 2;
    private double g = 1;

    @Nullable
    public Double fact(int numerator, int denominator)
    {
        if(numerator < 0 || denominator < 0) return null;
        if(numerator == denominator) return 1.;

        double res = 1.;

        if(numerator > denominator)
        {
            for(double i = denominator + 1; i <= numerator; i++)
                res*=i;
            return res;
        }

        for(double i = numerator + 1; i <= denominator; i++)
            res*=i;
        return 1. / res;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Nullable
    public Double fact(int num)
    {
        return fact(num, 1);
    }

    @Nullable
    protected Double combinations(int k, int s)
    {
        Double res;
        if(s == 0) return 1.;

        if(k - s <= s)
        {
            res = fact(k + s, k - s);
            if(s > 1) {
                Double fact_s = fact(s);
                res /= fact_s;
                res /= fact_s;
            }
        }
        else
        {
            res = fact(k + s, s);
            res /= fact(k - s);
            if(s > 1) res /= fact(s);
        }

        return res;
    }

    protected double minus_1_pow(int pow)
    {
        if(pow % 2 == 0) return 1;
        return -1;
    }

    protected Complex summationComplex(int from, int to, Function<Integer, Complex> func)
    {
        Complex sum = new Complex();

        for(int i = from; i <= to; i++)
            sum.addSelf(func.apply(i));

        return sum;
    }

    protected Complex productionComplex(int from, int to, Function<Integer, Complex> func)
    {
        Complex prod = new Complex(1);

        for(int i = from; i <= to; i++)
            prod.multSelf(func.apply(i));

        return prod;
    }

    protected double summation(int from, int to, Function<Integer, Double> func)
    {
        double sum = 0;

        for(int i = from; i <= to; i++)
            sum += func.apply(i);

        return sum;
    }

    protected double production(int from, int to, Function<Integer, Double> func)
    {
        double prod = 1;

        for(int i = from; i <= to; i++)
            prod *= func.apply(i);

        return prod;
    }

    public abstract Complex val(double w);

    public double derivative(double point, double delta, Function<Double, Double> func)
    {
        return (func.apply(point) - func.apply(point + delta)) / delta;
    }

    public double derivative(double current, double next, double delta)
    {
        return (current - next) / delta;
    }

    public double derivative2(double prev, double cur, double next, double delta)
    {
        return (prev - 2*cur + next) /  (delta * delta);
    }

    private int sign(double val)
    {
        return val < 0 ? -1 : 1;
    }

    private boolean inCorridor(double d, double val)
    {
        return val <= d && val >= -d;
    }

    public Threshold getDwN(double d)
    {
        Threshold res = new Threshold();
        double dw = 0.1;
        int i;
        double prev = val(0).Re;
        double cur = val(1 * dw ).Re;
        double next;
        double der2_max = 0;
        double sn_prev = sign(prev);
        double sn_cur;
        double der_sn = 0;

        boolean entered = false;
        for(i = 1; true; i++)
        {
            next = val(dw * (i+1)).Re;
            der2_max = Math.max(Math.abs(derivative2(prev, cur, next, dw)), der2_max);

            //here we try to count how many times function crosses 0.
            //if it`s eq. k or more, it means we don`t need to explore further
            //********************************
            sn_cur = sign(cur);
            if(sn_prev != sn_cur) res.setZeroCrossCounter(res.getZeroCrossCounter()+1);

            if(res.getZeroCrossCounter() >= k && inCorridor(d, cur))  break;

            sn_prev = sn_cur;
            //********************************

            //but sometimes we,ve already entered the corridor, but haven`t crossed 0 enough times.
            //So here we check if our func is inside the corridor and wait for changing sign of derivative
            //********************************
            if(inCorridor(d, prev))
            {
                if(!entered)
                {
                    der_sn = sign(derivative(prev, cur, dw));
                    entered = true;
                }
                else if(der_sn != sign(derivative(prev, cur, dw))) break;
            }
            else entered = false;

            prev = cur;
            cur = next;
            //********************************
        }

        res.setDw(Math.sqrt((8*d)/der2_max));
        double w_max = i * dw;
        res.setN((int)Math.floor(w_max/res.getDw() + 0.5));

        return res;
    }

}
