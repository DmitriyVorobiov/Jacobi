package action;

import jacobi.Complex;
import jacobi.Jacobi3;
import jacobi.Ortho;
import com.opensymphony.xwork2.ActionSupport;

public class JacobiAction extends ActionSupport {

    private String k;
    private String b;
    private String g;
    private String d;

    public String execute() {
        int k = Integer.valueOf(getK());
        int b = Integer.valueOf(getB());
        double g = Double.valueOf(getG());
        double d =Double.valueOf(getD());
        Complex func;
        Ortho jacobi = new Jacobi3();
        Ortho.Threshold data = jacobi.calc_dw_and_n(Double.valueOf(d), Integer.valueOf(k), Integer.valueOf(b), Double.valueOf(g));
        for (int i = 0; i <= data.N; i++) {
            func = jacobi.val(k, b, g, data.dw * i);
            System.out.println((func.Re + "; " + func.Im + ";\n").replace('.', ','));
        }
        return SUCCESS;
    }

    @Override
    public void validate() {

    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
