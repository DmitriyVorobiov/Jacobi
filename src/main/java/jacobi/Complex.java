package jacobi;

import java.util.Objects;

/**
 * Created by Palladin on 25.11.2017.
 */
public class Complex {

    public double Re;
    public double Im;


    public Complex(double real, double imag) {
        Re = real;
        Im = imag;
    }

    public Complex(double real) {
        Re = real;
        Im = 0;
    }

    public Complex() {
        Re = 0;
        Im = 0;
    }

    public String toString() {
        if (Im == 0) return Re + "";
        if (Re == 0) return Im + "i";
        if (Im < 0) return Re + " - " + (-Im) + "i";
        return Re + " + " + Im + "i";
    }

    public double mag() {
        return Math.hypot(Re, Im);
    }

    public double phase() {
        return Math.atan2(Im, Re);
    }

    public Complex add(Complex b) {
        return new Complex(Re + b.Re, Im + b.Im);
    }

    public void addSelf(Complex b) {
        Re += b.Re;
        Im += b.Im;
    }

    public Complex sub(Complex b) {
        return new Complex(Re - b.Re, Im - b.Im);
    }

    public void subSelf(Complex b) {
        Re -= b.Re;
        Im -= b.Im;
    }

    public Complex mult(Complex b) {
        return new Complex(Re * b.Re - Im * b.Im, Re * b.Im + Im * b.Re);
    }

    public Complex mult(double num) {
        return new Complex(Re * num, Im * num);
    }

    public static Complex mult(Complex a, Complex b) {
        return new Complex(a.Re * b.Re - a.Im * b.Im, a.Re * b.Im + a.Im * b.Re);
    }

    public void multSelf(Complex b) {
        double re = Re * b.Re - Im * b.Im;
        Im = Re * b.Im + Im * b.Re;
        Re = re;
    }

    public void multSelf(double num) {
        Re *= num;
        Im *= num;
    }

    public Complex conjugate() {
        return new Complex(Re, -Im);
    }

    // return a new jacobi.Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = Re * Re + Im * Im;
        return new Complex(Re / scale, -Im / scale);
    }

    // return a / b
    public Complex div(Complex b) {
        return mult(b.reciprocal());
    }

    public Complex div(double num) {
        return new Complex(Re / num, Im / num);
    }

    public static Complex div(Complex a, Complex b) {
        return a.mult(b.reciprocal());
    }

    public void divSelf(Complex b) {
        multSelf(b.reciprocal());
    }

    public void divSelf(double num) {
        Re /= num;
        Im /= num;
    }

    // return a new jacobi.Complex object whose value is the complex exponential of this
    public static Complex exp(Complex c) {
        return new Complex(Math.exp(c.Re) * Math.cos(c.Im), Math.exp(c.Re) * Math.sin(c.Im));
    }

    // return a new jacobi.Complex object whose value is the complex sine of this
    public static Complex sin(Complex c) {
        return new Complex(Math.sin(c.Re) * Math.cosh(c.Im), Math.cos(c.Re) * Math.sinh(c.Im));
    }


    // return a new jacobi.Complex object whose value is the complex cosine of this
    public static Complex cos(Complex c) {
        return new Complex(Math.cos(c.Re) * Math.cosh(c.Im), -Math.sin(c.Re) * Math.sinh(c.Im));
    }

    // return a new jacobi.Complex object whose value is the complex tangent of this
    public static Complex tan(Complex c) {
        return sin(c).div(cos(c));
    }

    public static Complex ln(Complex c) {
        return new Complex(Math.log(c.mag()), c.phase());
    }

    public static Complex atan(Complex c) {

        if (c.Re == 0 && c.Im == 1) return new Complex(0, 0.34657359028);
        if (c.Re == 0 && c.Im == -1) return new Complex(0, -0.34657359028);

        Complex i = new Complex(0, 1);
        Complex iz = c.mult(i);
        Complex one = new Complex(1, 0);
        return ln(one.sub(iz)).sub(ln(one.add(iz))).mult(i).div(2);
    }


    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.Re == that.Re) && (this.Im == that.Im);
    }

    // See Section 3.3.
    public int hashCode() {
        return Objects.hash(Re, Im);
    }

}
