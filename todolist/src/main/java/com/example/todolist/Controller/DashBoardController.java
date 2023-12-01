package com.example.todolist.Controller;

import com.example.todolist.HelloApplication;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    public JFXListView<Task> Tasks_list;

    @FXML
    VBox mini_tasks_pane;

    TasksList tasksListModel = new TasksList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("hello from init ");
        //tasksListModel.loadfromDB();
        Tasks_list.setItems(tasksListModel.getList());
    }
    public void onAddTaskButton(ActionEvent event) throws IOException {

        FXMLLoader addtaskloader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/AddTask2.fxml"));

        Parent root = addtaskloader.load();

        AddTaskController addTaskController = addtaskloader.getController() ;

        //addTaskController.setVbox(mini_tasks_pane);
        addTaskController.setTask_list_local(Tasks_list);

        Scene scene = new Scene(root);

        Stage stage = new Stage() ;

        stage.setResizable(false);

        stage.setScene(scene);

        stage.show();



    }



}