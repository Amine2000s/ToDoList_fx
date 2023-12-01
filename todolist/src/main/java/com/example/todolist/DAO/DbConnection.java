package com.example.todolist.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DbConnection {

    private static final String HOST = "127.0.0.1";//localhost
    private static final int PORT = 3306;
    private static final String DB_NAME  = "tasks_database" ;
    private static final String USERNAME  = "root" ;
    private static final String PASSWORD  = "" ;

    private static Connection connection ;
    public static Connection getConnection() throws SQLException {

        //  try {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST,PORT ,DB_NAME), USERNAME, PASSWORD);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        //} catch (SQLException se) {
        //}

        return connection;
    }

}
