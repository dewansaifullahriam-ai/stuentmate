package com.studymate;

import com.studymate.controller.DashboardController;
import com.studymate.database.DatabaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        Main.class.getResource(
                                "/com/studymate/dashboard.fxml"
                        )
                );


        // Load FXML
        Scene scene =
                new Scene(
                        loader.load(),
                        1200,
                        700
                );


        // Get DashboardController
        DashboardController controller =
                loader.getController();


        stage.setTitle(
                "StudyMate - Study & Task Manager"
        );

        stage.setScene(scene);


        // =========================
        // CLOSE APPLICATION
        // =========================

        stage.setOnCloseRequest(event -> {

            // Shut down ExecutorService
            controller.shutdownThreadPool();

        });


        stage.show();
    }


    public static void main(String[] args) {

        // Initialize SQLite database
        DatabaseConnection.initializeDatabase();

        // Start JavaFX application
        launch(args);
    }
}