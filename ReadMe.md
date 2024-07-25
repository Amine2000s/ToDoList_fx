# Description
 Taks Manager Application using Java and JavaFx with MYSQL database.


## Features

- Add tasks with a title, description, deadline , Priority and Task Category 
- Mark tasks as complete or incomplete.
- Delete tasks.
- Display tasks with title, description, and due date.
- User-friendly interface.
- Easy to use and manage tasks efficiently.
- Dashboard for statistics

## Setup

To run this application locally:

1. Ensure you have Java and JavaFX installed on your system.
   2. Clone this repository:

      ```bash
        https://github.com/Amine2000s/ToDoList_fx.git


1. Open the project in your preferred IDE (like IntelliJ, Eclipse, or NetBeans).
2. Set up MySQL database by running the SQL scripts provided in the database folder.
3. Configure your database connection in the DbConnection.java file.
4. Run the HelloApplication.java file to start the application.

   - make Sure eo add this vm option command according to the folder path of your javafx
   
     ```bash
     --module-path "[Folder_path_of_javafx]" --add-modules javafx.controls,javafx.fxml


## Entity Diagram 

 ![Entity_diagram](/diagrams/entity-diagram.png)


 * Database : Save Informations of Tasks in "tasks" Table
 * 
 * DAO : Data Access Object , Works as API between the Model and Database , Contains Mainly main Database Queries
 * 
 * Model : Contains informations about Task and  All Tasks informations , Receives them from Database and from the Controller
 * 
 * Controller : Handle Interactions that happens on the GUI and Works as an Intermediary between View And Controller
 * 
 * View: Handles The User Intercation with the GUI


## Technologies Used

- Java
- JavaFX
- MySQL Database


## Screenshots 

![picture_1](/diagrams/image1.png)
![picture_4](/diagrams/image4.png)
![picture_7](/diagrams/image12.png)
![picture_5](/diagrams/image6.png)
![picture_6](/diagrams/image8.png)
![picture_3](/diagrams/image3.png)
![picture_7](/diagrams/image11.png)
![picture_7](/diagrams/image13.png)

