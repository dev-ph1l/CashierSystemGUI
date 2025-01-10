package com.example.cashiersystem.Model;

import java.sql.*;

public class DatabaseDriver {
    private static final String URL = "jdbc:mysql://127.0.0.1:3360/datenbank_boniersystem";
    private static final String USER = "root";
    private static final String PASSWORD = "Jsb9yfgMKZ58h5tj";

    /**
     *Establishes a connection to the database using the provided credentials.
     * @return a Connection object if successful, null if connection fails.
     * @throws SQLException if there is an issue with the database connection
     */
    public static Connection getConnection() throws SQLException {
        // Attempt to establish a connection to the database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /*
    *  Waiter Section
    * */

    public ResultSet getWaiterData(String name, String password) {
        String query = "SELECT * FROM waiters WHERE username = ? AND password = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    *  Admin Section
    * */


    /*
    * Utility Methods (used by both waiter and admin)
    * */
}
