package com.example.swiftide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SwiftIDE extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // loads resources from swift-view.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("swift-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // loads css file for styling
        String css = Objects.requireNonNull(this.getClass().getResource("swift-style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("SwiftIDE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
