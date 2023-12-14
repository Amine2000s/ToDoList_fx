package com.example.todolist.Controller;

import com.example.todolist.DAO.TaskDaoImp;
import com.example.todolist.HelloApplication;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {



    @FXML
    private VBox NotificationBox;
    @FXML
    private JFXButton removeNotification;
    @FXML
    private TableView<Task> Tasks_Tableview;
    @FXML
    private TextField searchbar;

    @FXML
    private JFXComboBox<String> categoryCombobox ;
    @FXML
    private JFXComboBox<String> priorityCombobox ;
    @FXML
    private JFXComboBox<String> statusCombobox ;

    @FXML
    private TableColumn<Task, Boolean> isDone_Column;
    @FXML
    private TableColumn<Task, String> Name;
    @FXML
    private TableColumn<Task, String> Priority;
    @FXML
    private TableColumn<Task, String> Category;
    @FXML
    private TableColumn<Task, Date> Deadline;
    @FXML
    private TableColumn<Task, String> Edit_Column;


    // returns and obersrvable list for task object by using its method getList()
    TasksList tasks_list_model = new TasksList();


    Task task; // used for retrieving info and gputting them into object task from add or for the info panel

    TaskDaoImp taskDAO = new TaskDaoImp();//Data access Object for CRUD operations


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("hello from init ");
        load_data();
        //setting the color of the priority column
        Priority.setCellFactory(column -> {
            return new TableCell<Task, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty) {
                        if(item.equals("High")) {
                            setTextFill(Color.rgb(226, 29, 18, 1));
                        } else if(item.equals("Medium")) {
                            setTextFill(Color.rgb(247, 178, 0, 1));
                        } else if(item.equals("Low")) {
                            setTextFill(Color.rgb(21, 132, 67, 1));
                        }
                        setText(item);
                    }
                }
            };
        });

        checkDeadLine();
        categoryCombobox.setItems(FXCollections.observableArrayList("All","General","Study","Sport"));
        priorityCombobox.setItems(FXCollections.observableArrayList("All","High","Medium","Low","Descending","Ascending"));
        statusCombobox.setItems(FXCollections.observableArrayList("All","Done","Undone"));


        categoryCombobox.setOnAction(event -> {

            String selction = categoryCombobox.getSelectionModel().getSelectedItem();
            if(selction=="All"){
                    load_data();
            }else{
                List<Task> new_list = taskDAO.Update_TableViewByCategory(selction);
                //tasks_list_model.getList().clear();
                //tasks_list_model.getList().addAll(new_list);
                ObservableList<Task> newlist = FXCollections.observableList(new_list);
                Tasks_Tableview.setItems(newlist);
            }

        });

        priorityCombobox.setOnAction(event -> {
            String selction = categoryCombobox.getSelectionModel().getSelectedItem();
            //if(selction=="All"){
             //   load_data();
            //}else {
                List<Task> new_list = taskDAO.Update_TableViewByCategory(selction);
                String selectedpriority = priorityCombobox.getSelectionModel().getSelectedItem();

                if(selectedpriority=="Descending"){
                    //execute dessending sort
                }else if (selectedpriority=="Ascending"){
                        //executing assencding sort
                }else{
                    System.out.println("selected priorirty is "+selectedpriority);

                    List<Task> filtered_list = new ArrayList<>();
                    for(Task t : new_list){

                        if(t.getPeriority()!=selectedpriority)
                        {new_list.remove(t);}
                    }

                    ObservableList<Task> newlist = FXCollections.observableList(new_list);
                    Tasks_Tableview.setItems(newlist);
                }
            //}


        });

        class EnlargeHandler implements EventHandler<ActionEvent> {
            public void handle(ActionEvent e) {

            }
        }
    }


    public void onAddTaskButton() throws IOException {
        //load fxml file
        FXMLLoader addtaskloader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/Add_Task.fxml"));

        Parent root = addtaskloader.load();

        AddTaskController addTaskController = addtaskloader.getController();

        addTaskController.setTableView__local(tasks_list_model);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);


        Stage stage = new Stage();

        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);


        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.show();


    }

    /*loads data from DB , get the Observable List and what happened in the Task_list model by
        * clearing the Observable List
        * then retrieving all the tasks from database (using DAO object)
        * linking them with the Observabale list
        * used by reload button and when delete , and updating tasks very crucial function

     */
    public void load_data() {

        /*
        Linking table view with the tasks list model(the obervable list )
         */


        Tasks_Tableview.setItems(tasks_list_model.getList());

        //Linking every Column with its propery value from the Task Class ,

        /////////////////////////////////////////////////////////////////////////////////////
        //isDone_Column.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("status"));
        //isDone_Column.setCellValueFactory(cellData ->cellData.getValue().isDone(););
        /**coonitnute tommorow here chof kifah trj3 el boolean bollean property ou kamel akhdm 3la check box */

        Name.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        Priority.setCellValueFactory(new PropertyValueFactory<Task, String>("periority"));
        Category.setCellValueFactory(new PropertyValueFactory<Task, String>("category"));
        Deadline.setCellValueFactory(new PropertyValueFactory<Task, Date>("deadline"));
        /////////////////////////////////////////////////////////////////////////////////////

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory = (TableColumn<Task, String> param) -> {
            // make cell containing buttons

            final TableCell<Task, String> cell = new TableCell<Task, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        //there icons that are on the edit column
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
                        deleteIcon.getStyleClass().add("delet-Icon");
                        infoIcon.getStyleClass().add("info-Icon");
                        editIcon.getStyleClass().add("edit-Icon");


                        //when delete icon is click
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object
                            taskDAO.DeleteTask(task);//query execution
                            load_data();//updating the table view
                        });

                        //when edit icon is clicked
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/com/example/todolist/View/Dashboard/Add_Task.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object
                            AddTaskController addTaskController = loader.getController();//loading the Controller
                            addTaskController.setAddTaskBtn_name("Confirm");//editing on the add task btn to Confrim

                            addTaskController.setTableView__local(tasks_list_model);//linking the dashboard table view
                            addTaskController.setTask_id(task.getId());//getting the task id
                            addTaskController.filltextfields(task);//filling the textfields by old data
                            //need to fix the date bug
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.TRANSPARENT);


                            stage.show();

                            load_data();//updating
                        });

                        //clicking on the info button
                        infoIcon.setOnMouseClicked((MouseEvent event)->{

                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();
                            URL fxmlLocation = getClass().getResource("/com/example/todolist/View/Dashboard/Task_info.fxml");
                            FXMLLoader loader = new FXMLLoader (fxmlLocation);
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            TaskInfoController taskInfoController = loader.getController();
                            taskInfoController.filllabelfield(task);
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();

                        });
                        //the buttons alignment
                        HBox managebtn = new HBox(infoIcon,editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 2));
                        HBox.setMargin(editIcon, new Insets(2, 2, 0, 2));
                        HBox.setMargin(infoIcon, new Insets(2, 2, 0, 2));

                        setGraphic(managebtn);

                        setText(null);
                    }
                }

            };
            return cell;
        };
        Edit_Column.setCellFactory(cellFactory);//linking the cell with the edit column
        //Tasks_Tableview.setItems(tasks_list_model.getList());
        //////checkbox functionality //
   /*     Callback<TableColumn<Task, String>, TableCell<Task, String>> cellcheckbox = (TableColumn<Task, String> param) -> {
            // make cell containing buttons

            final TableCell<Task, String> cell2 = new TableCell<Task, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        JFXCheckBox checkbox = new JFXCheckBox() ;

                        checkbox.setOnMouseClicked((MouseEvent event) ->{
                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object

                            if(checkbox.isSelected()){
                                //query to updates to true ;
                                TaskDAO.Update_Task_status(task.getId(),true);
                            }else{
                                //query to update it to false ;
                                TaskDAO.Update_Task_status(task.getId(),false);

                            }
                            load_data();
                        });
                        setGraphic(checkbox);

                        setText(null);
                    }
                }

            };
            return cell2 ;
        };
        isDone_Column.setCellFactory(cellcheckbox);//linking the cell with the edit column
        ///////////////////////////////checkbox*/
        /*
        * isDoneColumn.setCellValueFactory(cellData -> cellData.getValue().isDoneProperty());

        // Set the cell factory to use a CheckBox for the isDoneColumn
        isDoneColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Task, Boolean> call(TableColumn<Task, Boolean> param) {
                return new TableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    {
                        // Handle checkbox action
                        checkBox.setOnAction(event -> {
                            Task task = getTableView().getItems().get(getIndex());
                            task.setDone(checkBox.isSelected()); // Update the Task's property

                            // Update the database here (execute an update query)
                            // You'll need to implement database update logic

                            // Refresh the TableView to reflect changes
                            getTableView().refresh();
                        });
                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            checkBox.setSelected(item); // Set checkbox value based on Task's property
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });

        // Add other columns to the TableView

        tableView.getColumns().addAll(isDoneColumn);

        *
        *
        * */


        isDone_Column.setCellValueFactory(cellData -> {
            boolean isDone = cellData.getValue().isDone();
            return new javafx.beans.property.SimpleBooleanProperty(isDone);
        });

        isDone_Column.setCellFactory(column -> new TableCell<>() {
            private final JFXCheckBox checkBox = new JFXCheckBox();

            {
                checkBox.getStyleClass().add("checkbox");
                checkBox.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    task.setDone(checkBox.isSelected());
                    taskDAO.Update_Task_status(task.getId(),checkBox.isSelected());

                    //System.out.println("Task: " + task.getName() + ", Is Done: " + task.isDone());
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                }
            }
        });

        //////////////////////////////
        FilteredList<Task> filtredTasks = new FilteredList<>(tasks_list_model.getList(), b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {

            filtredTasks.setPredicate(task -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null || newValue =="Search..."){
                    return  true;
                }

                String tasksearchkeyword = newValue.toLowerCase();

                if(task.getName().toLowerCase().indexOf(tasksearchkeyword) >-1){

                    return true;
                }else{
                    return false ;
                }

            });

        });

        SortedList<Task> sortedTasks = new SortedList<>(filtredTasks);

        sortedTasks.comparatorProperty().bind(Tasks_Tableview.comparatorProperty());

        Tasks_Tableview.setItems(sortedTasks);

        ////// checkbox functoinality




        ////////




    }

    public void checkDeadLine(){
        LocalDate currentDate = LocalDate.now();
        for(Task task : Tasks_Tableview.getItems()){
            java.sql.Date sqlDate = (java.sql.Date) task.getDeadline();
            java.util.Date deadline = new java.util.Date(sqlDate.getTime());
            LocalDate localDeadLine = deadline.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(localDeadLine.minusDays(1).isEqual(currentDate)){
                Label label = new Label(task.getName()+"Dead Line is Tommorow");
                label.getStyleClass().add("NotificationBoxlabel");
                label.setWrapText(true);
                NotificationBox.getChildren().add(label);

                label.toBack();
            }
            else if(localDeadLine.isEqual(currentDate)) {
                Label label = new Label(task.getName() + "Dead Line is today");
                label.getStyleClass().add("NotificationBoxlabel");
                label.setWrapText(true);
                NotificationBox.getChildren().add(label);
                label.toBack();
            }
        }
    }
    @FXML
    private void removeNotification(){
        if(!NotificationBox.getChildren().isEmpty()){
            removeNotification.setVisible(false);
        }
        NotificationBox.getChildren().clear();
    }
}