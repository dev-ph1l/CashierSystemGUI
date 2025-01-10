package com.example.cashiersystem.Controllers;

import com.example.cashiersystem.Model.Model;
import com.example.cashiersystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public LoginController() {}

    @FXML
    public ChoiceBox<AccountType> account_selector;
    public TextField waiter_name_field;
    public TextField waiter_password_field;
    public Button login_btn;
    public Label waiter_name_lbl;
    public Label password_lbl;
    public Label error_lbl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        account_selector.setItems(FXCollections.observableArrayList(AccountType.WAITER, AccountType.ADMIN));
        account_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        account_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(account_selector.getValue()));
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();         //workaround
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.WAITER) {
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(waiter_name_field.getText(), waiter_password_field.getText()); // maybe check if it is empty
            if (Model.getInstance().getWaiterLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showWaiterWindow();
                // Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                waiter_name_field.setText("");
                waiter_password_field.setText("");
                System.out.println("ERROR");        // use error on screen
            }
        } else {
            Model.getInstance().getViewFactory().showAdminWindow();
        }

    }
}
