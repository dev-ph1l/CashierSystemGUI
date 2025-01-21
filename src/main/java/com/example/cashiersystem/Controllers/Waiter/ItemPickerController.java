package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.OrderItem;
import com.example.cashiersystem.Views.WaiterEnums.WaiterMenuOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemPickerController implements Initializable {
    private final Map<String, Integer> buttonIdMap = new HashMap<>();

    public GridPane food_items;


    // control buttons
    public Button place_order_btn;
    public Button abort_order_btn;
    public Button clear_all_items_btn;
    public TableView<OrderItem> selected_items_view;
    public TableColumn<OrderItem, String> itemNameColumn;
    public TableColumn<OrderItem, Integer> quantityColumn;

    private final ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect columns with their properties
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        // register Buttons
        registerButtons();

        place_order_btn.setOnAction(event -> {
            placeOrder();
            clearSelected();
        });
        clear_all_items_btn.setOnAction(event -> {
            Model.getInstance().getOrder().clearItems();
            clearSelected();
        });
        abort_order_btn.setOnAction(event -> {
            Model.getInstance().getOrder().clearItems();
            clearSelected();
            Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ORDERS);
        });
    }

    private void registerButtons() {
        buttonIdMap.put("Water 0.3", 1);
        buttonIdMap.put("Water 0.5", 2);
        buttonIdMap.put("Sparkling Water 0.3", 3);
        buttonIdMap.put("sparkling water 05", 4);
        buttonIdMap.put("cola", 5);
        buttonIdMap.put("sprite", 6);
        buttonIdMap.put("fanta", 7);
        buttonIdMap.put("ice tea peach", 8);
        buttonIdMap.put("ice tea lemon", 9);
        buttonIdMap.put("energy drink", 10);
        buttonIdMap.put("smoothie", 11);
        buttonIdMap.put("herbal tea", 12);
        buttonIdMap.put("hot chocolate", 13);
        buttonIdMap.put("espresso", 14);
        buttonIdMap.put("cappuccino", 15);

        buttonIdMap.put("draft beer", 16);
        buttonIdMap.put("wheat beer", 17);
        buttonIdMap.put("aperol spritz", 18);
        buttonIdMap.put("tequila shot", 19);
        buttonIdMap.put("whisky sour", 20);
        buttonIdMap.put("red wine", 21);
        buttonIdMap.put("white wine", 22);
        buttonIdMap.put("mojito", 23);
        buttonIdMap.put("gin tonic", 24);

        buttonIdMap.put("cosmopolitan", 25);
        buttonIdMap.put("caipirinha", 26);
        buttonIdMap.put("long island iced tea", 27);
        buttonIdMap.put("pina colada", 28);

        buttonIdMap.put("burger", 29);
        buttonIdMap.put("burger witch fries", 30);
        buttonIdMap.put("chicken wings", 31);
        buttonIdMap.put("nachos cheese", 32);
        buttonIdMap.put("ceaser salad", 33);
        buttonIdMap.put("mini pizzas", 34);
        buttonIdMap.put("club sandwich", 35);
        buttonIdMap.put("cheese platter", 36);
        buttonIdMap.put("peanuts", 37);
        buttonIdMap.put("pretzel sticks", 38);
        buttonIdMap.put("chips", 39);

        // dynamically add the buttons to the grid
        food_items.addRow(0);

        AtomicInteger column = new AtomicInteger();
        AtomicInteger row = new AtomicInteger();
        buttonIdMap.forEach((item, id) -> {
            Button button = new Button(item);
            button.setOnAction(event -> {
                // Set up event listener
                addItem(id);
                displaySelected(id);
            });

            if (column.get() == 6) {
                column.set(0);
                row.getAndIncrement();
                food_items.addRow(row.get());
            }

            food_items.add(button, column.get(), row.get());
            column.getAndIncrement();
        });
    }


    private void addItem(int id) {
        Model.getInstance().getOrder().addItemId(id);
    }

    private void displaySelected(int id) {
        String itemName = Model.getInstance().getDatabaseDriver().getItemName(id);
        int quantity = Model.getInstance().getOrder().getQuantityForItemId(id);

        // check if item already is in the list
        for (OrderItem item : orderItems) {
            if (item.nameProperty().get().equals(itemName)) {
                // increase quantity
                item.quantityProperty().set(quantity);
                selected_items_view.refresh();
                return;
            }
        }

        // add item if it doesn't exist
        orderItems.add(new OrderItem(itemName, quantity));
        selected_items_view.setItems(orderItems);
    }

    private void clearSelected() {
        selected_items_view.setItems(null);
        orderItems.clear();
    }

    private void placeOrder() {
        Model.getInstance().getDatabaseDriver().createOrder();
    }
}
