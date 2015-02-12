package utils.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * ProcessMonitor
 * Used to monitor an IO stream from a running process.
 * 
 * @author c_bode
 */
public class StreamMonitor implements Runnable {
    
    final BufferedReader in;
    final PrintStream out;
    
    /**
     * Initializes a new monitor for the input stream
     * @param stream The input stream
     * @param err If true, direct the input to System.err; else to System.out
     */
    public StreamMonitor (InputStream stream, boolean err) {
        in = new BufferedReader(
                new InputStreamReader(stream));
        
        if (err) {
            out = System.err;
        } else {
            out = System.out;
        }
    }
    
    /**
     * Default constructor for the stream monitor.  Directs input to System.out
     * @param stream The input stream
     */
    public StreamMonitor (InputStream stream) {
        this(stream, false);
    }
    
    /**
     * Runs the stream monitor
     */
    public void run () {
        try {
            String line = null;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}