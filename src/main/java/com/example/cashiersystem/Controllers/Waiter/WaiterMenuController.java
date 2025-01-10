package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class WaiterMenuController implements Initializable {
    public Button dashboard_btn;
    public Button orders_btn;
    public Button reports_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        orders_btn.setOnAction(event -> onOrders());
        reports_btn.setOnAction(event ->onReports());
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.DASHBOARD);
    }

    private void onOrders() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ORDERS);
    }

    private void onReports() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.REPORTS);
    }
}
