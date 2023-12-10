package com.example.todolist.Controller;

import com.example.todolist.Model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TaskInfoController {

    @FXML
    Label name_label;

    @FXML
    TextArea desc_label;

    @FXML
    Label priority_label;
    @FXML
    Label category_label;
    @FXML
    Label deadline_label;

    void filllabelfield(Task task){
        name_label.setText(task.getName());
        desc_label.setText(task.getDescrition());
        priority_label.setText(task.getPeriority());
        category_label.setText(task.getCategory());
        deadline_label.setText(task.getDeadline().toString());

    }
}
