**Checkpoint-two**
==============

A Simple File to DB Parser
-------------
-------------
**About**
========
This is a simple multithreaded Application, which reads in data from a text document, parses the document and saves the data into a mysql database while generating an action log at the same time. 
The program consist of three threads.

 - Fileparser.
 - DBWriter.
 - LogWriter.


----------
**UML CLASS DIAGRAM**

![Uml class Diagram](https://github.com/andela-gkuti/Checkpoint-one/blob/master/uml.png?raw=true)


----------


**Reactions.dat File summary**
=====
This is a key-value or attribute-value file, data is read by lines. 
 # indicates a comment which will be omitted by the FileParser.
 
```java
public boolean isNeeded(String line) {
        if (line.startsWith("#")) {
            return false;
        }
        return true;
    }
```

Apart from the needed key-value lines, we have lines that begin with other characters which was taken care of by the Dbwriter.
  Lines that begin with a "//" indicates the end of an individual record and the begining of another.
  Lines that begin with a "/" or "^" indicates a continued value of the current or last key read, so this value are appended together with "**" character.
```java
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
```

```java
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
```

Working Procedure
-----------------
The FileParser reads the data from the reactions.dat file, store it in a databuffer and also stores its actions in a logbuffer, the DBWriter reads those data from the databuffer, store it in a MySql database and also stores its actions in the lobuffer.
The LogWriter picks those logs in the logbuffer and writes to a reactions.txt file.

Main Class
----------

```java
public class Main {

    public static void main(String[] args) {
        Buffer dataBuffer = new DatabaseBuffer();
        Buffer logBuffer = new LogBuffer();
        FileParser fileParser = new FileParser(dataBuffer, logBuffer, Constants.DATA_FILENAME);
        DBWriter dbWriter = new DBWriter(dataBuffer, logBuffer);
        LogWriter logWriter = new LogWriter(logBuffer, Constants.LOG_FILENAME);
        ExecutorService e = Executors.newCachedThreadPool();
        e.execute(fileParser);
        e.execute(dbWriter);
        e.execute(logWriter);
        e.shutdown();

    }
}
```