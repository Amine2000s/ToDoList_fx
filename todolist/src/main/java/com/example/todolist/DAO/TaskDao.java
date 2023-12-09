package com.example.todolist.DAO;

import com.example.todolist.Model.Task;

import java.util.List;

public interface TaskDao {


    public List<Task> GetAll() ;//retrieve all infromation(tasks) from DB
    public void CreateTask(Task task);//create(insert) task in DB or Update
    public Task SearchTask();//search task from DB
    public void DeleteTask(Task task );//Delete from DB

    public void Update_Task_status(int id  , boolean  status );
    public List<Task> Update_TableViewByCategory(String category);

    public List<Task> Filter_Tasks(String category , String priority , String status );







}
