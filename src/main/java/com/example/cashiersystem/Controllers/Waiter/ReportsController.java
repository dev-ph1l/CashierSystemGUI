package com.example.cashiersystem.Controllers.Waiter;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Model.Report;
import com.example.cashiersystem.Views.AccountType;
import com.example.cashiersystem.Views.Months;
import com.example.cashiersystem.Views.Years;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> yearValues = FXCollections.observableArrayList();
        for (Years year : Years.values()) {
            yearValues.add(year.getValue()); // Use the numeric value from the enum
        }
        report_year_dropdown.setItems(FXCollections.observableArrayList(yearValues));
        report_month_dropdown.setItems(FXCollections.observableArrayList(Months.values()));
        confirm_date_btn.setOnAction(event -> onDailyReport());
        confirm_date_btn_m.setOnAction(event ->onMonthlyReport());
    }

    public void onDailyReport() {
        if (!Objects.equals(report_date_picker.getValue(), null)) {
            Model.getInstance().getReport().dateProperty().set(report_date_picker.getValue().toString());
            // get values
            Model.getInstance().getDatabaseDriver().getDailyReportInfo(Model.getInstance().getReport().dateProperty().get());
            // set values
            showReport();
        } else {
            // show error label --> add!
        }
    }
    public void onMonthlyReport(){
        // set date a combination of both year and month? --> how to realize in query?
    }

    public void showReport() {
        Report report =  Model.getInstance().getReport();

        setLabelText(date_selected_lbl, report.dateProperty().get());
        setLabelText(tot_am_of_orders_lbl, report.totalOrdersProperty().get());
        setLabelText(tot_am_of_orders_you_lbl, report.totalOrdersByWaiterProperty().get());
        setLabelText(tot_am_of_items_lbl, report.totalItemsOrderedProperty().get());
        setLabelText(total_revenue_lbl, report.totalRevenueProperty().get());
        setLabelText(total_purchase_price_lbl, report.totalPurchaseCostProperty().get());
        setLabelText(total_profit_lbl, report.totalProfitProperty().get());
    }

    private void setLabelText(Label label, Object value) {
        label.setText(String.valueOf(value));
    }

}
