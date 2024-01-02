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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.paint.Color;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

import java.net.URL;


public class DashBoardController implements Initializable {


    FileChooser fileChooser;
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
    @FXML
    private Label CurrentCategoryLabel;

    @FXML
    private JFXButton Statics_button ;


    @FXML
    StackPane main_board ;

    /**returns and obersrvable list for task object by using its method getList()*/
    TasksList tasks_list_model = new TasksList();


    Task task; /** used for retrieving info and gputting them into object task from add or for the info panel*/

    TaskDaoImp taskDAO = new TaskDaoImp();/**Data access Object for CRUD operations**/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //System.out.println("hello from init ");

         fileChooser = new FileChooser() ;
         fileChooser.setInitialDirectory(new File("C:\\Users\\amin\\Documents\\ToDoList_fx\\inputfiles"));
        load_data();
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("View/Dashboard/Dashboardactl.fxml"));

        //try {
          //  main_board.getChildren().add((Node) fxmlLoader.load());
            //System.out.println("added with succes");
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/

        //setting the color of the priority column
        Priority.setCellFactory(column -> {
            return new TableCell<Task, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        switch (item) {
                            case "High" -> setTextFill(Color.rgb(226, 29, 18, 1));
                            case "Medium" -> setTextFill(Color.rgb(247, 178, 0, 1));
                            case "Low" -> setTextFill(Color.rgb(21, 132, 67, 1));
                        }
                        setText(item);
                    }
                }
            };
        });

        checkDeadLine();
        categoryCombobox.setItems(FXCollections.observableArrayList("All", "General", "Study", "Sport"));
        priorityCombobox.setItems(FXCollections.observableArrayList("All", "High", "Medium", "Low", "Descending", "Ascending"));
        statusCombobox.setItems(FXCollections.observableArrayList("All", "Done", "Undone"));

        initFilterListeners();
    }
        /*class EnlargeHandler implements EventHandler<ActionEvent> {
            public void handle(ActionEvent e) {

            }
        }
    }*/
    /**
     * initialise Listeners for Categrory,priority,status Combox boxes
     * */
    public void initFilterListeners(){

            categoryCombobox.setOnAction(event -> {

                String selction = categoryCombobox.getSelectionModel().getSelectedItem();
                if (selction.equals("All")) {

                    load_data();

                } else {
                    List<Task> new_list = taskDAO.Update_TableViewByCategory(selction);
                    //tasks_list_model.getList().clear();
                    //tasks_list_model.getList().addAll(new_list);
                    ObservableList<Task> newlist = FXCollections.observableList(new_list);
                    Tasks_Tableview.setItems(newlist);
                }

            });

        //    initFilterListeners();



       // filtered_observable_list.clear();

        priorityCombobox.setOnAction(event -> {

            List<Task> filtred_list = new ArrayList<>();

            ObservableList<Task> filtered_observable_list = FXCollections.observableList(filtred_list);

            String selction = categoryCombobox.getSelectionModel().getSelectedItem();

            String selectedpriority = priorityCombobox.getSelectionModel().getSelectedItem();

            if(selectedpriority=="Descending"){

                //execute dessending sort
                dessending_sort(filtered_observable_list);
                System.out.println(filtered_observable_list.toString());

            }else if (selectedpriority=="Ascending"){

                //executing assencding sort
                assending_sort(filtered_observable_list);
                System.out.println(filtered_observable_list.toString());

            }else{

                //System.out.println("selected priorirty is "+selectedpriority);


                for (Task task : tasks_list_model.getList()){

                    if(task.getPeriority().equals(selectedpriority)&& !filtered_observable_list.contains(task)) {

                        filtered_observable_list.add(task);
                        //System.out.println("added with succes ");
                    }

                }

            }

            Tasks_Tableview.setItems(filtered_observable_list);




        });


        statusCombobox.setOnAction(event -> {

            List<Task> filtred_list = new ArrayList<>();

            ObservableList<Task> filtered_observable_list = FXCollections.observableList(filtred_list);

            //String selction = categoryCombobox.getSelectionModel().getSelectedItem();

            String selectedstatus = statusCombobox.getSelectionModel().getSelectedItem();

            if(selectedstatus.equals("Undone")){

                for (Task task : tasks_list_model.getList()){

                    if(!task.isDone() &&!filtered_observable_list.contains(task)) {

                        filtered_observable_list.add(task);
                        //System.out.println("added with succes ");
                    }

                }

            }else if (selectedstatus.equals("Done")){

                //executing assencding sort

                for (Task task : tasks_list_model.getList()){

                    if(task.isDone() && !filtered_observable_list.contains(task)) {

                        filtered_observable_list.add(task);
                        //System.out.println("added with succes ");
                    }

                }
            }else{

                //System.out.println("selected priorirty is "+selectedpriority);

                load_data();



            }

            Tasks_Tableview.setItems(filtered_observable_list);

        });





    }

    public void assending_sort(ObservableList<Task> list){

        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("Low")){
                list.add(task);
            }

        }

        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("Medium")){
                list.add(task);
            }

        }

        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("High")){
                list.add(task);
            }

        }
    }/***/

    public void dessending_sort(ObservableList<Task> list){
        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("High")){
                list.add(task);
            }

        }
        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("Medium")){
                list.add(task);
            }

        }

        for(Task task : tasks_list_model.getList()){
            if(task.getPeriority().equals("Low")){
                list.add(task);
            }

        }
    }/***/

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


    }/***/

    /**loads data from DB , get the Observable List and what happened in the Task_list model by
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


        initFilterListeners();

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
                //SendMail(task.getName()+" Deadline is Tommorow");
                label.toBack();
            }
            else if(localDeadLine.isEqual(currentDate)) {
                Label label = new Label(task.getName() + "Deadline is today");
                label.getStyleClass().add("NotificationBoxlabel");
                label.setWrapText(true);
                NotificationBox.getChildren().add(label);
                //SendMail(task.getName() + " Deadline is today");
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
//    public void SendMail(String notification) {
//        // Sender's credentials
//        final String username = "edd07bf091e201";
//        final String password = "de739a16c27665";
//
//        // Recipient's email address
//        String toEmail = "abdoukermiche123@gmail.com";
//
//        // Email properties
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
//        props.put("mail.smtp.port", "25");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//
//        // Session to get debug information
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            // Create a MimeMessage object
//            Message message = new MimeMessage(session);
//
//            // Set the sender and recipient addresses
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//
//            // Set the email subject and content
//            message.setSubject(" Deadline is close");
//            message.setText(notification);
//
//            // Send the email
//            Transport.send(message);
//
//            System.out.println("Email sent successfully.");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.err.println("Error sending email: " + e.getMessage());
//        }
//    }


    public void OnImportButton(){

        File file = fileChooser.showOpenDialog(new Stage());

        BufferedReader reader = null ;
        String line ="";
        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null ){

                String [] row = line.split(",");
                Date temp = new SimpleDateFormat("yyyy-mm-dd").parse(row[1]);
                Boolean temp_boolean = Boolean.valueOf(row[4]);
                Task task_input = new Task(row[0],temp,row[2],row[3],row[4],temp_boolean);
                taskDAO.CreateTask(task_input);
                System.out.println("done with succes ");
                System.out.println(task_input.toString());


            }


        }catch (Exception e ){
            e.printStackTrace();

        }finally {

        }



    }

    private void writeToCSV(ObservableList<Task> rowsData) {
        File csvFileName = fileChooser.showSaveDialog(new Stage());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            // Writing row data to the CSV file
            for (Task rowData : rowsData) {
                String[] task_info = new String[5];
                task_info[0]= rowData.name;
                task_info[1]= rowData.deadline.toString();
                task_info[2]= rowData.category;
                task_info[3]= rowData.description;
                task_info[4]= String.valueOf(rowData.done);
                for(int i=0;i<5;i++){

                    writer.write(task_info[i]);
                    if(i!=4)writer.write(",");
                }
                writer.newLine(); // Move to the next line for the next row
            }
        } catch (IOException e) {
            // Handle file writing errors
            e.printStackTrace();
        }
    }

    public void OnExportButton() {


        ObservableList<Task> selectedRows = Tasks_Tableview.getSelectionModel().getSelectedItems();

        if (!selectedRows.isEmpty()) {
            writeToCSV(selectedRows);
        } else {
            // Handle case where no rows are selected
            System.out.println("Please select rows to export.");
        }

    }

    public void HandleStaticsButton(ActionEvent event) throws IOException {

        FXMLLoader staticsloader = new FXMLLoader(getClass().getResource("View/Dashboard/stats.fxml"));

        //Scene scene = new Scene(staticsloader.load());

        Stage stage = new Stage(staticsloader.load());

        stage.show();

    }
}