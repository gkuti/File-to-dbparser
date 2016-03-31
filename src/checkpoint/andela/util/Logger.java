package checkpoint.andela.util;

import checkpoint.andela.buffer.Buffer;

/**
 * Logger class
 */
public class Logger {
    static Date date;

    public static void setBuffer(Buffer sharedLogLocation, String threadName, String text) {
        try {
            date = new Date();
            if (threadName.equals("FileParser")) {
                sharedLogLocation.set(threadName + "Thread " + "\t" + "(" + date.getDate() + ")" + "\t" + "--- wrote " + text + " to buffer");
            }
            if (threadName.equals("DBWriter")) {
                sharedLogLocation.set(threadName + "Thread " + "\t" + "(" + date.getDate() + ")" + "\t" + "--- collected " + text + " from buffer");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

