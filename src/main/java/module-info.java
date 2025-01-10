module com.example.cashiersystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cashiersystem to javafx.fxml;
    exports com.example.cashiersystem;
    exports com.example.cashiersystem.Controllers;
    exports com.example.cashiersystem.Controllers.Waiter to javafx.fxml;
    exports com.example.cashiersystem.Controllers.Admin to javafx.fxml;
    opens com.example.cashiersystem.Controllers to javafx.fxml;
}