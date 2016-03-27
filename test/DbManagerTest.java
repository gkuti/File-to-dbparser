import checkpoint.andela.db.DbManager;
import org.junit.*;
import static org.junit.Assert.*;

public class DbManagerTest {
    DbManager dbManager;

    @Before
    public void setUp() throws Exception {
        dbManager = new DbManager("jdbc:mysql://localhost:3306/new_schema1", "root", "");
    }

    @Test
    public void testInsert() throws Exception {
        String keys = "name`,`age`,`sex";
        String values = "kuti gbolahan','23','male";
        int result = dbManager.insert("biodata", keys, values);
        assertEquals("expect to return 1", 1, result);
    }
}