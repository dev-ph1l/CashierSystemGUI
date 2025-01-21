package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.Reservation;
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
    public TableView<Reservation> reservation_details_table;
    public TableColumn<Reservation, LocalDateTime> time_column;
    public TableColumn<Reservation, String> name_column;
    public TableColumn<Reservation, String> table_column;
    public TableColumn<Reservation, Integer> count_column;
    public TableColumn<Reservation, String> notes_column;
    public TableColumn<Reservation, String> status_column;
    public TableColumn<Reservation, Void> complete_btn_column;
    public TableColumn<Reservation, Void> cancel_btn_column;


    ObservableList<String> times = FXCollections.observableArrayList("17:00", "17:30", "18:00");
    ObservableList<Integer> guestCount = FXCollections.observableArrayList(1, 2, 3, 4);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateReservationDetailsLabel();

        time_selector.setItems(times);
        guest_amm_selector.setItems(guestCount);

        addListeners();

        table_column.setCellValueFactory(cellData -> cellData.getValue().tableNameProperty());
        name_column.setCellValueFactory(cellData -> cellData.getValue().reservedByProperty());
        time_column.setCellValueFactory(cellData -> cellData.getValue().reservationTimeProperty());
        count_column.setCellValueFactory(cellData -> cellData.getValue().guestCountProperty().asObject());
        notes_column.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        status_column.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // formatting for time coulmn
        time_column.setCellFactory(column -> new TableCell<Reservation, LocalDateTime>() {
            private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalTime().format(timeFormatter));
                }
            }
        });

        // comparator for time column
        time_column.setComparator((time1, time2) -> {
            if (time1 == null || time2 == null) {
                return time1 == null ? 1 : -1;
            }
            return time1.compareTo(time2);
        });

        reservation_details_table.getSortOrder().add(time_column);
        time_column.setSortType(TableColumn.SortType.ASCENDING);

        // create column for Button
        complete_btn_column.setCellFactory(param -> new TableCell<Reservation, Void>() {
            private final Button completedButton = new Button("✔");

            {
                completedButton.setStyle("-fx-text-fill: green;");
                completedButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    reservation.setStatus(ReservationStatus.COMPLETED);
                    Model.getInstance().getDatabaseDriver().updateReservationStatus(reservation);
                    loadReservationsForDate();
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(completedButton);
                }
            }
        });

        cancel_btn_column.setCellFactory(param -> new TableCell<Reservation, Void>() {
            private final Button cancelButton = new Button("✘");

            {
                cancelButton.setStyle("-fx-text-fill: red;");
                cancelButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    reservation.setStatus(ReservationStatus.CANCELLED);
                    Model.getInstance().getDatabaseDriver().updateReservationStatus(reservation);
                    loadReservationsForDate();
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });

        // show reservations
        loadReservationsForDate();
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
        selected_table_lbl.setStyle("");
        selected_table_lbl.setText("Table Selected: " + table);
        Model.getInstance().getReservation().tableNameProperty().set(table);
    }

    private void createReservation() {
        // check if any field isn't filled
        boolean isValid = true;

        if (date_selector.getValue() == null) {
            date_selector.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            date_selector.setStyle("");
        }

        if (time_selector.getValue() == null || time_selector.getValue().isEmpty()) {
            time_selector.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            time_selector.setStyle("");
        }

        if (guest_name_field.getText() == null || guest_name_field.getText().trim().isEmpty()) {
            guest_name_field.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            guest_name_field.setStyle("");
        }

        if (guest_amm_selector.getValue() == null) {
            guest_amm_selector.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            guest_amm_selector.setStyle("");
        }
        if (Model.getInstance().getReservation().tableNameProperty().get() == null ||
                Model.getInstance().getReservation().tableNameProperty().get().isEmpty()) {
            selected_table_lbl.setStyle("-fx-text-fill: red;");  // Visuelle Hervorhebung
            isValid = false;
        } else {
            selected_table_lbl.setStyle("");  // Zurücksetzen
        }

        if (!isValid) {
            return;
        }

        Model.getInstance().getReservation().reservedByProperty().set(guest_name_field.getText());
        LocalDate date = date_selector.getValue();
        String timeString = time_selector.getValue();
        LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        Model.getInstance().getReservation().reservationTimeProperty().set(reservationDateTime);

        Model.getInstance().getReservation().guestCountProperty().set(guest_amm_selector.getValue());
        Model.getInstance().getReservation().notesProperty().set(notes_text_area.getText());
        Model.getInstance().getReservation().setStatus(ReservationStatus.CONFIRMED);

        Model.getInstance().getDatabaseDriver().createReservation();
        loadReservationsForDate();

        // reset everything for next reservation
        resetScene();
    }

    public void loadReservationsForDate() {
        ObservableList<Reservation> reservations = Model.getInstance().getDatabaseDriver().loadTodayReservations();
        reservation_details_table.setItems(reservations);

        // trigger sorting explicitly
        time_column.setSortType(TableColumn.SortType.ASCENDING);
        reservation_details_table.getSortOrder().add(time_column);
        reservation_details_table.sort();
    }

    public void updateReservationDetailsLabel() {
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = today.format(formatter);

        reservation_detail_lbl.setText("Reservation Details for " + formattedDate);
    }

    private void resetScene() {
        error_lbl.setText("");
        selected_table_lbl.setText("No Table Selected");
        date_selector.setValue(null);
        time_selector.setValue(null);
        guest_name_field.setText("");
        guest_amm_selector.setValue(null);
        notes_text_area.setText("");

        date_selector.setStyle("");
        time_selector.setStyle("");
        guest_name_field.setStyle("");
        guest_amm_selector.setStyle("");
        selected_table_lbl.setStyle("");
    }
}
