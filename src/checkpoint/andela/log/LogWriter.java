package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.db.DBWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * LogWriter class implements the Runnable interface
 */
public class LogWriter implements Runnable {
    public File file;
    private Buffer sharedLogLocation;
    private BufferedWriter bufferedWriter;
    private FileWriter fileWriter;

    /**
     * constructor for the LogWriter class
     *
     * @param buffer   the log buffer to read from
     * @param fileName the file to store the data
     */
    public LogWriter(Buffer buffer, String fileName) {
        sharedLogLocation = buffer;
        file = new File(fileName);
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * The run method that is invoked when the thread is started
     */
    @Override
    public void run() {
        while (DBWriter.getState()) {
            try {
                String log = sharedLogLocation.get();
                if (log != null) {
                    writeLog(log);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopRun();
    }

    /**
     * closes the writer stream sets the thread runningState to false
     */
    private void stopRun() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes the specified text to a new line in the file
     *
     * @param text the data to write to file
     */
    public void writeLog(String text) {
        try {
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
