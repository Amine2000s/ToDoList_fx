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
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;


import java.net.URL;
import java.util.List;


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
    private JFXButton Dashboard_button;

    @FXML
    private Rectangle staticsRec;
    @FXML
    private Rectangle dashboardRec;
    @FXML
    private AnchorPane Statics_Anchor;
    @FXML
    private JFXButton Contact_Button;

    @FXML
    StackedBarChart<String,Integer> Categorystackbar ;
    @FXML
    PieChart Priority_Pie ;
    @FXML
    PieChart Task_Pie ;

    @FXML
    JFXButton reloadStats ;

    /**returns and observable list for task object by using its method getList()*/
    TasksList tasks_list_model = new TasksList();


    Task task; /** used for retrieving info and putting them into object task from add or for the info panel*/

    TaskDaoImp taskDAO = new TaskDaoImp();/**Data access Object for CRUD operations**/

    /**  staticstics Variables */

    int TotalNumberOftasks =0 ;
    int NumberofDoneTasks =0 ;
    int NumberofUndoneTasks =0 ;
    int NumberofHighPriorityTasks =0 ;
    int NumberofMediuemPriorityTasks =0 ;
    int NumberofLowPriorityTasks =0 ;
    int NumberofGeneralTasks  =0 ;
    int NumberofStudyTasks  =0 ;
    int NumberofSportTasks =0 ;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //System.out.println("hello from init ");


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
        CurrentCategoryLabel.setText("All");
        staticsRec.setVisible(false);
        Statics_Anchor.setVisible(false);

        initFilterListeners();


        initialiseStatisticsCalculation();
        init_Categorystackbar();
        init_Priority_Pie();
        init_Task_Pie();
    }
    /**
     * initialise Listeners for Category,priority,status Combobox boxes,Category Label and Info Button
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

            String selection = categoryCombobox.getSelectionModel().getSelectedItem();

            String selectedpriority = priorityCombobox.getSelectionModel().getSelectedItem();

            if(Objects.equals(selectedpriority, "Descending")){

                //execute dessending sort
                dessending_sort(filtered_observable_list);
                System.out.println(filtered_observable_list.toString());

            }else if (Objects.equals(selectedpriority, "Ascending")){

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

        Contact_Button.setOnMouseClicked(event->{
            openWebpage("https://github.com/Amine2000s/ToDoList_fx");
        });

        categoryCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CurrentCategoryLabel.setText(newValue);
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

        //Linking every Column with its property value from the Task Class ,

        /////////////////////////////////////////////////////////////////////////////////////
        //isDone_Column.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("status"));
        //isDone_Column.setCellValueFactory(cellData ->cellData.getValue().isDone(););
        /**coonitnute tommorow here chof kifah trj3 el  boolean property ou kamel akhdm 3la check box */

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
                            addTaskController.setAddTaskBtn_name("Confirm");//editing on the add task btn to Confirm

                            addTaskController.setTableView__local(tasks_list_model);//linking the dashboard table view
                            addTaskController.setTask_id(task.getId());//getting the task id
                            addTaskController.filltextfields(task);//filling the text-fields by old data
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




        isDone_Column.setCellValueFactory(cellData -> {
            boolean isDone = cellData.getValue().isDone();
            return new SimpleBooleanProperty(isDone);
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
                checkBox.selectedProperty().addListener((obs,wasSelected,isSelected)->{
                    if (isSelected) {
                        getTableRow().setOpacity(0.2);
                    } else {
                        getTableRow().setOpacity(1);
                    }
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

        ////// checkbox functionality




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
                Label label = new Label(task.getName()+"Dead Line is Tomorrow");
                label.getStyleClass().add("NotificationBoxlabel");
                label.setWrapText(true);
                NotificationBox.getChildren().add(label);
                //SendMail(task.getName()+" Deadline is Tomorrow");
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
    public void SendMail(String notification) {
        // Sender's credentials
        final String username = "edd07bf091e201";
        final String password = "de739a16c27665";

        // Recipient's email address
        String toEmail = "abdoukermiche123@gmail.com";

        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        // Session to get debug information
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set the email subject and content
            message.setSubject(" Deadline is close");
            message.setText(notification);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }


    public void OnImportButton(){
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv file", "*.csv"));
        File file = fileChooser.showOpenDialog(new Stage());

        BufferedReader reader = null ;
        String line ="";
        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null ){

                String [] row = line.split(",");
                Date temp = new SimpleDateFormat("yyyy-MM-dd").parse(row[5]);
                Boolean temp_boolean = Boolean.valueOf(row[4]);
                Task task_input = new Task(row[0],temp,row[1],row[3],row[2],temp_boolean);
                taskDAO.CreateTask(task_input);
                System.out.println("done with success ");
                System.out.println(task_input.toString());


            }


        }catch (Exception e ){
            e.printStackTrace();

        }finally {

        }



    }

    private void writeToCSV(ObservableList<Task> rowsData) {

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("task export.csv");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv file", "*.csv"));
        File csvFileName = fileChooser.showSaveDialog(new Stage());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            // Writing row data to the CSV file
            for (Task rowData : rowsData) {
                String[] task_info = new String[6];
                task_info[0]= rowData.name;
                task_info[1]= rowData.periority;
                task_info[2]= rowData.category;
                task_info[3]= rowData.description;
                task_info[4]= String.valueOf(rowData.done);
                task_info[5]= rowData.deadline.toString();
                for(int i=0;i<6;i++){

                    writer.write(task_info[i]);
                    if(i!=5)writer.write(",");
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

    public void HandleStaticsButton() throws IOException {
       Statics_button.setOnAction(event ->{
            staticsRec.setVisible(true);
            dashboardRec.setVisible(false);
            Statics_Anchor.setVisible(true);
            UpdateStats();
       });
    }
    public void HandleBarChart_Category(){

    }
    public void HandleDashboardButton() throws IOException {
        Dashboard_button.setOnAction(event ->{
            dashboardRec.setVisible(true);
            staticsRec.setVisible(false);
            Statics_Anchor.setVisible(false);
        });
    }

    private void openWebpage(String url){
        try{
            if(Desktop.isDesktopSupported()){
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("url");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initialiseStatisticsCalculation(){TotalNumberOftasks =0 ;
         NumberofDoneTasks =0 ;
         NumberofUndoneTasks =0 ;
         NumberofHighPriorityTasks =0 ;
         NumberofMediuemPriorityTasks =0 ;
         NumberofLowPriorityTasks =0 ;
         NumberofGeneralTasks  =0 ;
         NumberofStudyTasks  =0 ;
         NumberofSportTasks =0 ;

         TotalNumberOftasks =tasks_list_model.getList().size();

         for(Task task : tasks_list_model.getList()){
             switch (task.periority){
                 case "High" : NumberofHighPriorityTasks++;break;
                 case "Medium" : NumberofMediuemPriorityTasks++;break;
                 case "Low" :    NumberofLowPriorityTasks++;break;
             }

             switch (task.getCategory()){
                 case "General" : NumberofGeneralTasks++;break;
                 case "Study" : NumberofStudyTasks++;break;
                 case "Sport" :    NumberofSportTasks++;break;
             }

             if (task.isDone()){
                 NumberofDoneTasks++;
             }else{
                 NumberofUndoneTasks++;
             }

         }

    }

    void init_Categorystackbar(){
        if(!Categorystackbar.getData().isEmpty()) {
            Categorystackbar.getData().clear();
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Task Category ");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tasks Number ");

        //Category_BarChart= new BarChart(xAxis,yAxis);
        System.out.println(NumberofGeneralTasks);
        System.out.println(NumberofStudyTasks);
        System.out.println(NumberofSportTasks);

        XYChart.Series<String,Integer> data = new XYChart.Series();
        data.setName("General");
        XYChart.Series<String,Integer> data2 = new XYChart.Series();
        data2.setName("Study");
        XYChart.Series<String,Integer> data3 = new XYChart.Series();
        data3.setName("Sport");
        data.getData().add(new XYChart.Data("General",NumberofGeneralTasks));
        data2.getData().add(new XYChart.Data("Study",NumberofStudyTasks));
        data3.getData().add(new XYChart.Data("Sport",NumberofSportTasks));


        Categorystackbar.getData().addAll(data,data2,data3);


    }
    void init_Priority_Pie(){
        if(!Priority_Pie.getData().isEmpty()) {
            Priority_Pie.getData().clear();
        }
        ObservableList<PieChart.Data> PriorityPieChartData = FXCollections.observableArrayList(
                new PieChart.Data("High",NumberofHighPriorityTasks),
                new PieChart.Data("Medium",NumberofMediuemPriorityTasks),
                new PieChart.Data("Low",NumberofLowPriorityTasks));

        PriorityPieChartData.forEach(data5 ->
                data5.nameProperty().bind(
                        Bindings.concat(
                                data5.getName()," ",data5.pieValueProperty()
                        )
                )
        );
        Priority_Pie.getData().addAll(PriorityPieChartData);
        PriorityPieChartData.get(0).getNode().setStyle("-fx-pie-color: #F53900");
        PriorityPieChartData.get(1).getNode().setStyle("-fx-pie-color: #F5DC00");
        PriorityPieChartData.get(2).getNode().setStyle("-fx-pie-color: #7dcc28");
//        Priority_Pie.setLegendVisible(false);

    }
    void init_Task_Pie(){
        if(!Task_Pie.getData().isEmpty()) {
            Task_Pie.getData().clear();
        }

        ObservableList<PieChart.Data> statusPieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Done",NumberofDoneTasks),
                new PieChart.Data("Undone",NumberofUndoneTasks));


        statusPieChartData.forEach(data6 ->
                data6.nameProperty().bind(
                        Bindings.concat(
                                data6.getName()," ",data6.pieValueProperty()
                        )
                )
        );

        Task_Pie.getData().addAll(statusPieChartData);
        statusPieChartData.get(0).getNode().setStyle("-fx-pie-color: #7dcc28");
        statusPieChartData.get(1).getNode().setStyle("-fx-pie-color: #F53900");
        Task_Pie.setLegendVisible(false);


    }
    @FXML
    void UpdateStats(){
        initialiseStatisticsCalculation();
        Categorystackbar.getData().get(0).getData().get(0).setYValue(NumberofGeneralTasks);
        Categorystackbar.getData().get(1).getData().get(0).setYValue(NumberofStudyTasks);
        Categorystackbar.getData().get(2).getData().get(0).setYValue(NumberofSportTasks);

        Priority_Pie.getData().get(0).setPieValue(NumberofHighPriorityTasks);
        Priority_Pie.getData().get(1).setPieValue(NumberofMediuemPriorityTasks);
        Priority_Pie.getData().get(2).setPieValue(NumberofLowPriorityTasks);

        Task_Pie.getData().get(0).setPieValue(NumberofDoneTasks);
        Task_Pie.getData().get(1).setPieValue(NumberofUndoneTasks);
        System.out.println("doneeeeee0");

    }

}