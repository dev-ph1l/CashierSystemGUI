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

    // method to add an item
    public void addItemId(int itemId) {
        // Überprüfen, ob das Item bereits in der Bestellung ist
        if (itemCountMap.containsKey(itemId)) {
            int currentCount = itemCountMap.get(itemId);
            itemCountMap.put(itemId, currentCount + 1);  // Menge um 1 erhöhen
        } else {
            itemCountMap.put(itemId, 1);  // Neues Item mit einer Menge von 1 hinzufügen
            itemIds.add(itemId);  // Nur einmal zur Liste hinzufügen
        }
    }

    // Methode, um alle Items zu löschen
    public void clearItems() {
        this.itemIds.clear();
        this.itemCountMap.clear();  // Löscht auch das Mengenmap
    }

    // Methode, um die Mengen der Items zu erhalten
    public Map<Integer, Integer> getItemQuantities() {
        return itemCountMap;
    }
}
