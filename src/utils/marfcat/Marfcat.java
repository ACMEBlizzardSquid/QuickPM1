package utils.marfcat;
import java.io.InputStream;
import java.io.IOException;
import java.lang.InterruptedException;
import utils.io.StreamMonitor;

/**
 * MARFCAT
 * A facade around the MARFCAT library
 * 
 * @author c_bode
 */
public class Marfcat {
    
    String rootPath = getClass().getClassLoader().getResource(".").getPath();
    String marfcatPath = rootPath + "marfcat/marfcat.jar";
    String marfcatExec = "java -jar " + marfcatPath;
    
    /**
     * Trains MARFCAT on a supplied MARFCAT_IN file
     * @param inputFilePath The path to the MARFCAT_IN file
     * @throws IOException
     */
    public void train (String inputFilePath) 
            throws IOException, InterruptedException {
        
        // execute job
        String options = " --train -nopreprep -raw -fft -eucl ";
        Process process = Runtime.getRuntime().exec(marfcatExec + options + inputFilePath);
        
        // redirect Marfcat output streams
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();
        StreamMonitor inMonitor = new StreamMonitor(in);
        StreamMonitor errMonitor = new StreamMonitor(err, true);
        inMonitor.run();
        errMonitor.run();
        
        // wait for process to finish
        process.waitFor();
    }
    
    public void analyze (String inputFilePath)
            throws IOException, InterruptedException {
        
        // execute job
        String options = " --batch-ident test-quick-marf-cve ";
        String options2 = " -nopreprep -raw -fft -cheb";
        Process process = Runtime.getRuntime().exec(marfcatExec + options + inputFilePath + options2);
        
        // redirect Marfcat output streams
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();
        StreamMonitor inMonitor = new StreamMonitor(in);
        StreamMonitor errMonitor = new StreamMonitor(err, true);
        inMonitor.run();
        errMonitor.run();
        
        // wait for process to finish
        process.waitFor();
        
    }
    
}
