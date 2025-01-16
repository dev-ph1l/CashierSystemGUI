package com.example.cashiersystem.Model;

import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Map;

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

    // retrieves the waiter password and username for login
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

    // creates an order, to which the items ordered are connected (via the order_id)
    public void createOrder() {
        String insertOrderQuery = "INSERT INTO orders (table_id, order_date, waiter_id) VALUES (?, NOW(), ?)";

        String insertOrderItemQuery = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection()) {
            PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStatement.setInt(1, Model.getInstance().getOrder().tableIdProperty().get());
            orderStatement.setInt(2, Model.getInstance().getOrder().waiterIdProperty().get());
            orderStatement.executeUpdate();

            int orderId = -1;
            try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1); // Get the generated order ID
                }
            }

            PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemQuery);

            Map<Integer, Integer> itemMap = Model.getInstance().getOrder().getItemQuantities(); // Get the item quantities map
            for (Map.Entry<Integer, Integer> entry : itemMap.entrySet()) {
                int itemId = entry.getKey();
                int itemQuantity = entry.getValue();
                orderItemStatement.setInt(1, orderId);
                orderItemStatement.setInt(2, itemId);
                orderItemStatement.setInt(3, itemQuantity);
                orderItemStatement.executeUpdate();
            }

            Model.getInstance().getOrder().clearItems();
            Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ORDERS);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // retrieves data for daily report
    public void getDailyReportInfo(String date) {
        String query = """
                SELECT\s
                            COUNT(DISTINCT o.id) AS total_orders,
                            COUNT(DISTINCT CASE WHEN w.id = ? THEN o.id END) AS waiter_orders,
                            SUM(oi.quantity) AS total_items_ordered,
                            SUM(oi.quantity * m.price) AS total_price,
                            SUM(oi.quantity * m.purchase_price) AS total_purchase_cost
                        FROM orders o
                        JOIN order_items oi ON o.id = oi.order_id
                        JOIN menu_items m ON oi.menu_item_id = m.id
                        JOIN waiters w ON o.waiter_id = w.id
                        WHERE DATE(o.order_date) = ?;
        """;
        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Model.getInstance().getWaiter().waiterIdProperty().get());
            preparedStatement.setString(2, date);

            // Execute query and process results
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Model.getInstance().getReport().totalOrdersProperty().set(rs.getInt("total_orders"));
                    Model.getInstance().getReport().totalOrdersByWaiterProperty().set(rs.getInt("waiter_orders"));
                    Model.getInstance().getReport().totalItemsOrderedProperty().set(rs.getInt("total_items_ordered"));
                    Model.getInstance().getReport().totalRevenueProperty().set(rs.getDouble("total_price"));
                    Model.getInstance().getReport().totalPurchaseCostProperty().set(rs.getDouble("total_purchase_cost"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // retrieves data for monthly report
    public void getMonthlyReportInfo(String date) {

    }

    // returns item name corresponding to its id, used to display items added to an order in ItemPicker
    public String getItemName(int ItemId) {


        String getNamesQuery = "SELECT name FROM menu_items WHERE id = ?";

        String itemName = null;
        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getNamesQuery)) {
            preparedStatement.setInt(1, ItemId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    itemName = rs.getString("name");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemName;
    }


    /*
    *  Admin Section
    * */

    // retrieves the admin password and username for login
    public ResultSet getAdminData(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
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


    /*
    * Utility Methods (used by both waiter and admin)
    * */
}
