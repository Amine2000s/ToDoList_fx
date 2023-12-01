package other;

import com.example.todolist.Model.Task;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class mini_task_pane  {

    int id ;

    String task_name;

    String task_description;

    String priority ;

    FXMLLoader mini_task_loader ;

    Pane task_pane ;

    JFXCheckBox checkBox;

    JFoenixResources checkbox ;


    public mini_task_pane(Task task) throws IOException {

        task_name = task.getName();

        task_description = task.getDescrition();

        priority = task.getPeriority() ;


        task_pane = new Pane();
        task_pane.setPrefWidth(600);
        task_pane.setPrefHeight(87);
        //task_pane.getChildren().add(checkBox);

        //task_pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("View/Dashboard/Task.css")).toExternalForm());


    }

    public void alignchildren(){




    }



}
