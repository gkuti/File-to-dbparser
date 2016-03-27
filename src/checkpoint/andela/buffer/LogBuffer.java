package checkpoint.andela.buffer;

import java.util.concurrent.ArrayBlockingQueue;

public class LogBuffer implements Buffer {
    private ArrayBlockingQueue<String> buffer;

    public LogBuffer() {
        buffer = new ArrayBlockingQueue<>(1);
    }

    @Override
    public void set(String line) throws InterruptedException {
        buffer.put(line);

    }

    @Override
    public String get() throws InterruptedException {
        return buffer.poll();
    }

}
