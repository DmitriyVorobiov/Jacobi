package jacobi;

import java.io.Serializable;

/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi1 extends Ortho implements Serializable {

    public Complex val(int k, int b, double g, double w) {
        Complex prod = productionComplex(1, b, p ->
                new Complex((2 * (k + p) + 1) * c * g / 2, w));

        Complex sum = summationComplex(0, k, s ->
                new Complex(combinations(k, s) * minus_1_pow(s)).div(new Complex((2 * s + 1) * c * g / 2, w)));

        Complex res = new Complex(fact(k + b, k) * (2 * k + b + 1) * Math.pow(c * g, b + 1));

        return res.div(prod).mult(sum);
    }
}
