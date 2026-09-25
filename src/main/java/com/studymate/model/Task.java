package com.studymate.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Task implements managable {

    protected int id;
    protected String title;
    protected String description;
    protected String deadline;
    protected String priority;
    protected String status;
    protected String subject;

    public Task() {
    }

    public Task(int id, String title, String description,
                String deadline, String priority,
                String status, String subject) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.subject = subject;
    }
     @JsonIgnore
    public abstract String getTaskType();

    @Override
    public void add() {
        System.out.println("Task added");
    }

    @Override
    public void update() {
        System.out.println("Task updated");
    }

    @Override
    public void delete() {
        System.out.println("Task deleted");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}