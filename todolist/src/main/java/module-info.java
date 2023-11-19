module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.todolist to javafx.fxml;
    exports com.example.todolist;
    exports com.example.todolist.Controller;
    opens com.example.todolist.Controller to javafx.fxml;
}