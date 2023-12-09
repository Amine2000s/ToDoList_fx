package com.example.todolist.DAO;

import com.example.todolist.Model.Task;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.todolist.DAO.DbConnection.connection;
import static com.example.todolist.utils.dateConverter.Convertinto;

public class TaskDaoImp implements TaskDao {


    @Override
    public ArrayList<Task> GetAll() {

        String query = " SELECT * FROM tasks ;";
        //Connection dbcon = null;
        ArrayList<Task> Tasks_list = new ArrayList<>();

        try (Connection dbcon = DbConnection.getConnection()) {
            try (PreparedStatement preparedStatement = dbcon.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Tasks_list.add(new Task(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("deadline"), resultSet.getString("periority"), resultSet.getString("descrpition"), resultSet.getString("category"), resultSet.getBoolean("done")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Tasks_list;
    }


    @Override
    public void CreateTask(Task task) {
        if(task.getId() > 0){

            String query = "UPDATE tasks SET name=? , deadline=? ,periority=?,category=?, descrpition=?, done=? WHERE id=? ;" ;
            try (Connection connection = DbConnection.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, task.getName());
                    preparedStatement.setDate(2, Convertinto(task.getDeadline()));
                    preparedStatement.setString(3, task.getPeriority());
                    preparedStatement.setString(4, task.getCategory());
                    preparedStatement.setString(5, task.getDescrition());
                    preparedStatement.setBoolean(6, task.isDone());
                    preparedStatement.setInt(7, task.getId());

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            String query = "INSERT INTO tasks(name , deadline , periority , category , descrpition , done) VALUES(? , ? , ? , ? , ? , ? ) ; ";
            //UPDATE tasks SET nom=? , deadline=? ,periority=? descrpition=?, done=?  WHERE name=? ;
            try (Connection connection = DbConnection.getConnection()) {

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, task.getName());
                preparedStatement.setDate(2, Convertinto(task.getDeadline()));
                preparedStatement.setString(3, task.getPeriority());
                preparedStatement.setString(4, task.getCategory());
                preparedStatement.setString(5, task.getDescrition());
                preparedStatement.setBoolean(6, task.isDone());


                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Task SearchTask() {
        return null;
    }

    @Override
    public void DeleteTask(Task task) {

        String query = "DELETE FROM tasks WHERE id  = ?";
        try (Connection connection = DbConnection.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, task.getId());

                preparedStatement.executeUpdate();

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Update_Task_status(int id  , boolean  status ){

        String query ="UPDATE tasks SET done=? WHERE id=?";

        try (Connection connection = DbConnection.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setBoolean(1,status);
                preparedStatement.setInt(2,id);

                preparedStatement.executeUpdate();
                System.out.println("done with succes ");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Task> Update_TableViewByCategory(String category){

        String query ="Select * FROM tasks WHERE category=?";
        ArrayList<Task> Tasks_list = new ArrayList<>();

        try(Connection connection = DbConnection.getConnection()){

            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){

                preparedStatement.setString(1,category);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Tasks_list.add(new Task(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("deadline"), resultSet.getString("periority"), resultSet.getString("descrpition"), resultSet.getString("category"), resultSet.getBoolean("done")));
                }

            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Tasks_list;
    }

        @Override
        public List<Task> Filter_Tasks(String category , String priority , String status ){






            return null ;
        }


    }


