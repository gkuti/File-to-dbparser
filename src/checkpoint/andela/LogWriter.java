package checkpoint.andela;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter implements Runnable {
    public File file;
    private Buffer sharedLogLocation;
    private BufferedWriter bufferedWriter;
    private FileWriter fileWriter;

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
        closeWriter();
    }

    private void closeWriter() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
