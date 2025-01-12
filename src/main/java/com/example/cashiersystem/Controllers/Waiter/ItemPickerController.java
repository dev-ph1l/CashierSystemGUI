package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemPickerController implements Initializable {
    public Button water_btn;
    public Button cola_btn;
    public Button burger_btn;
    public Button fries_btn;
    public Button place_order_btn;
    public Button abort_order_btn;
    public Button clear_all_items_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cola_btn.setOnAction(event -> addItem(1));
        water_btn.setOnAction(event -> addItem(2));

        place_order_btn.setOnAction(event -> placeOrder());
    }

    private void addItem(int id) {
        Model.getInstance().getOrder().addItemId(id);
    }

    private void placeOrder() {
        Model.getInstance().getDatabaseDriver().createOrder();
    }

}
