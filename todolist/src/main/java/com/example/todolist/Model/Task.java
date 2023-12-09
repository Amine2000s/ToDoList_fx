package com.example.todolist.Model;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;

import java.util.Date;

public class Task extends AbstractTask {

    public int id ;

    public String  name ;

    public Date deadline;

    public String periority;

    public String description;

    public String category;

    public JFXCheckBox status ;
    public boolean done;

    public Task(String name, Date deadline, String periority, String descrition, String category, boolean done) {
        this.id=0;
        this.name = name;
        this.deadline = deadline;
        this.periority = periority;
        this.description = descrition;
        this.category = category;
        this.done = done;
        status = new JFXCheckBox();
    }
    public Task(int id , String name, Date deadline, String periority, String descrition, String category, boolean done) {
        this.id=id;
        this.name = name;
        this.deadline = deadline;
        this.periority = periority;
        this.description = descrition;
        this.category = category;
        this.done = done;
        status = new JFXCheckBox();
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getPeriority() {
        return periority;
    }

    public String getDescrition() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDone() {
        return done;
    }

    public JFXCheckBox getStatus() {
        return status;
    }

    public void setStatus(JFXCheckBox status) {
        this.status = status;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", deadline=" + deadline +
                ", periority='" + periority + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", done=" + done +
                '}';
    }
}
