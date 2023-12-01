package com.example.todolist.utils;

public class dateConverter {

    public static java.sql.Date Convertinto(java.util.Date Date){
        return new java.sql.Date(Date.getTime());
    }
}
