package com.example.todolist.Model;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.DAO.TaskDaoImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TasksList extends AbstractTasksList {
    public ObservableList<Task> List ;


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
}
