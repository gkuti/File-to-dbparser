import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.DatabaseBuffer;
import org.junit.*;
import static org.junit.Assert.*;

public class BufferTest {
    Buffer buffer;

    @Before
    public void setUp() throws Exception {
        buffer = new DatabaseBuffer();
    }

    @Test
    public void testSet() throws Exception {
        buffer.set("Hello");
        assertEquals("expect to return Hello", "Hello", buffer.get());
    }

    @Test
    public void testGet() throws Exception {
        buffer.set("Hello java");
        String result = buffer.get();
        assertEquals("expect to return Hello java", "Hello java", result);
    }

    @After
    public void tearDown() throws Exception {
        buffer = null;
    }
}