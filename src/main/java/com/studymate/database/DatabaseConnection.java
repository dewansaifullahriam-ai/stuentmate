package com.studymate.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlite:studymate.db";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        String sql = """
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

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println("Database initialized.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}