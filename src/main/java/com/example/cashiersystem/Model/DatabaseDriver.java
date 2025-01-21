package com.example.cashiersystem.Model;

import com.example.cashiersystem.Views.WaiterEnums.ReservationStatus;
import com.example.cashiersystem.Views.WaiterEnums.WaiterMenuOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseDriver {
    private static final String URL = "jdbc:mysql://localhost:3360/datenbank_boniersystem";
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

            Map<Integer, Integer> itemMap = Model.getInstance().getOrder().getItemMap(); // Get the item quantities map
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
            SELECT 
                COUNT(DISTINCT o.id) AS total_orders,
                COUNT(DISTINCT CASE WHEN a.id = ? AND a.account_type = 'WAITER' THEN o.id END) AS waiter_orders,
                SUM(oi.quantity) AS total_items_ordered,
                SUM(oi.quantity * m.price) AS total_price,
                SUM(oi.quantity * m.purchase_price) AS total_purchase_cost
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN menu_items m ON oi.menu_item_id = m.id
            JOIN account a ON o.waiter_id = a.id
            WHERE DATE(o.order_date) = ?;
    """;

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the logged-in waiter's ID
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

    // method to retrieve report breakdown for daily report
    public List<ReportDetail> getDailyReportDetail(String date) {
        String query = """
        SELECT o.id, o.order_date, a.username AS waiter_name, oi.menu_item_id, m.name AS item_name, oi.quantity, 
               (oi.quantity * m.price) AS revenue, (oi.quantity * m.purchase_price) AS purchase_price, 
               ((oi.quantity * m.price) - (oi.quantity * m.purchase_price)) AS profit
        FROM orders o
        JOIN order_items oi ON o.id = oi.order_id
        JOIN menu_items m ON oi.menu_item_id = m.id
        JOIN account a ON o.waiter_id = a.id
        WHERE DATE(o.order_date) = ?
    """;

        Map<Integer, ReportDetail> reportMap = new HashMap<>();

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, date);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("id");
                    String orderDate = rs.getString("order_date");
                    String waiterName = rs.getString("waiter_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    double revenue = rs.getDouble("revenue");
                    double purchasePrice = rs.getDouble("purchase_price");
                    double profit = rs.getDouble("profit");

                    // create OrderItem for every item
                    OrderItem orderItem = new OrderItem(itemName, quantity);

                    // find or create ReportDetail for this order
                    ReportDetail reportDetail = reportMap.get(orderId);
                    if (reportDetail == null) {
                        reportDetail = new ReportDetail(orderDate, waiterName, new ArrayList<>(), 0.0, 0.0, 0.0);
                        reportMap.put(orderId, reportDetail);
                    }

                    // add to list of orders
                    reportDetail.getItems().add(orderItem);

                    reportDetail.revenueProperty().set(reportDetail.revenueProperty().get() + revenue);
                    reportDetail.purchasePriceProperty().set(reportDetail.purchasePriceProperty().get() + purchasePrice);
                    reportDetail.profitProperty().set(reportDetail.profitProperty().get() + profit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(reportMap.values());
    }


    // retrieves data for monthly report
    public void getMonthlyReportInfo(String date) {
        String query = """
        SELECT 
            COUNT(DISTINCT o.id) AS total_orders,
            COUNT(DISTINCT CASE WHEN a.id = ? AND a.account_type = 'WAITER' THEN o.id END) AS waiter_orders,
            SUM(oi.quantity) AS total_items_ordered,
            SUM(oi.quantity * m.price) AS total_price,
            SUM(oi.quantity * m.purchase_price) AS total_purchase_cost
        FROM orders o
        JOIN order_items oi ON o.id = oi.order_id
        JOIN menu_items m ON oi.menu_item_id = m.id
        JOIN account a ON o.waiter_id = a.id
        WHERE DATE_FORMAT(o.order_date, '%Y-%m') = ?;
    """;

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the logged-in waiter's ID
            preparedStatement.setInt(1, Model.getInstance().getWaiter().waiterIdProperty().get());
            preparedStatement.setString(2, date); // Hier übergeben wir das Datum im Format YYYY-MM

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

    // method to retrieve report breakdown for monthly report
    public List<ReportDetail> getMonthlyReportDetail(String month) {
        String query = """
        SELECT o.id, o.order_date, a.username AS waiter_name, oi.menu_item_id, m.name AS item_name, oi.quantity, 
               (oi.quantity * m.price) AS revenue, (oi.quantity * m.purchase_price) AS purchase_price, 
               ((oi.quantity * m.price) - (oi.quantity * m.purchase_price)) AS profit
        FROM orders o
        JOIN order_items oi ON o.id = oi.order_id
        JOIN menu_items m ON oi.menu_item_id = m.id
        JOIN account a ON o.waiter_id = a.id
        WHERE DATE_FORMAT(o.order_date, '%Y-%m') = ?  -- Filter für den Monat (yyyy-MM)
    """;

        Map<Integer, ReportDetail> reportMap = new HashMap<>();

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, month);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("id");
                    String orderDate = rs.getString("order_date");
                    String waiterName = rs.getString("waiter_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    double revenue = rs.getDouble("revenue");
                    double purchasePrice = rs.getDouble("purchase_price");
                    double profit = rs.getDouble("profit");

                    OrderItem orderItem = new OrderItem(itemName, quantity);

                    ReportDetail reportDetail = reportMap.get(orderId);
                    if (reportDetail == null) {
                        reportDetail = new ReportDetail(orderDate, waiterName, new ArrayList<>(), 0.0, 0.0, 0.0);
                        reportMap.put(orderId, reportDetail);
                    }

                    reportDetail.getItems().add(orderItem);

                    reportDetail.revenueProperty().set(reportDetail.revenueProperty().get() + revenue);
                    reportDetail.purchasePriceProperty().set(reportDetail.purchasePriceProperty().get() + purchasePrice);
                    reportDetail.profitProperty().set(reportDetail.profitProperty().get() + profit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(reportMap.values());
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

    public void createReservation() {
        String query = "INSERT INTO reservations (table_name, reserved_by, reservation_date, guest_count, notes, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Model.getInstance().getReservation().tableNameProperty().get());
            preparedStatement.setString(2, Model.getInstance().getReservation().reservedByProperty().get());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(Model.getInstance().getReservation().reservationTimeProperty().get()));
            preparedStatement.setInt(4, Model.getInstance().getReservation().guestCountProperty().get());
            preparedStatement.setString(5, Model.getInstance().getReservation().notesProperty().get());
            preparedStatement.setString(6, Model.getInstance().getReservation().statusProperty().get());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Reservation> loadTodayReservations() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        String query = "SELECT * FROM reservations WHERE DATE(reservation_date) = CURDATE()";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int reservationId = resultSet.getInt("id");
                String tableName = resultSet.getString("table_name");
                String reservedBy = resultSet.getString("reserved_by");
                LocalDateTime reservationTime = resultSet.getTimestamp("reservation_date").toLocalDateTime();
                int guestCount = resultSet.getInt("guest_count");
                String notes = resultSet.getString("notes");
                String status = resultSet.getString("status");

                reservations.add(new Reservation(reservationId, tableName, reservedBy, reservationTime, guestCount, notes, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public void updateReservationStatus(Reservation reservation) {
        String query = "UPDATE reservations SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, reservation.getStatus().toString());
            preparedStatement.setInt(2, reservation.reservationIdProperty().get());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
    *  Admin Section
    * */


    /*
    * Utility Methods (used by both waiter and admin)
    * */

    public ResultSet getLoginData(String username, String password, String accountType) {
        String query = "SELECT * FROM account WHERE username = ? AND password = ? AND account_type = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, accountType);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
