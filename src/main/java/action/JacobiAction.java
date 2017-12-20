package action;

import jacobi.Complex;
import jacobi.Jacobi3;
import jacobi.Jacobi3Spark;
import jacobi.Ortho;

import java.io.*;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class JacobiAction extends ActionSupport {


    private String k;
    private String b;
    private String g;
    private String d;
    private String spark;
    private Exception exception;
    private ArrayList<Long> timings = new ArrayList<>();
    private File file;
    private ArrayList<Ortho.Threshold> serialThresholds = new ArrayList<>();

    public String execute() throws IOException {
        SparkConf conf = new SparkConf().setAppName("Jacobi").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(SparkContext.getOrCreate(conf));
        int k = Integer.valueOf(getK());
        int b = Integer.valueOf(getB());
        double g = Double.valueOf(getG());
        double d =Double.valueOf(getD());
        long start = 0L,end = 0L;
        boolean spark = Boolean.parseBoolean(getSpark());
        Complex func;
        Ortho jacobi = new Jacobi3();
        file = new File(DownloadResultsAction.FILENAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getName());
        BufferedWriter bw = new BufferedWriter(fw);
        if(spark) {
            ArrayList<Ortho.ThresholdSpark> thresholdSparks = new ArrayList<>();
            for (int i = 0; i <= k; i++) {
                thresholdSparks.add(new Ortho.ThresholdSpark(i));
            }
            JavaRDD<Ortho.ThresholdSpark> thresholdSparkJavaRDD = jsc.parallelize(thresholdSparks);
            start = Instant.now().toEpochMilli();
            Ortho.ThresholdSpark results = thresholdSparkJavaRDD.map(t -> jacobi.calc_dw_and_n(d, t.k, b, g))
                    .reduce((result, next) -> (result.addResult(next)));

            end = Instant.now().toEpochMilli();

            for (Ortho.ThresholdSpark threshold : results.getRes()) {
                bw.write("k = " + threshold.k + "\r\n");
                for (int i = 0; i <= threshold.N; i++) {
                    func = jacobi.val(threshold.k, b, g, threshold.dw * i);
                    bw.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }
        }else{
            start = Instant.now().toEpochMilli();

            for (int i=0;i<=k;i++){
                serialThresholds.add(jacobi.calc_dw_and_n(d, i, b, g));
            }
            end = Instant.now().toEpochMilli();//stopWatch.stop();

            for (int i=0;i<=k;i++) {
                bw.write("k = " + i+ "\r\n");
                for (int j = 0; j <= serialThresholds.get(i).N; j++) {
                    func = jacobi.val(k, b, g, serialThresholds.get(i).dw * j);
                    bw.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }

        }

        if (bw != null)
            bw.close();

        if (fw != null)
            fw.close();
        timings.add(end-start);
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

    public String getSpark() {
        return spark;
    }

    public void setSpark(String spark) {
        this.spark = spark;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public ArrayList<Long> getTimings() {
        return timings;
    }

    public void setTimings(ArrayList<Long> timings) {
        this.timings = timings;
    }
}
