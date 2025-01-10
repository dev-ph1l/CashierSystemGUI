package com.example.cashiersystem.Model;

import com.example.cashiersystem.Views.AccountType;
import com.example.cashiersystem.Views.ViewFactory;

import java.sql.ResultSet;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.WAITER;

    // Waiter Data Section
    private final Waiter waiter;
    private boolean waiterLoginSuccessFlag;


    // Admin Data Section

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();

        // Client Data Section

        this.waiterLoginSuccessFlag = false;
        this.waiter = new Waiter("", "");

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

    public void evaluateClientCred(String name, String password) {
        ResultSet rs = databaseDriver.getWaiterData(name, password);
        try {
            if (rs.isBeforeFirst() && rs.next()){
                this.waiter.nameProperty().set(rs.getString("username"));
                this.waiter.passwordProperty().set(rs.getString("password"));

                this.waiterLoginSuccessFlag = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Admin Method Section
     * */
}
