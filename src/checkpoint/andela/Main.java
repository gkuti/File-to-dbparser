package checkpoint.andela;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.DatabaseBuffer;
import checkpoint.andela.buffer.LogBuffer;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;
import checkpoint.andela.util.Constants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Buffer dataBuffer = new DatabaseBuffer();
        Buffer logBuffer = new LogBuffer();
        FileParser fileParser = new FileParser(dataBuffer, logBuffer, Constants.DATA_FILENAME.getValue());
        DBWriter dbWriter = new DBWriter(dataBuffer, logBuffer);
        LogWriter logWriter = new LogWriter(logBuffer, Constants.LOG_FILENAME.getValue());
        ExecutorService e = Executors.newCachedThreadPool();
        e.execute(fileParser);
        e.execute(dbWriter);
        e.execute(logWriter);
        e.shutdown();

    }
}
