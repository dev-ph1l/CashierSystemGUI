package com.example.cashiersystem.Model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty waiterId;
    private final IntegerProperty tableId;
    private final ObjectProperty<LocalDate> date;

    Order(int orderId, int waiterId, int tableId, LocalDate date) {
        this.orderId = new SimpleIntegerProperty(this, "OrderId", orderId);
        this.waiterId = new SimpleIntegerProperty(this, "WaiterId", waiterId);
        this.tableId = new SimpleIntegerProperty(this, "TableId", tableId);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
    }

    // getters
    public IntegerProperty orderIdProperty() {
        return orderId;
    }
    public IntegerProperty waiterIdProperty() {
        return waiterId;
    }

    public IntegerProperty tableIdProperty() {
        return tableId;
    }

    public ObjectProperty dateProperty() {
        return date;
    }
}
