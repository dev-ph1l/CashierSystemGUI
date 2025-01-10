package com.example.cashiersystem.Model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Order {
    private final StringProperty waiter;
    private final DoubleProperty tableId;
    private final StringProperty item;              //double for itemid?
    private final ObjectProperty<LocalDate> date;

    public Order(String waiter, double tableId, String item, LocalDate date) {
        this.waiter = new SimpleStringProperty(this, "Waiter", waiter);
        this.tableId = new SimpleDoubleProperty(this, "TableId", tableId);
        this.item = new SimpleStringProperty(this, "Item", item);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
    }

    // getters
    public StringProperty waiterProperty() {return this.waiter;}
    public DoubleProperty tableIdProperty() {return  this.tableId;}
    public StringProperty itemProperty() {return  this.item;}
    public ObjectProperty<LocalDate> dateProperty() {return this.date;}

    // setters


}
