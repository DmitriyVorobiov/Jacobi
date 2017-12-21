package action;

import jacobi.*;

import java.io.*;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
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
    private String algorithm;
    private Exception exception;
    private long timeSerial, timeParallel, timeTesting;
    private ArrayList<Threshold> serialThresholds = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String execute() throws IOException {
        int k = Integer.valueOf(getK());
        int b = Integer.valueOf(getB());
        double g = Double.valueOf(getG());
        double d = Double.valueOf(getD());
        File fileSerial, fileParallel;
        long start, end;
        Complex func;
        Ortho jacobi;
        switch (algorithm) {
            case "Jacobi1":
                jacobi = new Jacobi1();
                break;
            case "Jacobi2":
                jacobi = new Jacobi2();
                break;
            case "Jacobi3":
            default:
                jacobi = new Jacobi3();
                break;
        }
        fileSerial = new File(DownloadResultsAction.FILENAME_SERIAL);
        fileParallel = new File(DownloadResultsAction.FILENAME_PARALLEL);
        if (!fileSerial.exists()) {
            fileSerial.createNewFile();
        } else {
            fileSerial.delete();
            fileSerial.createNewFile();
        }
        if (!fileParallel.exists()) {
            fileParallel.createNewFile();
        } else {
            fileParallel.delete();
            fileParallel.createNewFile();
        }
        BufferedWriter bws = new BufferedWriter(new FileWriter(fileSerial.getName()));
        BufferedWriter bwp = new BufferedWriter(new FileWriter(fileParallel.getName()));
        //serial code
        {
            start = Instant.now().toEpochMilli();

            for (int i = 0; i <= k; i++) {
                serialThresholds.add(jacobi.getDwN(d, i, b, g));
            }
            end = Instant.now().toEpochMilli();//stopWatch.stop();
            timeSerial = end-start;
            for (int i = 0; i <= k; i++) {
                bws.write("k = " + i + "\r\n");
                for (int j = 0; j <= serialThresholds.get(i).getN(); j++) {
                    func = jacobi.val(serialThresholds.get(i).getK(), b, g, serialThresholds.get(i).getDw() * j);
                    bws.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }
        }

        //parallel code
        {
            SparkConf conf = new SparkConf().setAppName("Jacobi").setMaster("local[*]");
            JavaSparkContext jsc = new JavaSparkContext(SparkContext.getOrCreate(conf));
            ArrayList<Threshold> thresholdSparks = new ArrayList<>();
            for (int i = 0; i <= k; i++) {
                Threshold threshold = new Threshold();
                threshold.setK(i);
                thresholdSparks.add(threshold);
            }
            JavaRDD<Threshold> thresholdSparkJavaRDD = jsc.parallelize(thresholdSparks);
            start = Instant.now().toEpochMilli();
            List<Threshold> result = thresholdSparkJavaRDD.map(threshold -> jacobi.getDwN(d, threshold.getK(), b, g)).collect();
            //.reduce((result, next) -> (result.addResult(next)));

            end = Instant.now().toEpochMilli();

            for (int i = 0; i < serialThresholds.size(); i++) {
                if (!serialThresholds.get(i).isEqualTo(result.get(i)))
                    org.apache.log4j.Logger.getRootLogger().error("Pair # "+i+" is not equal");
            }

            jsc.stop();
            timeParallel = end - start;
            for (Threshold threshold : result) {
                bwp.write("k = " + threshold.getK() + "\r\n");
                for (int i = 0; i <= threshold.getN(); i++) {
                    func = jacobi.val(threshold.getK(), b, g, threshold.getDw() * i);
                    bwp.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
                }
            }
        }

        bws.close();
        bwp.close();

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

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public long getTimeSerial() {
        return timeSerial;
    }

    public long getTimeParallel() {
        return timeParallel;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public long getTimeTesting() {
        return timeTesting;
    }
}
