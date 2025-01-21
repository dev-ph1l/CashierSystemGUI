package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Views.AccountType;
import com.example.cashiersystem.Views.WaiterEnums.ReservationStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    public DatePicker date_selector;
    public ChoiceBox<String> time_selector;
    public TextField guest_name_field;
    public ChoiceBox<Integer> guest_amm_selector;
    public TextArea notes_text_area;

    public Button bar1_btn;
    public Button bar2_btn;
    public Button bar3_btn;
    public Button bar4_btn;
    public Button bar5_btn;
    public Button table1_btn;
    public Button table2_btn;
    public Button table3_btn;
    public Button table4_btn;
    public Button table5_btn;
    public Button table6_btn;
    public Label selected_table_lbl;

    public Button confirm_btn;
    public Button reset_btn;
    public Label error_lbl;

    public Label reservation_detail_lbl;
    public TableView reservation_details_table;

    ObservableList<String> times = FXCollections.observableArrayList("17:00", "17:30", "18:00");
    ObservableList<Integer> guestCount = FXCollections.observableArrayList(1, 2, 3, 4);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        time_selector.setItems(times);
        guest_amm_selector.setItems(guestCount);

        addListeners();
    }

    private void addListeners() {
        confirm_btn.setOnAction(event -> createReservation());
        reset_btn.setOnAction(event -> resetScene());

        bar1_btn.setOnAction(event -> onTableClicked("BAR 1"));
        bar2_btn.setOnAction(event -> onTableClicked("BAR 2"));
        bar3_btn.setOnAction(event -> onTableClicked("BAR 3"));
        bar4_btn.setOnAction(event -> onTableClicked("BAR 4"));
        bar5_btn.setOnAction(event -> onTableClicked("BAR 5"));
        table1_btn.setOnAction(event -> onTableClicked("TABLE 1"));
        table2_btn.setOnAction(event -> onTableClicked("TABLE 2"));
        table3_btn.setOnAction(event -> onTableClicked("TABLE 3"));
        table4_btn.setOnAction(event -> onTableClicked("TABLE 4"));
        table5_btn.setOnAction(event -> onTableClicked("TABLE 5"));
        table6_btn.setOnAction(event -> onTableClicked("TABLE 6"));
    }

    private void onTableClicked(String table) {
        selected_table_lbl.setText("Table Selected: " + table);
        Model.getInstance().getReservation().tableNameProperty().set(table);
    }

    private void createReservation() {
        Model.getInstance().getReservation().reservedByProperty().set(guest_name_field.getText());

        //
        LocalDate date = date_selector.getValue();
        String timeString = time_selector.getValue();
        LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        Model.getInstance().getReservation().reservationTimeProperty().set(reservationDateTime);

        Model.getInstance().getReservation().guestCountProperty().set(guest_amm_selector.getValue());
        Model.getInstance().getReservation().notesProperty().set(notes_text_area.getText());
        Model.getInstance().getReservation().setStatus(ReservationStatus.CONFIRMED);

        Model.getInstance().getDatabaseDriver().createReservation();
    }

    private void resetScene() {
        selected_table_lbl.setText("No Table Selected");
    }
}
