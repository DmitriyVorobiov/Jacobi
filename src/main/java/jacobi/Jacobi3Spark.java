package jacobi;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;

/**
 * Created by Pavel Chursin on 18.12.2017.
 *
 * Need to debug. It takes too much time to complete
 */

public class Jacobi3Spark extends Ortho {

    private JavaSparkContext jsc;

    public Jacobi3Spark() {
        SparkConf conf = new SparkConf().setAppName("Jacobi").setMaster("local");

//        jsc = new JavaSparkContext(conf);
//        JavaSparkContext.toSparkContext(jsc).st
        jsc = new JavaSparkContext(SparkContext.getOrCreate(conf));
    }

    public static double fi(double k, double p, double g, double w) {
        return Math.atan(2 * w / (2 * (k + p) + 1) * c * g);
    }

    public Complex val(int k, int b, double g, double w) {

        ArrayList<Double> steps = new ArrayList<>();
        for (int i = 0; i <= b; i++)
            steps.add(i+0.0);
        JavaRDD<Double> distSteps = jsc.parallelize(steps);
        Function2<Double, Double, Double> fiSum = (accum, n) -> (accum + fi(k, n.intValue(), g, w));
        Function2<Double, Double, Double> cosProd = (accum, n) ->
                (accum * Math.cos(fi(k, n, g, w)) / (2 * (k + n) + 1));

        //double prod = production(0, b, p -> Math.cos(fi(k, p, g, w)) / (2 * (k + p) + 1));
        //double prod = distSteps.reduce(cosProd);
        double prod = distSteps.map(dbl -> Math.cos(fi(k, dbl, g, w)) / (2 * (k + dbl) + 1))
                .reduce((accum, n) -> (accum * n));
        //double sum = summation(0, b, p -> fi(k, p, g, w));
//        double sum = distSteps.reduce(fiSum);
        double sum = distSteps.map(dbl -> fi(k, dbl, g, w)).reduce((accum, n) -> (accum + n));
        Complex neg_i = new Complex(0, -1);

        if (k == 0)
            return new Complex(Math.pow(2, b + 1) * fact(b + 1) * prod).mult(Complex.exp(neg_i.mult(sum)));

        Complex res = new Complex(Math.pow(2, b + 1) * (2 * k + b + 1) * fact(k + b, k) * prod);

        steps.clear();
        for (int i = 0; i <= k-1; i++)
            steps.add(i+0.0);
        distSteps = jsc.parallelize(steps);
        Function2<Double, Double, Double> fiSumOnRet = (accum, n) -> (accum + fi(n.intValue(), 0, g, w));

        //return res.mult(Complex.exp(neg_i.mult(sum + 2 * summation(0, k - 1, s -> fi(s, 0, g, w)))));
        //return res.mult(Complex.exp(neg_i.mult(sum + 2 * distSteps.reduce(fiSumOnRet))));
        return res.mult(Complex.exp(neg_i.mult(sum + 2 * distSteps.map(dbl -> fi(dbl, 0, g, w)).reduce((accum, n) -> (accum + n)))));
    }
}
