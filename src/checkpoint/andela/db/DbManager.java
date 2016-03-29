package checkpoint.andela.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBManager class
 */
public class DbManager {
    private Connection connection;
    private Statement statement;

    /**
     * constructor for the DBManager class
     *
     * @param url      the database url
     * @param user     the username for the database connection
     * @param password the password for the database connection
     * @throws SQLException
     */
    public DbManager(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * this method insert data into the specified table with the keys and valuse
     *
     * @param table  the database table to insert into
     * @param keys   the String of columns to insert into
     * @param values the values to insert into those columns
     * @return 1 if the operation is successful
     * @throws SQLException
     */
    public int insert(String table, String keys, String values) throws SQLException {
        int result;
        statement = connection.createStatement();
        result = statement.executeUpdate("insert into " + table + " (`" + keys + "`) values ('" + values + "')");
        return result;
    }

    /**
     * closes the database connection
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        connection.close();
    }
}
