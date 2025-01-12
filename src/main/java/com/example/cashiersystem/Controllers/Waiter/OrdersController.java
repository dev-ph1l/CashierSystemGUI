package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    public Button table1_btn;
    public Button table2_btn;
    public Button table3_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table1_btn.setOnAction(event -> onTableClicked(1));
        table2_btn.setOnAction(event -> onTableClicked(2));
        table3_btn.setOnAction(event -> onTableClicked(3));
    }

    private void onTableClicked(int tableId) {
        Model.getInstance().getOrder().waiterIdProperty().set(Model.getInstance().getWaiter().waiterIdProperty().get()); // redundant? just leave out waiterId in ORDER?
        Model.getInstance().getOrder().tableIdProperty().set(tableId);
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ITEMPICKER);
    }
}
