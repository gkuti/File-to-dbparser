package checkpoint.andela;

import java.util.concurrent.ArrayBlockingQueue;

public class DatabaseBuffer implements Buffer {
    private ArrayBlockingQueue<String> buffer;

    public DatabaseBuffer() {
        buffer = new ArrayBlockingQueue<>(1);
    }

    @Override
    public void set(String line) throws InterruptedException {
        buffer.put(line);
    }

    @Override
    public String get() throws InterruptedException {
        return buffer.take();
    }

}
