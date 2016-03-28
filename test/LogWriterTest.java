import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.LogBuffer;
import checkpoint.andela.log.LogWriter;
import org.junit.*;
import static org.junit.Assert.*;

public class LogWriterTest {
    LogWriter logWriter;
    Buffer logBuffer;

    @Before
    public void setUp() throws Exception {
        logBuffer = new LogBuffer();
        logWriter = new LogWriter(logBuffer, "files/logtest.txt");
        logWriter.run();
    }

    @Test
    public void testWriteLog() throws Exception {
        logWriter = new LogWriter(logBuffer, "files/logtest.txt");
        logWriter.writeLog("Hello");
        assertTrue(logWriter.file.exists());
    }
}