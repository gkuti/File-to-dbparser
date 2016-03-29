import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.DatabaseBuffer;
import checkpoint.andela.buffer.LogBuffer;
import checkpoint.andela.db.DBWriter;
import org.junit.*;
import java.util.HashMap;
import static org.junit.Assert.*;

public class DBWriterTest {
    DBWriter dbWriter;
    Buffer logBuffer, dataBuffer;

    @Before
    public void setUp() throws Exception {
        dataBuffer = new DatabaseBuffer();
        logBuffer = new LogBuffer();
        dbWriter = new DBWriter(dataBuffer, logBuffer);
    }
    @Test
    public void testRun() throws Exception {
        dbWriter.run();
        assertFalse(DBWriter.getState());
    }
    @Test
    public void testKeyValueStore() throws Exception {
        dbWriter.keyValueStore("LEFT", "12er4");
        assertTrue(dbWriter.getKeyList().contains("LEFT"));
        HashMap<String, String> valueMap = dbWriter.getValueMap();
        String value = valueMap.get("LEFT");
        assertEquals("expect to return 12er4", "12er4", value);
        dbWriter.keyValueStore("LEFT", "67fgt");
        valueMap = dbWriter.getValueMap();
        value = valueMap.get("LEFT");
        assertEquals("12er4**67fgt", value);
    }

    @Test
    public void testStatementBuilder() throws Exception {
        dbWriter.keyValueStore("LEFT", "12er4");
        dbWriter.keyValueStore("RIGHT", "XY-0");
        dbWriter.statementBuilder();
        String keyStatement = dbWriter.getKeyStatement();
        String valueStatement = dbWriter.getValueStatement();
        assertEquals("LEFT`, `RIGHT", keyStatement);
        assertEquals("12er4', 'XY-0", valueStatement);
    }

    @Test
    public void testSetLogBuffer() throws Exception {
        dbWriter.setLogBuffer("REACTIONS");
        String text = logBuffer.get();
        assertNotNull(text);
        assertTrue(text.contains("REACTIONS"));
    }

    @Test
    public void testNewKeyValue() throws Exception {
        dbWriter.newKeyValue("CITATIONS");
        String key = dbWriter.getKeyStatement();
        assertEquals("CITATIONS", key);
    }

    @Test
    public void testAppendKeyValue() throws Exception {
        dbWriter.newKeyValue("CITATIONS");
        dbWriter.appendKeyValue("COMMENT");
        String keyStatement = dbWriter.getKeyStatement();
        assertEquals("CITATIONS`, `COMMENT", keyStatement);
    }

    @Test
    public void testGetKeyStatement() throws Exception {
        dbWriter.keyValueStore("LEFT", "12er4");
        dbWriter.keyValueStore("RIGHT", "XY-0");
        dbWriter.statementBuilder();
        String result = dbWriter.getKeyStatement();
        assertEquals("LEFT`, `RIGHT", result);
    }

    @Test
    public void testGetValueStatement() throws Exception {
        dbWriter.keyValueStore("LEFT", "12er4");
        dbWriter.keyValueStore("RIGHT", "XY-0");
        dbWriter.statementBuilder();
        String result = dbWriter.getValueStatement();
        assertEquals("12er4', 'XY-0", result);
    }

    @Test
    public void testGetState() throws Exception {
        dbWriter.setState(true);
        boolean result = dbWriter.getState();
        assertEquals("expect to return true", true, result);
    }

    @Test
    public void testSetState() throws Exception {
        dbWriter.setState(false);
        boolean result = dbWriter.getState();
        assertEquals("expect to return true", false, result);
    }

    @Test
    public void testNewOPeration() throws Exception {
        dbWriter.keyValueStore("LEFT", "12er4");
        dbWriter.keyValueStore("RIGHT", "XY-0");
        dbWriter.newOperation();
        String keyStatement = dbWriter.getKeyStatement();
        assertEquals("", keyStatement);
        String valueStatement = dbWriter.getValueStatement();
        assertEquals("", valueStatement);
    }

    @Test
    public void testKeyValueOPeration() throws Exception {
        dbWriter.keyValueOperation("UNIQUE-ID - RXN-11836");
        assertTrue(dbWriter.getKeyList().contains("UNIQUE-ID"));
        HashMap<String, String> valueMap = dbWriter.getValueMap();
        String value = valueMap.get("UNIQUE-ID");
        assertEquals("expect to return RXN-11836", "RXN-11836", value);
    }

    @Test
    public void testAppendValue() throws Exception {
        dbWriter.keyValueStore("RIGHT", "XY-0");
        dbWriter.appendValue("^087");
        HashMap<String, String> valueMap = dbWriter.getValueMap();
        String value = valueMap.get("RIGHT");
        assertEquals("XY-0^087", value);
        dbWriter.keyValueStore("COMMENT", "Good ");
        dbWriter.appendValue("/reaction");
        valueMap = dbWriter.getValueMap();
        value = valueMap.get("COMMENT");
        assertEquals("Good reaction", value);
    }
    @Test
    public void testStopRun() throws Exception {
        dbWriter.stopRun();
        assertEquals("expect to return false", false, dbWriter.getState());
    }
}