package com.example.cashiersystem.Model;

import com.example.cashiersystem.Views.AccountType;
import com.example.cashiersystem.Views.ViewFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.WAITER;

    // Waiter Data Section
    private final Waiter waiter;
    private boolean waiterLoginSuccessFlag;
    private final Order order;
    private final Report report;


    // Admin Data Section

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();

        // Client Data Section

        this.waiterLoginSuccessFlag = false;
        this.waiter = new Waiter("", "", -1);
        this.order = new Order(-1, -1, -1, null, null, null);
        this.report = new Report(null,null, null, null , null, null, null);

        // Admin Data Section
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public AccountType getLoginAccountType() {return this.loginAccountType;}
    public void setLoginAccountType(AccountType loginAccountType) {this.loginAccountType = loginAccountType;}

    /*
    * Waiter Method Section
    * */
    public boolean getWaiterLoginSuccessFlag() {return this.waiterLoginSuccessFlag;}
    public void setWaiterLoginSuccessFlag(boolean flag) {this.waiterLoginSuccessFlag = flag;}

    public Waiter getWaiter() {
        return waiter;
    }

    public void evaluateClientCred(String username, String password) {
        ResultSet rs = databaseDriver.getWaiterData(username, password);
        try {
            if (rs.isBeforeFirst() && rs.next()){
                this.waiter.nameProperty().set(rs.getString("username"));
                this.waiter.passwordProperty().set(rs.getString("password"));
                this.waiter.waiterIdProperty().set(rs.getInt("id"));

                this.waiterLoginSuccessFlag = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Order getOrder() {
        return order;
    }

    public Report getReport(){
        return report;
    }
        /*
     * Admin Method Section
     * */
}
