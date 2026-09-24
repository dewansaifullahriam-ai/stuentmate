package com.studymate.model;

public class AssignmentTask extends task {

    public AssignmentTask() {
        super();
    }

    public AssignmentTask(int id, String title, String description,
                          String deadline, String priority,
                          String status, String subject) {

        super(id, title, description,
                deadline, priority, status, subject);
    }

    @Override
    public String getTaskType() {
        return "Assignment";
    }
}