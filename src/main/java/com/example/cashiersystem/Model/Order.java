package com.example.cashiersystem.Model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty waiterId;
    private final IntegerProperty tableId;
    private final ObjectProperty<LocalDate> date;
    private final ListProperty<Integer> itemIds;
    private final Map<Integer, Integer> itemCountMap;

    Order(int orderId, int waiterId, int tableId, LocalDate date, ListProperty<Integer> ItemIds,Map<Integer, Integer> itemCountMap) {
        this.orderId = new SimpleIntegerProperty(this, "OrderId", orderId);
        this.waiterId = new SimpleIntegerProperty(this, "WaiterId", waiterId);
        this.tableId = new SimpleIntegerProperty(this, "TableId", tableId);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.itemIds = new SimpleListProperty<>(this, "ItemIds", FXCollections.observableArrayList());
        this.itemCountMap = new HashMap<>();
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

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public ListProperty<Integer> itemIdsProperty() {
        return itemIds;
    }

    // order items methods
    public void addItemId(int itemId) {
        if (itemCountMap.containsKey(itemId)) {
            int currentCount = itemCountMap.get(itemId);
            itemCountMap.put(itemId, currentCount + 1);
        } else {
            itemCountMap.put(itemId, 1);
            itemIds.add(itemId);
        }
    }

    public void clearItems() {
        this.itemIds.clear();
        this.itemCountMap.clear();  // Löscht auch das Mengenmap
    }

    public Map<Integer, Integer> getItemQuantities() {
        return itemCountMap;
    }
}
