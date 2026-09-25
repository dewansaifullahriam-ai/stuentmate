package com.studymate;
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

        Scene scene =
                new Scene(
                        loader.load(),
                        1200,
                        700
                );

        stage.setTitle(
                "StudyMate - Study & Task Manager"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {

        DatabaseConnection.initializeDatabase();

        launch(args);
    }
}
