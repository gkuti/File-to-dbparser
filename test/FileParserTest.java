import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.DatabaseBuffer;
import checkpoint.andela.buffer.LogBuffer;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.parser.FileParser;
import org.junit.*;
import static org.junit.Assert.*;

public class FileParserTest {
    FileParser fileParser;
    DBWriter dbWriter;
    Buffer logBuffer, dataBuffer;

    @Before
    public void setUp() throws Exception {
        dataBuffer = new DatabaseBuffer();
        logBuffer = new LogBuffer();
        dbWriter = new DBWriter(dataBuffer, logBuffer);
        fileParser = new FileParser(dataBuffer, logBuffer, "files/test.txt");
    }

    @Test
    public void testRun() throws Exception {
        fileParser.run();
        String result = dataBuffer.get();
        assertEquals("UNIQUE-ID - RXN-11836", result);
    }

    @Test
    public void testIsNeeded() throws Exception {
        String line = "#";
        boolean result = fileParser.isNeeded(line);
        assertFalse("expect to return false", result);
        line = "LEFT";
        result = fileParser.isNeeded(line);
        assertTrue("expect to return true", result);
    }

    @Test
    public void testGetState() throws Exception {
        fileParser.setState(true);
        boolean result = fileParser.getState();
        assertEquals("expect to return true", true, result);
    }

    @Test
    public void testSetState() throws Exception {
        fileParser.setState(false);
        boolean result = fileParser.getState();
        assertEquals("expect to return true", false, result);
    }

    @Test
    public void testEscapeQuote() throws Exception {
        String line = "'let us go' so he said";
        String result = fileParser.escapeQuote(line);
        assertEquals(fileParser.escapeQuote(line), result);
    }

    @Test
    public void testStopRun() throws Exception {
        fileParser.stopRun();
        assertEquals("expect to return false", false, fileParser.getState());
    }

    @Test
    public void testSetLogBuffer() throws Exception {
        fileParser.setLogBuffer("REACTIONS");
        String text = logBuffer.get();
        assertNotNull(text);
        assertTrue(text.contains("REACTIONS"));
    }
}