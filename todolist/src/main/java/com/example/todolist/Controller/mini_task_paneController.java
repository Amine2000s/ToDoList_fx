package com.example.todolist.Controller;

import com.example.todolist.Model.Task;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class mini_task_paneController {

    @FXML
    JFXCheckBox checkbox;

    @FXML
    Label task_name_label;

     @FXML
    Label priority_label;


    Parent local ;



    public void setTask_name_label(String task_name_label) {
        this.task_name_label.setText(task_name_label);
    }



    public void setPriority_label(String priority_label) {
        this.priority_label.setText( priority_label);
    }

    public void init(Task task){

    }

    public void setLocal(Parent node ){
        this.local = node;
    }
}
