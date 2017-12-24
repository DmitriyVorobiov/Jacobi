package jacobi;

/**
 * Created by Palladin on 28.11.2017.
 */
public class Jacobi3 extends Ortho
{

    public double fi(int k, int p, double w)
    {
        return Math.atan(2*w / ((2*(k+p) + 1)*C*getG()));
    }

    public Complex val(double w) {

        double prod = production(0, getB(), p-> Math.cos(fi(getK(), p, w)) / (2*(getK()+p) + 1));
        double sum = summation(0, getB(), p-> fi(getK(), p, w));
        Complex neg_i = new Complex(0, -1);

        if(getK() == 0)
            return new Complex(Math.pow(2, getB()+1) * fact(getB()+1) * prod).mult(Complex.exp(neg_i.mult(sum)));

        Complex res = new Complex(Math.pow(2, getB()+1) * (2*getK() + getB() + 1) * fact(getK()+getB(),getK()) * prod);

        return res.mult(Complex.exp(neg_i.mult(sum + 2*summation(0, getK()-1, s -> fi(s,0, w)))));
    }
}
