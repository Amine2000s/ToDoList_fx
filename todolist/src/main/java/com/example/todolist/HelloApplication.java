package com.example.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/Dashboard3.fxml"));
       try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("View/Dashboard/Dashboard.css")).toExternalForm());

            stage.setTitle("Le Task Manager");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e ){
           e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch();
    }
}