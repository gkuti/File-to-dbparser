package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.util.Date;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * FileParser class implements the Runnable interface
 */
public class FileParser implements Runnable {
    private String fileName;
    private Buffer sharedDataLocation, sharedLogLocation;
    private static boolean runningState;
    private BufferedReader bufferedReader;
    private FileReader reader;
    private Date date;

    /**
     * constructor for the FileParser class
     *
     * @param dataBuffer the buffer to store data read from the file
     * @param logBuffer  the buffer to log its activity
     * @param fileName   the file to read data from
     */
    public FileParser(Buffer dataBuffer, Buffer logBuffer, String fileName) {
        this.fileName = fileName;
        sharedDataLocation = dataBuffer;
        sharedLogLocation = logBuffer;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(reader);
    }

    /**
     * The run method that is invoked when the thread is started
     */
    @Override
    public void run() {
        runningState = true;
        try {
            String line = bufferedReader.readLine().trim();
            while (line != null) {
                if (isNeeded(line)) {
                    sharedDataLocation.set(escapeQuote(line));
                    if (!line.startsWith("//")) {
                        setLogBuffer(line);
                    }
                    System.out.println(line);
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopRun();
    }

    /**
     * this is called by the run method to check if the line read should be omitted
     *
     * @param line the String to check if it is a valid data
     * @return true if the line starts with anything but # character
     */
    public boolean isNeeded(String line) {
        if (line.startsWith("#")) {
            return false;
        }
        return true;
    }

    /**
     * to check the state of the thread
     *
     * @return true if the thread is still running else false
     */
    public static boolean getState() {
        return runningState;

    }

    /**
     * to set the state of the thread
     *
     * @param value the boolean value to set for the thread
     */
    public void setState(boolean value) {
        runningState = value;
    }

    /**
     * to escape a string of text with qoute
     *
     * @param text the String of text to perform the operation on
     * @return a String with escaped qoute
     */
    public String escapeQuote(String text) {
        return text.replaceAll("'", "''");
    }

    /**
     * to finish the state of the thread
     */
    public void stopRun() {
        try {
            runningState = false;
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set the logbuffer to specified String parameter
     *
     * @param text to be stored in the buffer
     */
    public void setLogBuffer(String text) {
        try {
            date = new Date();
            sharedLogLocation.set("FileParser Thread " + "\t" + "(" + date.getDate() + ")" + "\t" + "--- wrote " + text + " to buffer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
