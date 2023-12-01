DROP DATABASE IF EXISTS tasks_database;

CREATE DATABASE tasks_database ;

USE tasks_database ;

CREATE TABLE tasks (

    id INT(15) PRIMARY KEY AUTO_INCREMENT ,
    name VARCHAR(35) ,
    deadline DATE ,
    periority VARCHAR(15),
    category VARCHAR(15),
    descrpition VARCHAR(300),
    done BOOLEAN
);