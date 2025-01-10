package com.example.cashiersystem.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Waiter {
    private final StringProperty name;  // add first name and last name
    private final StringProperty password;

    Waiter(String name, String password) {
        this.name = new SimpleStringProperty(this, "Name", name);
        this.password = new SimpleStringProperty(this, "Password", password);
    }

    // getters
    public StringProperty nameProperty() {return name;}
    public StringProperty passwordProperty() {return password;}
}
