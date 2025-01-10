package com.example.cashiersystem.Views;

import com.example.cashiersystem.Controllers.Admin.AdminController;
import com.example.cashiersystem.Controllers.Waiter.WaiterController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    //
    private AccountType loginAccountType;

    // Client Views
    private final ObjectProperty<WaiterMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    public AnchorPane ordersView;
    public AnchorPane reportsView;
    public AnchorPane itemPickerView;

    // Admin Views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane adminDashboardView;
    private AnchorPane changeItemPriceView;


    public ViewFactory(){
        this.loginAccountType = AccountType.WAITER;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /*
     * Waiter Views Section
     * */
    public ObjectProperty<WaiterMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Waiter/Dashboard.fxml")).load();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return dashboardView;
    }

    public AnchorPane getOrdersView() {
        if (ordersView == null){
            try {
                ordersView = new FXMLLoader(getClass().getResource("/Fxml/Waiter/Orders.fxml")).load();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return ordersView;
    }
    public AnchorPane getReportsView() {
        if (reportsView == null){
            try {
                reportsView = new FXMLLoader(getClass().getResource("/Fxml/Waiter/Reports.fxml")).load();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return reportsView;
    }
    public AnchorPane getItemPickerView() {
        if (itemPickerView == null) {
            try {
                itemPickerView = new FXMLLoader(getClass().getResource("/Fxml/Waiter/ItemPicker.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemPickerView;
    }

    public void showWaiterWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Waiter/Waiter.fxml"));
        WaiterController waiterController = new WaiterController();
        loader.setController(waiterController);
        createStage(loader);
    }

    /*
     * Admin Views Section
     * */

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getAdminDashboardView() {
        if (adminDashboardView == null) {
            try {
                adminDashboardView = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminDashboardView;
    }

    public AnchorPane getChangeItemPriceView() {
        if (changeItemPriceView == null) {
            try {
                changeItemPriceView = new FXMLLoader(getClass().getResource("/Fxml/Admin/ChangeItemPrice.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return changeItemPriceView;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController controller = new AdminController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    // stage loader
    public void createStage(FXMLLoader loader){
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        }catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        // stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("Cashier System");
        stage.show();
    }

    // stage closer
    public void closeStage(Stage stage) {
        stage.close();
    }
}
