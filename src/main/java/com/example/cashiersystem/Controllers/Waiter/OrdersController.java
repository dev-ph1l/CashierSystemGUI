package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.Order;
import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    public Button table1_btn;
    public Button table2_btn;
    public Button table3_btn;

    private Order newOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table1_btn.setOnAction(event -> onTableClicked(1));
        table2_btn.setOnAction(event -> onTableClicked(2));
        table3_btn.setOnAction(event -> onTableClicked(3));
    }

    private void onTableClicked(int tableId) {
        Model.getInstance().getDatabaseDriver().createOrder(tableId);
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ITEMPICKER);

    }
}
