package jacobi.test;

import jacobi.Complex;
import jacobi.Ortho;

/**
 * Created by Palladin on 24.12.2017.
 */
class SpecialCase4 extends SpecialCase
{
    @Override
    public Complex val(double w)
    {
        Complex prod = productionComplex(0, getB(), p -> new Complex((2* p + 9)*C*getG()/2, w));

        Complex res = new Complex(fact(getB() + 4) * (getB() + 9) * Math.pow(C*getG(), getB()+1));

        Complex numerator = new Complex(C*getG()/2, -w);

        res.divSelf(prod.mult(24));

        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re *= 3;
        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re /= 3;
        numerator.Re *= 5;
        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re /= 5;
        numerator.Re *= 7;

        return res.mult(numerator.div(numerator.conjugate()));
    }
}
