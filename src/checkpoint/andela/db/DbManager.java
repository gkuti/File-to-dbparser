package checkpoint.andela.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    private Connection connection;
    private Statement statement;

    public DbManager(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public int insert(String table, String keys, String values) throws SQLException {
        int result;
        statement = connection.createStatement();
        result = statement.executeUpdate("insert into " + table + " (`" + keys + "`) values ('" + values + "')");
        return result;
    }
}
