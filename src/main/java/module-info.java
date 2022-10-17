module com.example.swiftide {
    requires richtextfx.fat;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;


    opens com.example.swiftide to javafx.fxml;
    exports com.example.swiftide;
}