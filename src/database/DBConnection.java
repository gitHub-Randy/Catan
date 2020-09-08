package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection connection;

    public DBConnection() {
    	getConnection();
    }

    public static void getConnection() {
        if (connection == null) {
            makeConnection();
        }
    }

    public static boolean loadDataBaseDriver(String driverName) {
        try {
            // Load the JDBC driver
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // Could not find the database driver
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean makeConnection() {
        loadDataBaseDriver("com.mysql.jdbc.Driver");
        try {
//            connection = DriverManager.getConnection("jdbc:mysql://databases.aii.avans.nl/rawkamp_db?user=rawkamp&password=Ab12345");
            connection = DriverManager.getConnection("jdbc:mysql://databases.aii.avans.nl/3_soprj4_catan?user=42IN04SOa&password=afvaardiging");
        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean checkConnection() {
        getConnection();

        return connection != null;
    }

}

