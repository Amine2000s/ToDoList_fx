package com.example.todolist.Controller;

import com.example.todolist.DAO.TaskDaoImp;
import com.example.todolist.HelloApplication;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {




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


    // returns and obersrvable list for task obejcts by using its method getList()
    TasksList tasks_list_model = new TasksList();


    Task task; // used for retrieving info and gputting them into object task from add or for the info panel

    TaskDaoImp TaskDAO = new TaskDaoImp();//Data access Object for CRUD operations


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
                        if(item.equals("Hight")) {
                            setTextFill(Color.RED);
                        } else if(item.equals("Medium")) {
                            setTextFill(Color.ORANGE);
                        } else if(item.equals("Low")) {
                            setTextFill(Color.GREEN);
                        }
                        setText(item);
                    }
                }
            };
        });
        categoryCombobox.setItems(FXCollections.observableArrayList("All","General","Study","Sport"));
        priorityCombobox.setItems(FXCollections.observableArrayList("All","High","Medium","Low"));
        statusCombobox.setItems(FXCollections.observableArrayList("All","Done","Undone"));


    }


    public void onAddTaskButton() throws IOException {
        //load fxml file
        FXMLLoader addtaskloader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/Add_Task.fxml"));

        Parent root = addtaskloader.load();

        AddTaskController addTaskController = addtaskloader.getController();

        addTaskController.setTableView__local(Tasks_Tableview);

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
                        //thre icons that are on the edit column
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
                        deleteIcon.getStyleClass().add("delet-Icon");
                        infoIcon.getStyleClass().add("info-Icon");
                        editIcon.getStyleClass().add("edit-Icon");


                        //when delete icno is click
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object
                            TaskDAO.DeleteTask(task);//query execution
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

                            addTaskController.setTableView__local(Tasks_Tableview);//linking the dashboard table view
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

                        //clicking on the info buttong
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
                        //the buttongs alignment
                        HBox managebtn = new HBox(infoIcon,editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        HBox.setMargin(infoIcon, new Insets(2, 3, 0, 2));

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


*/
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


}