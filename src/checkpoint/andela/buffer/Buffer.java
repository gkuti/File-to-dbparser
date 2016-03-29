package checkpoint.andela.buffer;

/**
 * Buffer interface
 */
public interface Buffer {
    public void set(String line) throws InterruptedException;
    public String get() throws InterruptedException;
}
