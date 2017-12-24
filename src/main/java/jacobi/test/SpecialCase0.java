package jacobi.test;

import jacobi.Complex;
import jacobi.Ortho;

/**
 * Created by Palladin on 24.12.2017.
 */
class SpecialCase0 extends SpecialCase
{
    @Override
    public Complex val(double w)
    {
        Complex prod = productionComplex(0, getB(), p ->
                new Complex((2* p + 1)*C*getG()/2, w));

        Complex res = new Complex(fact(getB() + 1) * Math.pow(C*getG(), getB()+1));

        return res.div(prod);
    }
}
