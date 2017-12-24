package jacobi.test;

import jacobi.Complex;
import jacobi.Ortho;

/**
 * Created by Palladin on 24.12.2017.
 */
class SpecialCase5 extends SpecialCase
{
    @Override
    public Complex val(double w)
    {
        Complex prod = productionComplex(0, getB(), p -> new Complex((2* p + 11)*C*getG()/2, w));

        Complex res = new Complex(fact(getB() + 5) * (getB() + 11) * Math.pow(C*getG(), getB()+1));

        Complex numerator = new Complex(C*getG()/2, -w);

        res.divSelf(prod.mult(120));

        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re *= 3;
        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re /= 3;
        numerator.Re *= 5;
        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re /= 5;
        numerator.Re *= 7;
        res.multSelf(numerator.div(numerator.conjugate()));
        numerator.Re /= 7;
        numerator.Re *= 9;

        return res.mult(numerator.div(numerator.conjugate()));
    }
}
