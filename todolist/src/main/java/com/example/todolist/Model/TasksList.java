package com.example.todolist.Model;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.DAO.TaskDaoImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TasksList extends AbstractTasksList {

    public ObservableList<Task> List ;//this list is very mportnae its one we need in order to make 80% of content actions
    //Synchronized

    public TasksList() {
        List = FXCollections.observableArrayList();
    }

    public ObservableList<Task> getList() {
         this.loadfromDB();
         return List ;
    }

    public void setList(ArrayList<Task> list) {
    }

    public void loadfromDB(){
        TaskDao taskDao = new TaskDaoImp();
        List.clear();
        List.addAll(taskDao.GetAll());

    }

    public void Delete_task(Task task){




    }
}
