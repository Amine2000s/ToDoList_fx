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
import java.time.LocalDateTime;
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


    @FXML
    JFXButton AddTaskBtn;
    @FXML
    JFXRadioButton radiobtn1 ;
    @FXML
    JFXRadioButton radiobtn2 ;
    @FXML
    JFXRadioButton radiobtn3 ;
    @FXML
    JFXSnackbar error_msg;

    TableView<Task> TableView_local ;

    TaskDaoImp Task_Dao = new TaskDaoImp();//Dao function for main manipulation(CRUD)



    int taskid ;// variable used te determine if we should update or delete

    //linking the dashboard Tableview with a Local variable of same type for easy Access
    public void setTableView__local(TableView<Task> task_list_local) {

        TableView_local = task_list_local;

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


    //part of the check input valid operation , its mission to check if there is a checkked radio box
    private boolean checktogglevalue(ToggleGroup Group) {
        for (Toggle node : Group.getToggles()) {
            JFXRadioButton radioButton = (JFXRadioButton) node;
            if (radioButton.isSelected()) {
                return true;
            }
        }
        return false;
    }


   //function to create || update tasks
    @FXML
    public void onAddbtn(ActionEvent event) throws IOException {

        Task task;

        if(checkinputvalid()){

            String task_name = taskename.getText();
            String task_desc = taskdescription.getText();
            String priorty_Value = getSelectedRadioButton(Priority);

            LocalDate date1 = date.getValue();

            java.sql.Date date_Value = java.sql.Date.valueOf(date1);//transforming date type to SQL

            // if task is already created
            if(taskid>0) {

                task = new Task(taskid,task_name, date_Value, priorty_Value, task_desc, "All", false);

            }else{//new Task

                task = new Task(task_name, date_Value, priorty_Value, task_desc, "all", false);
                TableView_local.getItems().add(task);

            }

            taskid = 0; // Resetting the id for future Use

            Task_Dao.CreateTask(task); // task Query exeuction

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();

        }else{
            // field not filled

            Pane pane = (Pane)((Node)event.getSource()).getParent() ;

            error_msg = new JFXSnackbar(pane);
            error_msg.setPrefWidth(400);
            error_msg.fireEvent(new SnackbarEvent(new JFXSnackbarLayout("Please Fill Input Fields !")));

        }

    }

    @FXML
    public void onCancelbtn(ActionEvent event){

        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();

        stage.close();

    }



    public void setAddTaskBtn_name(String name ){

        AddTaskBtn.setText(name);
    }

    public void filltextfields(Task task){

         taskename.setText(task.getName());

         taskdescription.setText(task.getDescrition());
         //Converting from util.date to local date
         //LocalDate localDate;
        //localDate = task.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //date.setValue(localDate);

         Priority.selectToggle(Priority.getSelectedToggle());


    }

    //function for ssetting the Task id , Important for Updating Tasks
    public void setTaskid(int taskid) {

        this.taskid = taskid;
    }

}
