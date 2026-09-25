package com.studymate.model;

public class ExamTask extends Task {

    public ExamTask() {
        super();
    }

    public ExamTask(int id, String title, String description,
                    String deadline, String priority,
                    String status, String subject) {

        super(id, title, description,
                deadline, priority, status, subject);
    }

    @Override
    public String getTaskType() {
        return "Exam";
    }
}
