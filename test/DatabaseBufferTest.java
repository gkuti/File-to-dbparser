import checkpoint.andela.buffer.DatabaseBuffer;
import org.junit.*;
import static org.junit.Assert.*;

public class DatabaseBufferTest {
    DatabaseBuffer databaseBuffer;

    @Before
    public void setUp() throws Exception {
        databaseBuffer = new DatabaseBuffer();
    }

    @Test
    public void testSet() throws Exception {
        databaseBuffer.set("database");
        assertEquals("expect to return database", "database", databaseBuffer.get());
    }

    @Test
    public void testGet() throws Exception {
        databaseBuffer.set("database buffer");
        String result = databaseBuffer.get();
        assertEquals("expect to return database buffer", "database buffer", result);
    }
}