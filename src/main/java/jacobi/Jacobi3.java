package jacobi;

import java.io.Serializable;

/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi3 extends Ortho implements Serializable {

    public static double fi(int k, int p, double g, double w) {
        return Math.atan(2 * w / (2 * (k + p) + 1) * c * g);
    }

    public Complex val(int k, int b, double g, double w) {

        double prod = production(0, b, p -> Math.cos(fi(k, p, g, w)) / (2 * (k + p) + 1));
        double sum = summation(0, b, p -> fi(k, p, g, w));
        Complex neg_i = new Complex(0, -1);

        if (k == 0)
            return new Complex(Math.pow(2, b + 1) * fact(b + 1) * prod).mult(Complex.exp(neg_i.mult(sum)));

        Complex res = new Complex(Math.pow(2, b + 1) * (2 * k + b + 1) * fact(k + b, k) * prod);

        return res.mult(Complex.exp(neg_i.mult(sum + 2 * summation(0, k - 1, s -> fi(s, 0, g, w)))));
    }
}
