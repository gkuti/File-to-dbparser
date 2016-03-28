package checkpoint.andela.buffer;

public interface Buffer {
    public void set(String line) throws InterruptedException;
    public String get() throws InterruptedException;
}
