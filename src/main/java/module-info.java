module com.example.projetihmjeu {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projetihmjeu to javafx.fxml;
    exports com.example.projetihmjeu;
    exports Model;
    opens Model to javafx.fxml;
}