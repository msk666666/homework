package demo02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcuUtils {
    static final String URL = "jdbc:mysql://localhost:3306/homework";
    static final String NAME = "root";
    static final String PASSWORD = "westos";

    public static Connection getConnection() throws SQLException {
        System.out.println("数据库已连接");
        return DriverManager.getConnection(URL, NAME, PASSWORD);
    }
}
