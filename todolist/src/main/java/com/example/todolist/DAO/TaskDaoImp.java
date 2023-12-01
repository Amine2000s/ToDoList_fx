package com.example.todolist.DAO;

import com.example.todolist.Model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.todolist.utils.dateConverter.Convertinto;

public class TaskDaoImp implements TaskDao{


    @Override
    public ArrayList<Task> GetAll() {

        String query = " SELECT * FROM tasks ;";
        //Connection dbcon = null;
        ArrayList<Task> Tasks_list = new ArrayList<>();

        try(Connection dbcon = DbConnection.getConnection()){

            try(PreparedStatement preparedStatement = dbcon.prepareStatement(query)){

                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){

                    Tasks_list.add(new Task(resultSet.getString("name"),resultSet.getDate("deadline"),resultSet.getString("periority"),resultSet.getString("category"),resultSet.getString("descrpition"),resultSet.getBoolean("done")));


                }
            }catch (Exception e ){
                e.printStackTrace();
            }

        }catch (SQLException e ){
            e.printStackTrace();
        }

        return Tasks_list;






    }

    @Override
    public void CreateTask(Task task){

        String query = "INSERT INTO tasks(name , deadline , periority , category , descrpition , done) VALUES(? , ? , ? , ? , ? , ? ) ; ";

        try(Connection connection = DbConnection.getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, task.getName());
            preparedStatement.setDate(2,   Convertinto(task.getDeadline()));
            preparedStatement.setString(3, task.getPeriority());
            preparedStatement.setString(4, task.getCategory());
            preparedStatement.setString(5, task.getDescrition());
            preparedStatement.setBoolean(6, task.isDone());


            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Task SearchTask() {
        return null;
    }

    @Override
    public void DeleteTask() {

    }

}
