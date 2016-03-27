import checkpoint.andela.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void testGetDate() throws Exception {
        Date date = new Date();
        String year = date.getDate();
        assertTrue(year.startsWith("2016"));
    }
}