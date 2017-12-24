package jacobi.test;

import jacobi.Complex;
import jacobi.Ortho;

/**
 * Created by Palladin on 24.12.2017.
 */
class SpecialCase1 extends SpecialCase
{
    @Override
    public Complex val(double w)
    {
        Complex prod = productionComplex(0, getB(), p -> new Complex((2* p + 3)*C*getG()/2, w));

        Complex res = new Complex(fact(getB() + 1) * (getB() + 3) * Math.pow(C*getG(), getB()+1));

        Complex numerator = new Complex(C*getG()/2, -w);

        return res.div(prod).mult(numerator.div(numerator.conjugate()));
    }
}
