package checkpoint.andela;

import andela.util.Date;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileParser implements Runnable {
    private String fileName;
    private Buffer sharedDataLocation, sharedLogLocation;
    private static boolean runningState;
    private BufferedReader bufferedReader;
    private FileReader reader;
    private Date date;

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

    public boolean isNeeded(String line) {
        if (line.startsWith("#")) {
            return false;
        }
        return true;
    }

    public static boolean getState() {
        return runningState;

    }

    public void setState(boolean value) {
        runningState = value;
    }

    public String escapeQuote(String text) {
        return text.replaceAll("'", "''");
    }

    public void stopRun() {
        try {
            runningState = false;
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogBuffer(String text) {
        try {
            date = new Date();
            sharedLogLocation.set("FileParser Thread " + "\t" + "(" + date.getDate() + ")" + "\t" + "--- wrote " + text + " to buffer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
