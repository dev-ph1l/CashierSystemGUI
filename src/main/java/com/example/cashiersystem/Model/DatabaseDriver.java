package com.example.cashiersystem.Model;

import javax.xml.transform.Source;
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

    public ResultSet getWaiterData(String username, String password) {
        String query = "SELECT * FROM waiters WHERE username = ? AND password = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createOrder(Integer tableId) {
        Model.getInstance().getOrder().waiterIdProperty().set(Model.getInstance().getWaiter().waiterIdProperty().get());
        Model.getInstance().getOrder().tableIdProperty().set(tableId);
        // SQL query to insert a new order into the orders table (with the current date)
        String insertOrderQuery = "INSERT INTO orders (table_id, order_date, waiter_id) VALUES (?, NOW(), ?)";

        int orderId = -1;
        try (Connection connection = DatabaseDriver.getConnection()) {
            // Prepare the query to insert the new order into the orders table
            PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStatement.setInt(1, Model.getInstance().getOrder().tableIdProperty().get());
            orderStatement.setInt(2, Model.getInstance().getOrder().waiterIdProperty().get()); // Get the logged-in waiter ID
            orderStatement.executeUpdate();


            // Retrieve the generated order ID after inserting the order
            var generatedKeys = orderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1); // Get the generated order ID
                Model.getInstance().getOrder().orderIdProperty().set(orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    *  Admin Section
    * */


    /*
    * Utility Methods (used by both waiter and admin)
    * */
}
