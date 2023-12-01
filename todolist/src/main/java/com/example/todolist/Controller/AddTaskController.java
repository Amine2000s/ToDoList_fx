package com.example.todolist.Controller;

import com.example.todolist.DAO.TaskDaoImp;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import other.mini_task_pane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddTaskController {

    @FXML
    TextField taskename;
    @FXML
    TextField taskdescription;
    @FXML
    DatePicker date ;
    @FXML
    ToggleGroup Priority;

    VBox local_mini_task_pane ;

    @FXML
    JFXRadioButton radiobtn1 ;
    @FXML
    JFXRadioButton radiobtn2 ;
    @FXML
    JFXRadioButton radiobtn3 ;
    @FXML
    JFXSnackbar error_msg;

    JFXListView Task_list_local ;

    public void setTask_list_local(JFXListView task_list_local) {
        Task_list_local = task_list_local;
    }

    //this function check if all input field are filled , returning true or false reflecting the state
    public boolean checkinputvalid(){
        if(taskename.getText().isEmpty()){return false;}
        if(taskdescription.getText().isEmpty()){return false;}
        if(date.getValue()==null){return false;}
       if(!checktogglevalue(Priority)){return false;}

        return true;
    }

//function gets the content of the selected radio button
    private String getSelectedRadioButton(ToggleGroup Group) {
        for (Toggle node : Group.getToggles()) {
            JFXRadioButton radioButton = (JFXRadioButton) node;
            if (radioButton.isSelected()) {
                return radioButton.getText();
            }
        }
        return null;
    }
    //part of the check input valid its mission is check the toggles
    private boolean checktogglevalue(ToggleGroup Group) {
        for (Toggle node : Group.getToggles()) {
            JFXRadioButton radioButton = (JFXRadioButton) node;
            if (radioButton.isSelected()) {
                return true;
            }
        }
        return false;
    }
    public void add_task_to_view(Task task) throws IOException {

        //mini_task_pane mini_pane = new mini_task_pane(task);
        FXMLLoader mini_pane_loader = new FXMLLoader(this.getClass().getResource("mini_task_pane.fxml"));
        Parent node = mini_pane_loader.load();
        mini_task_paneController  miniTaskPaneController = mini_pane_loader.getController();

        //miniTaskPaneController.setLocal(node);

        miniTaskPaneController.setTask_name_label(task.getName());
        miniTaskPaneController.setPriority_label(task.getPeriority());

        local_mini_task_pane.getChildren().add(mini_pane_loader.load());


       // local_mini_task_pane.getChildren().add(mini_pane);




    }
    @FXML
    public void onAddbtn(ActionEvent event) throws IOException {
      //  init_toggle_group();

        if(checkinputvalid()){

            String task_name = taskename.getText();
            String task_desc = taskdescription.getText();

            String priorty_Value = getSelectedRadioButton(Priority);

            LocalDate date1 = date.getValue();

            java.sql.Date date_Value = java.sql.Date.valueOf(date1);

            Task task = new Task(task_name,date_Value,priorty_Value,task_desc,"all",false);

            TaskDaoImp Task_Dao = new TaskDaoImp();

            Task_Dao.CreateTask(task);
           // add_task_to_view(task);
            Task_list_local.getItems().add(task);
            //addtask_toview() or //addtask_topane(task task )
            //add_task_to_view(task);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();

        }else{



            Pane pane = (Pane)((Node)event.getSource()).getParent() ;

            error_msg = new JFXSnackbar(pane);
            error_msg.setPrefWidth(400);
            //error_msg.getStylesheets().add(this.getClass().getResource("View/Dashboard/Task.css").toExternalForm());
            error_msg.fireEvent(new SnackbarEvent(new JFXSnackbarLayout("Please Fill Input Fields !")));
        }



    }

    @FXML
    public void onCancelbtn(ActionEvent event){

        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

        stage.close();

    }

    public void setVbox(VBox vbox){

        local_mini_task_pane = vbox ;

    }
}
