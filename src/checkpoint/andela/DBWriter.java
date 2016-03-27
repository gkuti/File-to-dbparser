package checkpoint.andela;

import andela.util.Constants;
import andela.util.Date;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBWriter implements Runnable {
    private Buffer sharedDataLocation, sharedLogLocation;
    private DbManager dbm;
    private String keyStatement = "";
    private String valueStatement = "";
    private static boolean runningState;
    private String currentKey;
    private ArrayList<String> keyList = new ArrayList<>();
    private HashMap<String, String> valueMap = new HashMap<>();
    private Date date;

    public DBWriter(Buffer dataBuffer, Buffer logBuffer) {
        sharedDataLocation = dataBuffer;
        sharedLogLocation = logBuffer;
        try {
            dbm = new DbManager(Constants.DATABASE_URL, Constants.USER, Constants.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String keyValue = "";
        runningState = true;
        while (FileParser.getState()) {
            try {
                keyValue = sharedDataLocation.get();
                keyValueOperation(keyValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runningState = false;
    }

    private void executeStatement() {
        try {
            dbm.insert(Constants.TABLE, keyStatement, valueStatement);
            newOperation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void newOperation() {
        keyStatement = "";
        valueStatement = "";
        keyList = new ArrayList<>();
        valueMap = new HashMap<>();
    }

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

    public void statementBuilder() {
        for (String key : keyList) {
            if (keyStatement.equals("")) {
                newKeyValue(key);
            } else {
                appendKeyValue(key);
            }
        }
    }

    public void setLogBuffer(String text) {
        try {
            date = new Date();
            sharedLogLocation.set("DBWriter Thread " + "\t" + "(" + date.getDate() + ")" + "\t" + "--- collected " + text + " from buffer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newKeyValue(String key) {
        String value;
        keyStatement = key;
        value = valueMap.get(key);
        valueStatement = value;
    }

    public void appendKeyValue(String key) {
        String value;
        keyStatement = keyStatement + "`, " + "`" + key;
        value = valueMap.get(key);
        valueStatement = valueStatement + "', " + "'" + value;
    }

    public String getKeyStatement() {
        return keyStatement;
    }

    public String getValueStatement() {
        return valueStatement;
    }

    public void keyValueOperation(String keyValue) {
        if (keyValue.equals("//")) {
            statementBuilder();
            executeStatement();
        } else {
            if (keyValue.startsWith("/") || keyValue.startsWith("^")) {
                setLogBuffer(keyValue);
                appendValue(keyValue);
            } else {
                setLogBuffer(keyValue);
                String[] seperated = keyValue.split(" - ");
                keyValueStore(seperated[0], seperated[1]);
            }
        }
    }

    public void appendValue(String keyValue) {
        if (keyValue.startsWith("^")) {
            String value = valueMap.get(currentKey) + keyValue;
            valueMap.put(currentKey, value);
        }
        if (keyValue.startsWith("/")) {
            String value = valueMap.get(currentKey) + keyValue.replaceFirst("/", "");
            valueMap.put(currentKey, value);
        }
    }

    public ArrayList getKeyList() {
        return keyList;
    }

    public HashMap getValueMap() {
        return valueMap;
    }

    public static boolean getState() {
        return runningState;

    }

    public void setState(boolean value) {
        runningState = value;
    }
}
