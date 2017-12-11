package jacobi;

/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi2 extends Ortho {

    public Complex val(int k, int b, double g, double w) {
        Complex prod = productionComplex(0, b, p ->
                new Complex((2 * (k + p) + 1) * c * g / 2, w));


        if (k == 0) return new Complex(Math.pow(c * g, b + 1) * fact(b + 1)).div(prod);

        Complex res = new Complex(Math.pow(c * g, b + 1) * (2 * k + b + 1) * fact(k + b, k)).div(prod);


        return res.mult(productionComplex(0, k - 1, s -> {
            Complex numerator = new Complex((2 * s + 1) * c * g / 2, -w);
            Complex denominator = numerator.conjugate();
            return numerator.div(denominator);
        }));
    }
}
