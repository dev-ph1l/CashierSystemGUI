package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Views.WaiterMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
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

    // control buttons
    public Button place_order_btn;
    public Button abort_order_btn;
    public Button clear_all_items_btn;
    public ListView<Integer> selected_items_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();

        place_order_btn.setOnAction(event -> placeOrder());
        clear_all_items_btn.setOnAction(event -> Model.getInstance().getOrder().clearItems());
        abort_order_btn.setOnAction(event -> {
            Model.getInstance().getOrder().clearItems();
            Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(WaiterMenuOptions.ORDERS);
        });
    }

    private void addListeners() {
        water_03_btn.setOnAction(event -> addItem(1));
        water_05_btn.setOnAction(event -> addItem(2));
        sparkling_water_03_btn.setOnAction(event -> addItem(3));
        sparkling_water_05_btn.setOnAction(event -> addItem(4));
        cola_btn.setOnAction(event -> addItem(5));
        sprite_btn.setOnAction(event -> addItem(6));
        fanta_btn.setOnAction(event -> addItem(7));
        ice_tea_peach_btn.setOnAction(event -> addItem(8));
        ice_tea_lemon_btn.setOnAction(event -> addItem(9));
        energy_drink_btn.setOnAction(event -> addItem(10));
        smoothie_btn.setOnAction(event -> addItem(11));
        herbal_tea_btn.setOnAction(event -> addItem(12));
        hot_chocolate_btn.setOnAction(event -> addItem(13));
        espresso_btn.setOnAction(event -> addItem(14));
        cappuccino_btn.setOnAction(event -> addItem(15));

        draft_beer_btn.setOnAction(event -> addItem(16));
        wheat_beer_btn.setOnAction(event -> addItem(17));
        aperol_spritz_btn.setOnAction(event -> addItem(18));
        tequila_shot_btn.setOnAction(event -> addItem(19));
        whisky_sour_btn.setOnAction(event -> addItem(20));
        red_wine_btn.setOnAction(event -> addItem(21));
        white_wine_btn.setOnAction(event -> addItem(22));
        mojito_btn.setOnAction(event -> addItem(23));
        gin_tonic_btn.setOnAction(event -> addItem(24));

        cosmopolitan_btn.setOnAction(event -> addItem(25));
        caipirinha_btn.setOnAction(event -> addItem(26));
        long_island_iced_tea_btn.setOnAction(event -> addItem(27));
        pina_colada_btn.setOnAction(event -> addItem(28));


        burger_btn.setOnAction(event -> addItem(29));
        burger_witch_fries_btn.setOnAction(event -> addItem(30));
        chicken_wings_btn.setOnAction(event -> addItem(31));
        nachos_cheese_btn.setOnAction(event -> addItem(32));
        ceaser_salad_btn.setOnAction(event -> addItem(33));

        mini_pizzas_btn.setOnAction(event -> addItem(34));
        club_sandwich_btn.setOnAction(event -> addItem(35));
        cheese_platter_btn.setOnAction(event -> addItem(36));
        peanuts_btn.setOnAction(event -> addItem(37));
        pretzel_sticks_btn.setOnAction(event -> addItem(38));
        chips_btn.setOnAction(event -> addItem(39));
    }

    private void addItem(int id) {
        Model.getInstance().getOrder().addItemId(id);
        selected_items_view.setItems(Model.getInstance().getOrder().itemIdsProperty());
    }

    private void displaySelected() {

    }

    private void placeOrder() {
        Model.getInstance().getDatabaseDriver().createOrder();
    }

}
