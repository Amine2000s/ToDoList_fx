package com.example.todolist.Controller;

import com.example.todolist.DAO.TaskDaoImp;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AddTaskController implements Initializable {

    @FXML
    TextField taskename;
    @FXML
    TextField taskdescription;
    @FXML
    DatePicker date ;
    @FXML
    ToggleGroup Priority;
    @FXML
    ComboBox<String> categoryCombobox;
    @FXML
    JFXButton AddTaskBtn;
    @FXML
    JFXSnackbar error_msg;

    TasksList tasksList_local;

    TaskDaoImp Task_Dao = new TaskDaoImp();//Dao function for main manipulation(CRUD)



    int Task_id;// variable used te determine if we should update or create

    //linking the dashboard Tableview with a Local variable of same type for easy Access
    public void setTableView__local(TasksList task_list_local) {

        tasksList_local = task_list_local;

    }

    //this function check if all input field are filled , returning true or false reflecting the state
    public boolean checkinputvalid(){

        if(taskename.getText().isEmpty()){return false;}

        if(taskdescription.getText().isEmpty()){return false;}

        if(date.getValue()==null){return false;}

        if(!checktogglevalue(Priority)){return false;}

        if(categoryCombobox.getValue().isEmpty()||categoryCombobox.getValue().isBlank()||categoryCombobox.getValue()==null){return false ;}

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
            String category = categoryCombobox.getValue();
            LocalDate date1 = date.getValue();

            java.sql.Date date_Value = java.sql.Date.valueOf(date1);//transforming date type to SQL

            // if task is already created
            if(Task_id >0) {

                task = new Task(Task_id,task_name, date_Value, priorty_Value, task_desc, category, false);

            }else{//new Task

                task = new Task(task_name, date_Value, priorty_Value, task_desc, category, false);

               // TableView_local.getItems().add(task);


            }

            Task_id = 0; // Resetting the id for future Use

            Task_Dao.CreateTask(task); // task Query exeuction
            tasksList_local.loadfromDB();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();

        }else{
            // field not filled

            Pane pane = (Pane)((Node)event.getSource()).getParent() ;

            error_msg = new JFXSnackbar(pane);
            error_msg.setPrefWidth(400);
            error_msg.fireEvent(new SnackbarEvent(new JFXSnackbarLayout("Please fill input fields")));

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

    //function for setting the Task id , Important for Updating Tasks
    public void setTask_id(int task_id) {

        this.Task_id = task_id;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
            categoryCombobox.setItems(FXCollections.observableArrayList("General","Study","Sport"));
    }
}
