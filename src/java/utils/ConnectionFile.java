package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFile {

    private static EnvLoader envLoader;
    
    static {
        try {
            // Get the system property set in the listener
            String envPath = System.getProperty("env.filepath");
            
            if (envPath == null) {
                throw new IOException("System Property 'env.filepath' is not set!");
            }
            envLoader = new EnvLoader(envPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        String url = envLoader.get("DB_URL");
        String user = envLoader.get("DB_USER");
        String password = envLoader.get("DB_PASSWORD");

        try {
            if (url == null || user == null || password == null) {
                throw new SQLException("Missing required environment variables for DB connection");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Successful!");
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Connection Failed!");
            System.err.println(ex);
            return null;
        }
    }
}
