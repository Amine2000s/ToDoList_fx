package com.example.todolist.DAO;

import com.example.todolist.Model.Task;

import java.util.List;

public interface TaskDao {


    public List<Task> GetAll() ;
    public void CreateTask(Task task);
    public Task SearchTask();
    public void DeleteTask();





}
