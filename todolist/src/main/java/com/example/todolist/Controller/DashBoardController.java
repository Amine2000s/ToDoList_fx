package com.example.todolist.Controller;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.DAO.TaskDaoImp;
import com.example.todolist.HelloApplication;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TasksList;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DashBoardController implements Initializable {




    @FXML
    private TableView<Task> Tasks_Tableview;

    @FXML
    private TableColumn<Task, Boolean> isDone_Column;
    @FXML
    private TableColumn<Task, String> name;
    @FXML
    private TableColumn<Task, String> periority;
    @FXML
    private TableColumn<Task, String> category;
    @FXML
    private TableColumn<Task, Date> deadline;
    @FXML
    private TableColumn<Task, String> edit_Column;


    // returns and obersrvable list for task obejcts by using its method getList()
    TasksList tasks_list_model = new TasksList();

    String qeury ; // for qeury

    Task task; // used for retrieving info and gputting them into object task from add or for the info panel

    TaskDaoImp taskDao = new TaskDaoImp();//Data access Object for CRUD operations


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("hello from init ");
        load_data();

    }

    public void onAddTaskButton(ActionEvent event) throws IOException {

        FXMLLoader addtaskloader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/AddTask2.fxml"));

        Parent root = addtaskloader.load();

        AddTaskController addTaskController = addtaskloader.getController();

        addTaskController.setTableView__local(Tasks_Tableview);

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setResizable(false);

        stage.setScene(scene);

        stage.show();


    }

    /*loads data from DB , get the Observable List and the happnes in the Tasklist model by
        * clearing the Observable List
        * then retrieving all the tasks from database (using DAO object)
        * linking them with the Observabale list
        * used by reload buttong and when delete , and updating tasks very crucial function

     */
    public void load_data() {

        /*
        Linking table view with the tasks list model(the obervable list )
         */


        Tasks_Tableview.setItems(tasks_list_model.getList());
        //Linking every Column with its propery value from the Task Class ,

        /////////////////////////////////////////////////////////////////////////////////////
        isDone_Column.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("done"));
        name.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        periority.setCellValueFactory(new PropertyValueFactory<Task, String>("periority"));
        category.setCellValueFactory(new PropertyValueFactory<Task, String>("category"));
        deadline.setCellValueFactory(new PropertyValueFactory<Task, Date>("deadline"));
        /////////////////////////////////////////////////////////////////////////////////////

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFoctory = (TableColumn<Task, String> param) -> {
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

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        infoIcon.setStyle(
                                "-fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );

                        //when delete icno is click
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object
                            taskDao.DeleteTask(task);//query excutiong
                            load_data();//updating the table view
                        });

                        //when edit icon is clicked
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/com/example/todolist/View/Dashboard/addTask2.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            task = Tasks_Tableview.getSelectionModel().getSelectedItem();//getting the selected Object
                            AddTaskController addTaskController = loader.getController();//loading the Controller
                            addTaskController.setAddTaskBtn_name("Confirm");//editing on the add task btn to Confrim

                            addTaskController.setTableView__local(Tasks_Tableview);//linking the dashboard table view
                            addTaskController.setTaskid(task.getId());//getting the task id
                            addTaskController.filltextfields(task);//filling the textfields by old data
                            //need to fix the date bug
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
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
        edit_Column.setCellFactory(cellFoctory);//linking the cell with the edit column
        //Tasks_Tableview.setItems(tasks_list_model.getList());
    }
}