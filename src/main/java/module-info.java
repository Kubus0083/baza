module com.example.baza {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.baza to javafx.fxml;
    exports com.example.baza;
}