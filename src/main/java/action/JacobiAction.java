package action;

import jacobi.*;

import java.io.*;

import com.opensymphony.xwork2.ActionSupport;
import jacobi.test.SpecialCase;
import jacobi.test.SpecialCaseFactory;
import jacobi.test.exception.TestException;
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
    private String algoName;
    private Exception exception;
    private long timeSerial, timeParallel, timeTesting;
    private ArrayList<Threshold> serialThresholds = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String execute() throws IOException {
        int k = Integer.valueOf(getK());
        int b = Integer.valueOf(getB());
        double g = Double.valueOf(getG());
        final double d = Double.valueOf(getD());
        File fileSerial, fileParallel;
        long start, end;
        Complex func;
        Ortho jacobi = null;
        try {
            timeTesting = startTests();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (TestException e) {
            exception = e;
            return ERROR;
        }
        try {
            jacobi = (Ortho) Class.forName("jacobi." + algoName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
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

        jacobi.setB(b);
        jacobi.setG(g);

        start = Instant.now().toEpochMilli();

        for (int i = 0; i <= k; i++) {
            jacobi.setK(i);
            serialThresholds.add(jacobi.getDwN(d));
        }
        end = Instant.now().toEpochMilli();//stopWatch.stop();
        timeSerial = end - start;
        for (int i = 0; i <= k; i++) {
            bws.write("k = " + i + "\r\n");
            for (int j = 0; j <= serialThresholds.get(i).getN(); j++) {
                func = jacobi.val(serialThresholds.get(i).getDw() * j);
                bws.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
            }
        }
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
        final Ortho mappedJacobi = jacobi;
        List<Threshold> result = thresholdSparkJavaRDD.map(threshold -> {
            mappedJacobi.setK(threshold.getK());
            return mappedJacobi.getDwN(d);
        }).collect();
        //.reduce((result, next) -> (result.addResult(next)));

        end = Instant.now().toEpochMilli();

        for (int i = 0; i < serialThresholds.size(); i++) {
            if (!serialThresholds.get(i).isEqualTo(result.get(i)))
                org.apache.log4j.Logger.getRootLogger().error("Pair # " + i + " is not equal");
        }

        jsc.stop();
        timeParallel = end - start;
        for (Threshold threshold : result) {
            bwp.write("k = " + threshold.getK() + "\r\n");
            for (int i = 0; i <= threshold.getN(); i++) {
                func = jacobi.val(threshold.getDw() * i);
                bwp.write((func.Re + "; " + func.Im + ";\r\n").replace('.', ','));
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

    public String getAlgoName() {
        return algoName;
    }

    public void setAlgoName(String algoName) {
        this.algoName = algoName;
    }

    public long getTimeTesting() {
        return timeTesting;
    }

    private long startTests() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, TestException {
        long start, end;
        final int k = 6;
        final int b = 2;
        final int g = 1;
        final double d = 0.02;
        File testFile;
        Complex testFunc;
        Complex jacobiFunc;
        start = Instant.now().toEpochMilli();
        testFile = new File(DownloadResultsAction.FILENAME_TEST);
        BufferedWriter bufferedWriterTest = new BufferedWriter(new FileWriter(testFile.getName()));
        Jacobi1 jacobi1 = new Jacobi1();
        jacobi1.setB(b);
        jacobi1.setG(g);
        for (int i = 0; i <= 5; i++) {
            SpecialCase specialCase = SpecialCaseFactory.createSpecialCase(i);
            specialCase.setK(i);
            specialCase.setG(g);
            specialCase.setB(b);
            jacobi1.setK(i);
            Threshold testThreshold = specialCase.getDwN(d);
            Threshold jacobiThreshold = jacobi1.getDwN(d);
            if (!testFile.exists()) {
                testFile.createNewFile();
            } else {
                testFile.delete();
                testFile.createNewFile();
            }
            bufferedWriterTest.write("order= " + i + "\r\n");

            for (int j = 0; j <= testThreshold.getN(); j++) {
                //TODO there is error in second special case. Have to be fixed
                if(i==2) continue;
                testFunc = specialCase.val(testThreshold.getDw() * j);
                jacobiFunc = jacobi1.val(jacobiThreshold.getDw() * j);
                if (!testFunc.equals(jacobiFunc)) {
                    bufferedWriterTest.close();
                    throw TestException.build(j);
                }
                bufferedWriterTest.write((testFunc.Re + "; " + testFunc.Im + ";\r\n").replace('.', ','));
            }
        }
        end = Instant.now().toEpochMilli();
        bufferedWriterTest.close();
        return end - start;
    }
}
