package action;

import jacobi.*;

import java.io.*;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class JacobiAction extends ActionSupport {


    private String k;
    private String b;
    private String g;
    private String d;
    private String spark;
    private Exception exception;
    private ArrayList<Long> timings = new ArrayList<>();
    private ArrayList<Threshold> serialThresholds = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String execute() throws IOException {
        int k = Integer.valueOf(getK());
        int b = Integer.valueOf(getB());
        double g = Double.valueOf(getG());
        double d = Double.valueOf(getD());
        File file;
        long start, end;
        boolean spark = Boolean.parseBoolean(getSpark());
        Complex func;
        Ortho jacobi = new Jacobi3();
        file = new File(DownloadResultsAction.FILENAME);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getName());
        BufferedWriter bw = new BufferedWriter(fw);
        if (spark) {
            SparkConf conf = new SparkConf().setAppName("Jacobi").setMaster("local[*]");
            JavaSparkContext jsc = new JavaSparkContext(SparkContext.getOrCreate(conf));
            ArrayList<Threshold> thresholdSparks = new ArrayList<>();
            for (int i = 0; i <= k; i++) {
                Threshold threshold = new Threshold();
                threshold.setK(k);
                thresholdSparks.add(threshold);
            }
            JavaRDD<Threshold> thresholdSparkJavaRDD = jsc.parallelize(thresholdSparks);
            start = Instant.now().toEpochMilli();
            List<Threshold> result = thresholdSparkJavaRDD.map(threshold -> jacobi.getDwN(d, threshold.getK(), b, g)).collect();
            //.reduce((result, next) -> (result.addResult(next)));

            end = Instant.now().toEpochMilli();

            for (Threshold threshold : result) {
                bw.write("k = " + threshold.getK() + "\r\n");
                for (int i = 0; i <= threshold.getN(); i++) {
                    func = jacobi.val(threshold.getK(), b, g, threshold.getDw() * i);
                    bw.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }
        } else {
            start = Instant.now().toEpochMilli();

            for (int i = 0; i <= k; i++) {
                serialThresholds.add(jacobi.getDwN(d, i, b, g));
            }
            end = Instant.now().toEpochMilli();//stopWatch.stop();

            for (int i = 0; i <= k; i++) {
                bw.write("k = " + i + "\r\n");
                for (int j = 0; j <= serialThresholds.get(i).getN(); j++) {
                    func = jacobi.val(k, b, g, serialThresholds.get(i).getDw() * j);
                    bw.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }

        }

        bw.close();
        fw.close();

        timings.add(end - start);
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
