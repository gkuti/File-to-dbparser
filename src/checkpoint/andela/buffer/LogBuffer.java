package checkpoint.andela.buffer;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * LogBuffer class implements Buffer interface
 */
public class LogBuffer implements Buffer {
    private ArrayBlockingQueue<String> buffer;

    /**
     * constructor for the LogBuffer class
     */
    public LogBuffer() {
        buffer = new ArrayBlockingQueue<>(1);
    }

    /**
     * saves data to buffer or wait if the buffer is full
     *
     * @param line String to save to buffer
     * @throws InterruptedException
     */
    @Override
    public void set(String line) throws InterruptedException {
        buffer.put(line);

    }

    /**
     * returns the data from buffer
     *
     * @return data from buffer
     * @throws InterruptedException
     */
    @Override
    public String get() throws InterruptedException {
        return buffer.poll();
    }

}
