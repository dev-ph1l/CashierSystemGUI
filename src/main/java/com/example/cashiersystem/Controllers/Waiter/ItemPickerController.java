package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.OrderItem;
import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ItemPickerController implements Initializable {
    // drinks
    public Button water_03_btn;
    public Button water_05_btn;
    public Button sparkling_water_03_btn;
    public Button sparkling_water_05_btn;
    public Button cola_btn;
    public Button sprite_btn;
    public Button fanta_btn;
    public Button ice_tea_peach_btn;
    public Button ice_tea_lemon_btn;
    public Button energy_drink_btn;
    public Button smoothie_btn;
    public Button herbal_tea_btn;
    public Button hot_chocolate_btn;
    public Button cappuccino_btn;
    public Button draft_beer_btn;
    public Button wheat_beer_btn;
    public Button aperol_spritz_btn;
    public Button mojito_btn;
    public Button gin_tonic_btn;
    public Button long_island_iced_tea_btn;
    public Button whisky_sour_btn;
    public Button red_wine_btn;
    public Button white_wine_btn;
    public Button tequila_shot_btn;
    public Button espresso_btn;
    public Button pina_colada_btn;
    public Button cosmopolitan_btn;
    public Button caipirinha_btn;

    // food
    public Button burger_btn;
    public Button burger_witch_fries_btn;
    public Button chicken_wings_btn;
    public Button club_sandwich_btn;
    public Button nachos_cheese_btn;
    public Button ceaser_salad_btn;
    public Button mini_pizzas_btn;
    public Button cheese_platter_btn;
    public Button peanuts_btn;
    public Button pretzel_sticks_btn;
    public Button chips_btn;

    private final Map<Button, Integer> buttonIdMap = new HashMap<>();


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

        // Set up event listeners for each button
        buttonIdMap.forEach((button, id) -> button.setOnAction(event -> {
            addItem(id);
            displaySelected(id);
        }));

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
        buttonIdMap.put(water_03_btn, 1);
        buttonIdMap.put(water_05_btn, 2);
        buttonIdMap.put(sparkling_water_03_btn, 3);
        buttonIdMap.put(sparkling_water_05_btn, 4);
        buttonIdMap.put(cola_btn, 5);
        buttonIdMap.put(sprite_btn, 6);
        buttonIdMap.put(fanta_btn, 7);
        buttonIdMap.put(ice_tea_peach_btn, 8);
        buttonIdMap.put(ice_tea_lemon_btn, 9);
        buttonIdMap.put(energy_drink_btn, 10);
        buttonIdMap.put(smoothie_btn, 11);
        buttonIdMap.put(herbal_tea_btn, 12);
        buttonIdMap.put(hot_chocolate_btn, 13);
        buttonIdMap.put(espresso_btn, 14);
        buttonIdMap.put(cappuccino_btn, 15);

        buttonIdMap.put(draft_beer_btn, 16);
        buttonIdMap.put(wheat_beer_btn, 17);
        buttonIdMap.put(aperol_spritz_btn, 18);
        buttonIdMap.put(tequila_shot_btn, 19);
        buttonIdMap.put(whisky_sour_btn, 20);
        buttonIdMap.put(red_wine_btn, 21);
        buttonIdMap.put(white_wine_btn, 22);
        buttonIdMap.put(mojito_btn, 23);
        buttonIdMap.put(gin_tonic_btn, 24);

        buttonIdMap.put(cosmopolitan_btn, 25);
        buttonIdMap.put(caipirinha_btn, 26);
        buttonIdMap.put(long_island_iced_tea_btn, 27);
        buttonIdMap.put(pina_colada_btn, 28);

        buttonIdMap.put(burger_btn, 29);
        buttonIdMap.put(burger_witch_fries_btn, 30);
        buttonIdMap.put(chicken_wings_btn, 31);
        buttonIdMap.put(nachos_cheese_btn, 32);
        buttonIdMap.put(ceaser_salad_btn, 33);
        buttonIdMap.put(mini_pizzas_btn, 34);
        buttonIdMap.put(club_sandwich_btn, 35);
        buttonIdMap.put(cheese_platter_btn, 36);
        buttonIdMap.put(peanuts_btn, 37);
        buttonIdMap.put(pretzel_sticks_btn, 38);
        buttonIdMap.put(chips_btn, 39);
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
