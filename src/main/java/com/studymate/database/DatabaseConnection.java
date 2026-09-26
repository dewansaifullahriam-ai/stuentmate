package com.studymate.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlite:studymate.db";


    // =========================
    // DATABASE CONNECTION
    // =========================

    public static Connection getConnection()
            throws SQLException {

        Connection connection =
                DriverManager.getConnection(URL);

        // Enable foreign key support
        try (Statement statement =
                     connection.createStatement()) {

            statement.execute(
                    "PRAGMA foreign_keys = ON"
            );
        }

        return connection;
    }


    // =========================
    // DATABASE INITIALIZATION
    // =========================

    public static void initializeDatabase() {

        String tasksTable = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    deadline TEXT,
                    priority TEXT,
                    status TEXT,
                    subject TEXT
                )
                """;


        // One task can have many study sessions
        String studySessionsTable = """
                CREATE TABLE IF NOT EXISTS study_sessions (
                    session_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER NOT NULL,
                    duration INTEGER NOT NULL,
                    session_date TEXT NOT NULL,

                    FOREIGN KEY (task_id)
                        REFERENCES tasks(id)
                        ON DELETE CASCADE
                )
                """;


        try (Connection connection = getConnection();
             Statement statement =
                     connection.createStatement()) {

            // Create existing tasks table
            statement.execute(tasksTable);

            // Create new related table
            statement.execute(studySessionsTable);

            System.out.println(
                    "Database initialized successfully."
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}