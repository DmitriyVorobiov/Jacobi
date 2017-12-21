package action;

import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DownloadResultsAction extends ActionSupport {


    public static final String FILENAME_SERIAL=  "results_ser.txt";
    public static final String FILENAME_PARALLEL=  "results_par.txt";

    private InputStream fileInputStream;
    // Used to set file name dynamically
    private String fileName;

    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }

    public String execute() throws Exception
    {
        File fileToDownload = new File(fileName);
        fileName = fileToDownload.getName();
        fileInputStream = new FileInputStream(fileToDownload);
        return SUCCESS;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static String getFilenameSerial() {
        return FILENAME_SERIAL;
    }

    public static String getFilenameParallel() {
        return FILENAME_PARALLEL;
    }
}
