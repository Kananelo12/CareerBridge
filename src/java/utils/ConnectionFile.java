package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFile {
    public static Connection getConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/2230541", "root", "");
            System.out.println("Connection Successful!");
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Connection Failed!");
            System.err.println(ex);
            return null;
        }
    }
}
