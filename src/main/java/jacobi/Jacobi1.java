package jacobi;

/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi1 extends Ortho
{

    public Complex val(double w)
    {
        Complex prod = productionComplex(1, getB(), p -> new Complex((2*(getK() + p) + 1)*C*getG()/2, w));

        Complex sum = summationComplex(0, getK(), s ->
                new Complex(combinations(getK(), s) * minus_1_pow(s)).div(new Complex((2*s + 1)*C*getG()/2, w)));

        Complex res = new Complex(fact(getK() + getB(), getK()) * (2*getK() + getB() + 1) * Math.pow(C*getG(), getB()+1));

        return res.div(prod).mult(sum);
    }
}
