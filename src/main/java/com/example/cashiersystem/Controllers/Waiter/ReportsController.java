package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.Report;
import com.example.cashiersystem.Model.ReportDetail;
import com.example.cashiersystem.Views.WaiterEnums.Months;
import com.example.cashiersystem.Views.WaiterEnums.Years;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class ReportsController implements Initializable {
    public DatePicker report_date_picker;
    public Button confirm_date_btn;
    public Label date_selected_lbl;
    public Label tot_am_of_orders_lbl;
    public Label tot_am_of_orders_you_lbl;
    public Label tot_am_of_items_lbl;
    public Label total_revenue_lbl;
    public Label total_purchase_price_lbl;
    public Label total_profit_lbl;
    public Label date_selected_lbl_m;
    public Label tot_am_of_orders_lbl_m;
    public Label tot_am_of_orders_you_lbl_m;
    public Label tot_am_of_items_lbl_m;
    public Label total_revenue_lbl_m;
    public Label total_purchase_price_lbl_m;
    public Label total_profit_lbl_m;

    public ChoiceBox<Integer> report_year_dropdown;
    public ChoiceBox<Months> report_month_dropdown;

    public Button reset_btn;
    public Button confirm_date_btn_m;

    public Label error_lbl;

    @FXML
    private TableView<ReportDetail> reportDetailsView;

    @FXML
    private TableColumn<ReportDetail, String> orderDateColumn;
    @FXML
    private TableColumn<ReportDetail, String> waiterColumn;
    @FXML
    private TableColumn<ReportDetail, String> itemsColumn;
    @FXML
    private TableColumn<ReportDetail, Double> revenueColumn;
    @FXML
    private TableColumn<ReportDetail, Double> purchasePriceColumn;
    @FXML
    private TableColumn<ReportDetail, Double> profitColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> yearValues = FXCollections.observableArrayList();
        for (Years year : Years.values()) {
            yearValues.add(year.getValue());
        }
        report_year_dropdown.setItems(FXCollections.observableArrayList(yearValues));
        report_month_dropdown.setItems(FXCollections.observableArrayList(Months.values()));
        confirm_date_btn.setOnAction(event -> onDailyReport());
        confirm_date_btn_m.setOnAction(event -> onMonthlyReport());
        reset_btn.setOnAction(event -> resetReports());

        // Set RowFactory for dynamic rowheight
        reportDetailsView.setRowFactory(tv -> {
            TableRow<ReportDetail> row = new TableRow<>();

            // Add a listener to dynamically adjust the height
            row.itemProperty().addListener((observable, oldItem, newItem) -> {
                if (newItem != null) {
                    double maxHeight = 0;

                    // Iterate over all columns
                    for (TableColumn<ReportDetail, ?> column : reportDetailsView.getColumns()) {
                        // Get the text value for each column
                        String cellText = column.getCellData(row.getIndex()).toString();
                        double cellHeight = calculateCellHeight(cellText);
                        maxHeight = Math.max(maxHeight, cellHeight); // Get the maximum height of all cells
                    }
                    row.setPrefHeight(maxHeight); // Set the row's preferred height

                    double minHeight = 3 * (new Font("Lucida Console", 14).getSize());
                    row.setPrefHeight(Math.max(maxHeight, minHeight));
                }
            });

            return row;
        });

        // connections
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        waiterColumn.setCellValueFactory(cellData -> cellData.getValue().waiterNameProperty());
        itemsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedItems()));
        revenueColumn.setCellValueFactory(cellData -> cellData.getValue().revenueProperty().asObject());
        purchasePriceColumn.setCellValueFactory(cellData -> cellData.getValue().purchasePriceProperty().asObject());
        profitColumn.setCellValueFactory(cellData -> cellData.getValue().profitProperty().asObject());
    }

    private double calculateCellHeight(String text) {
        if (text == null || text.isEmpty()) {
            return 30; // Default minimum height
        }

        // Create a Text node to simulate the content and measure its height
        Text tempText = new Text(text);
        tempText.setFont(new Font("Lucida Console", 14)); // Use the same font as in your table
        tempText.setWrappingWidth(320); // Set the same width as the column

        // Use the Text's bounds to get the height
        tempText.setText(text);
        double height = tempText.getLayoutBounds().getHeight();

        return height + 10; // Add padding for comfort
    }



    public void onDailyReport() {
        if (!Objects.equals(report_date_picker.getValue(), null)) {
            Model.getInstance().getReport().dateProperty().set(report_date_picker.getValue().toString());

            // daily report
            Model.getInstance().getDatabaseDriver().getDailyReportInfo(Model.getInstance().getReport().dateProperty().get());
            showDailyReport();

            // daily report breakdown
            List<ReportDetail> reportDetails = Model.getInstance().getDatabaseDriver().getDailyReportDetail(Model.getInstance().getReport().dateProperty().get());
            reportDetailsView.setItems(FXCollections.observableArrayList(reportDetails));
        }
    }

    public void onMonthlyReport() {
        if (report_month_dropdown.getValue() != null && report_year_dropdown.getValue() != null) {
            error_lbl.setText("");
            Months selectedMonth = report_month_dropdown.getValue();
            int selectedYear = report_year_dropdown.getValue();

            setMonthlyReportDate(selectedMonth, selectedYear);


            // monthly report
            Model.getInstance().getDatabaseDriver().getMonthlyReportInfo(Model.getInstance().getReport().dateProperty().get());
            showMonthlyReport();

            // monthly report breakdown
            List<ReportDetail> reportDetails = Model.getInstance().getDatabaseDriver().getMonthlyReportDetail(Model.getInstance().getReport().dateProperty().get());
            reportDetailsView.setItems(FXCollections.observableArrayList(reportDetails));
        } else {
            error_lbl.setText("Please select both month and year!");
        }
    }

    public void setMonthlyReportDate(Months selectedMonth, int selectedYear) {
        int fullYear = 2000 + selectedYear;

        int monthValue = selectedMonth.ordinal() + 1;
        String monthFormatted = String.format("%02d", monthValue);

        String formattedDate = fullYear + "-" + monthFormatted;

        Model.getInstance().getReport().dateProperty().set(formattedDate);
    }

    public void showDailyReport() {
        Report report =  Model.getInstance().getReport();

        setLabelText(date_selected_lbl, report.dateProperty().get());
        setLabelText(tot_am_of_orders_lbl, report.totalOrdersProperty().get());
        setLabelText(tot_am_of_orders_you_lbl, report.totalOrdersByWaiterProperty().get());
        setLabelText(tot_am_of_items_lbl, report.totalItemsOrderedProperty().get());
        setLabelText(total_revenue_lbl, report.totalRevenueProperty().get() + " $");
        setLabelText(total_purchase_price_lbl, report.totalPurchaseCostProperty().get() + " $");
        String formattedProfit = String.format(Locale.US, "%.2f", report.totalProfitProperty().get());
        total_profit_lbl.setText(formattedProfit + " $");

    }

    private void showMonthlyReport() {
        Report report =  Model.getInstance().getReport();

        setLabelText(date_selected_lbl_m, report.dateProperty().get());
        setLabelText(tot_am_of_orders_lbl_m, report.totalOrdersProperty().get());
        setLabelText(tot_am_of_orders_you_lbl_m, report.totalOrdersByWaiterProperty().get());
        setLabelText(tot_am_of_items_lbl_m, report.totalItemsOrderedProperty().get());
        setLabelText(total_revenue_lbl_m, report.totalRevenueProperty().get() + " $");
        setLabelText(total_purchase_price_lbl_m, report.totalPurchaseCostProperty().get() + " $");
        String formattedProfit = String.format(Locale.US, "%.2f", report.totalProfitProperty().get());
        total_profit_lbl_m.setText(formattedProfit + " $");
    }

    private void setLabelText(Label label, Object value) {
        label.setText(String.valueOf(value));
    }

    private void resetReports() {
        date_selected_lbl.setText("");
        tot_am_of_orders_lbl.setText("");
        tot_am_of_orders_you_lbl.setText("");
        tot_am_of_items_lbl.setText("");
        total_revenue_lbl.setText("");
        total_purchase_price_lbl.setText("");
        total_profit_lbl.setText("");
        date_selected_lbl_m.setText("");
        tot_am_of_orders_lbl_m.setText("");
        tot_am_of_orders_you_lbl_m.setText("");
        tot_am_of_items_lbl_m.setText("");
        total_revenue_lbl_m.setText("");
        total_purchase_price_lbl_m.setText("");
        total_profit_lbl_m.setText("");

        report_year_dropdown.setValue(null);
        report_month_dropdown.setValue(null);
        report_date_picker.setValue(null);

        reportDetailsView.setItems(null);
    }
}
