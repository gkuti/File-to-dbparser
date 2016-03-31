package checkpoint.andela.db;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.parser.FileParser;
import checkpoint.andela.util.Constants;
import checkpoint.andela.util.Date;
import checkpoint.andela.util.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DBWriter class implements the Runnable interface
 */
public class DBWriter implements Runnable {
    private Buffer sharedDataLocation, sharedLogLocation;
    private DbManager dbManager;
    private String keyStatement = "";
    private String valueStatement = "";
    private static boolean runningState;
    private String currentKey;
    private ArrayList<String> keyList = new ArrayList<>();
    private HashMap<String, String> valueMap = new HashMap<>();
    private Date date;

    /**
     * constructor for the DBWriter class
     *
     * @param dataBuffer the buffer to store data read from the file
     * @param logBuffer  the buffer to log its activity
     */
    public DBWriter(Buffer dataBuffer, Buffer logBuffer) {
        sharedDataLocation = dataBuffer;
        sharedLogLocation = logBuffer;
        try {
            dbManager = new DbManager(Constants.DATABASE_URL.getValue(), Constants.USER.getValue(), Constants.PASSWORD.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method that is invoked when the thread is started
     */
    @Override
    public void run() {
        String keyValue;
        runningState = true;
        while (FileParser.getState()) {
            try {
                keyValue = sharedDataLocation.get();
                keyValueOperation(keyValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopRun();
    }

    /**
     * called by the keyValueOperation method to execute the Sql insert statement
     */
    private void executeStatement() {
        try {
            dbManager.insert(Constants.TABLE.getValue(), keyStatement, valueStatement);
            newOperation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * called by the execute method to reinitialize some data
     */
    public void newOperation() {
        keyStatement = "";
        valueStatement = "";
        keyList = new ArrayList<>();
        valueMap = new HashMap<>();
    }

    /**
     * method to store keys on a ArrayList and values on a HashMap.
     * It checks if the key is in the list then appends it to the value in the HashMap.
     * else it just adds the key to the list and create a new map for the value.
     *
     * @param newkey   the key to store
     * @param newvalue the value of the key
     */
    public void keyValueStore(String newkey, String newvalue) {
        String value;
        if (keyList.contains(newkey)) {
            currentKey = newkey;
            value = valueMap.get(newkey) + "**" + newvalue;
            valueMap.put(newkey, value);
        } else {
            keyList.add(newkey);
            valueMap.put(newkey, newvalue);
            currentKey = newkey;
        }
    }

    /**
     * Builds up the key and value statement
     */
    public void statementBuilder() {
        for (String key : keyList) {
            if (keyStatement.equals("")) {
                newKeyValue(key);
            } else {
                appendKeyValue(key);
            }
        }
    }

    /**
     * set the logbuffer to specified String parameter
     *
     * @param text to be stored in the buffer
     */
    public void setLogBuffer(String text) {
        Logger.setBuffer(sharedLogLocation, "DBWriter", text);
    }

    /**
     * called if the sql statement is empty
     *
     * @param key the key to add to the statement
     */
    public void newKeyValue(String key) {
        String value;
        keyStatement = key;
        value = valueMap.get(key);
        valueStatement = value;
    }

    /**
     * method to append keys and values to the sql statement
     *
     * @param key the key append and fetch value for
     */
    public void appendKeyValue(String key) {
        String value;
        keyStatement = keyStatement + "`, " + "`" + key;
        value = valueMap.get(key);
        valueStatement = valueStatement + "', " + "'" + value;
    }

    /**
     * returns the keyStatement
     *
     * @return String of keyStatement
     */
    public String getKeyStatement() {
        return keyStatement;
    }

    /**
     * returns the valueStatement
     *
     * @return String of valueStatement
     */
    public String getValueStatement() {
        return valueStatement;
    }

    /**
     * process the line read from the buffer and perform the appropriate operation
     *
     * @param keyValue the line read by the buffer
     */
    public void keyValueOperation(String keyValue) {
        if (keyValue.equals("//")) {
            statementBuilder();
            executeStatement();
        }
        if (keyValue.startsWith("/") || keyValue.startsWith("^")) {
            setLogBuffer(keyValue);
            appendValue(keyValue);
        } else {
            setLogBuffer(keyValue);
            String[] seperated = keyValue.split(" - ");
            keyValueStore(seperated[0], seperated[1]);
        }
    }

    /**
     * add values to a existing key
     *
     * @param Value the line read by the buffer
     */
    public void appendValue(String Value) {
        if (Value.startsWith("^")) {
            String value = valueMap.get(currentKey) + Value;
            valueMap.put(currentKey, value);
        }
        if (Value.startsWith("/")) {
            String value = valueMap.get(currentKey) + Value.replaceFirst("/", "");
            valueMap.put(currentKey, value);
        }
    }

    /**
     * returns the list keys
     *
     * @return ArrayList of keys
     */
    public ArrayList getKeyList() {
        return keyList;
    }

    /**
     * returns the map of values
     *
     * @return HashMap of the keys and values
     */
    public HashMap getValueMap() {
        return valueMap;
    }

    /**
     * returns the state of the thread
     *
     * @return true if the thread is running else false
     */
    public static boolean getState() {
        return runningState;

    }

    /**
     * to set the state of the thread
     *
     * @param value the boolean value to set for the thread
     */
    public void setState(boolean value) {
        runningState = value;
    }

    /**
     * closes the database connection
     */
    public void stopRun() {
        runningState = false;
        try {
            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
