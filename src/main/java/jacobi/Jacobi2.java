package jacobi;


/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi2 extends Ortho
{

    public Complex val(double w) {
      Complex prod = productionComplex(0, getB(), p ->
                new Complex((2*(getK() + p) + 1)*C*getG()/2, w));



        if(getK() == 0) return new Complex( Math.pow(C*getG(), getB()+1) * fact(getB()+1)).div(prod);

        Complex res = new Complex(Math.pow(C*getG(), getB()+1) * (2*getK() + getB() + 1) * fact(getK() + getB(), getK())).div(prod);


        return res.mult(productionComplex(0, getK()-1, s -> {
            Complex numerator = new Complex((2*s + 1)*C*getG()/2, -w);
            Complex denominator = numerator.conjugate();
            return numerator.div(denominator);
        }));
    }
}
