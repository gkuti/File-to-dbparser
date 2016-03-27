import checkpoint.andela.buffer.LogBuffer;
import org.junit.*;
import static org.junit.Assert.*;

public class LogBufferTest {
    LogBuffer logBuffer;

    @Before
    public void setUp() throws Exception {
        logBuffer = new LogBuffer();
    }

    @Test
    public void testSet() throws Exception {
        logBuffer.set("database");
        assertEquals("expect to return database", "database", logBuffer.get());
    }

    @Test
    public void testGet() throws Exception {
        logBuffer.set("database buffer");
        String result = logBuffer.get();
        assertEquals("expect to return database buffer", "database buffer", result);
    }
}