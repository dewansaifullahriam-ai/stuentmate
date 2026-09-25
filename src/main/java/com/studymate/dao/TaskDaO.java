package com.studymate.dao;
import com.studymate.database.DatabaseConnection;
import com.studymate.model.AssignmentTask;
import com.studymate.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class TaskDaO {
    // INSERT
    public void insertTask(Task task) throws SQLException {

        String sql = """
                INSERT INTO tasks
                (title, description, deadline, priority, status, subject)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getDeadline());
            statement.setString(4, task.getPriority());
            statement.setString(5, task.getStatus());
            statement.setString(6, task.getSubject());

            statement.executeUpdate();
        }
    }

    // SELECT
    public List<Task> getAllTasks() throws SQLException {

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             Statement statement =
                     connection.createStatement();

             ResultSet resultSet =
                     statement.executeQuery(sql)) {

            while (resultSet.next()) {

                Task task = new AssignmentTask(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("deadline"),
                        resultSet.getString("priority"),
                        resultSet.getString("status"),
                        resultSet.getString("subject")
                );

                tasks.add(task);
            }
        }

        return tasks;
    }

    // UPDATE
    public void updateTask(Task task) throws SQLException {

        String sql = """
                UPDATE tasks
                SET title=?,
                    description=?,
                    deadline=?,
                    priority=?,
                    status=?,
                    subject=?
                WHERE id=?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getDeadline());
            statement.setString(4, task.getPriority());
            statement.setString(5, task.getStatus());
            statement.setString(6, task.getSubject());
            statement.setInt(7, task.getId());

            statement.executeUpdate();
        }
    }

    // DELETE
    public void deleteTask(int id) throws SQLException {

        String sql = "DELETE FROM tasks WHERE id=?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
}
