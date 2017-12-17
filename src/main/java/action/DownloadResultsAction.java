package action;

import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DownloadResultsAction extends ActionSupport {


    public static final String FILENAME=  "results.txt";

    private InputStream fileInputStream;
    // Used to set file name dynamically
    private String fileName;

    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }

    public String execute() throws Exception
    {
        File fileToDownload = new File(FILENAME);
        fileName = fileToDownload.getName();
        fileInputStream = new FileInputStream(fileToDownload);
        return SUCCESS;
    }

    public String getFileName()
    {
        return FILENAME;
    }

}
