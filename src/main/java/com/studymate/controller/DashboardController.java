package com.studymate.controller;

import com.studymate.dao.TaskDaO;
import com.studymate.model.AssignmentTask;
import com.studymate.model.Task;
import com.studymate.service.ApiService;
import com.studymate.service.JsonService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.util.List;

public class DashboardController {

    // =========================
    // INPUT FIELDS
    // =========================

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField deadlineField;

    @FXML
    private TextField subjectField;


    @FXML
    private ComboBox<String> priorityBox;

    @FXML
    private ComboBox<String> statusBox;


    // =========================
    // SEARCH / FILTER
    // =========================

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> priorityFilter;

    @FXML
    private ComboBox<String> statusFilter;


    // =========================
    // TABLE
    // =========================

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, String> subjectColumn;

    @FXML
    private TableColumn<Task, String> deadlineColumn;

    @FXML
    private TableColumn<Task, String> priorityColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;


    // =========================
    // STATISTICS
    // =========================

    @FXML
    private Label totalLabel;

    @FXML
    private Label completedLabel;

    @FXML
    private Label pendingLabel;

    @FXML
    private Label highPriorityLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;


    // =========================
    // API
    // =========================

    @FXML
    private Label quoteLabel;


    // =========================
    // TIMER
    // =========================

    @FXML
    private Label timerLabel;

    private Thread timerThread;

    private volatile boolean timerRunning = false;

    private int remainingSeconds = 25 * 60;


    // =========================
    // SERVICES
    // =========================

    private final TaskDaO taskDAO =
            new TaskDaO();

    private final JsonService jsonService =
            new JsonService();

    private final ApiService apiService =
            new ApiService();


    // =========================
    // DATA
    // =========================

    private final ObservableList<Task> taskList =
            FXCollections.observableArrayList();

    private FilteredList<Task> filteredTasks;


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        setupComboBoxes();

        setupTable();

        setupFilters();

        setupRowColors();

        loadTasks();

        updateStatistics();

        updateTimerLabel();
    }


    // =========================
    // COMBO BOXES
    // =========================

    private void setupComboBoxes() {

        priorityBox.setItems(
                FXCollections.observableArrayList(
                        "Low",
                        "Medium",
                        "High"
                )
        );

        statusBox.setItems(
                FXCollections.observableArrayList(
                        "Pending",
                        "Completed"
                )
        );

        priorityBox.setValue("Medium");

        statusBox.setValue("Pending");


        priorityFilter.setItems(
                FXCollections.observableArrayList(
                        "All",
                        "Low",
                        "Medium",
                        "High"
                )
        );

        priorityFilter.setValue("All");


        statusFilter.setItems(
                FXCollections.observableArrayList(
                        "All",
                        "Pending",
                        "Completed"
                )
        );

        statusFilter.setValue("All");
    }


    // =========================
    // TABLE
    // =========================

    private void setupTable() {

        idColumn.setCellValueFactory(
                cell ->
                        new SimpleObjectProperty<>(
                                cell.getValue().getId()
                        )
        );

        titleColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getTitle()
                        )
        );

        subjectColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getSubject()
                        )
        );

        deadlineColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getDeadline()
                        )
        );

        priorityColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getPriority()
                        )
        );

        statusColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getStatus()
                        )
        );


        filteredTasks =
                new FilteredList<>(
                        taskList,
                        task -> true
                );

        taskTable.setItems(filteredTasks);


        // Select task
        taskTable.setOnMouseClicked(event -> {

            Task selected =
                    taskTable.getSelectionModel()
                            .getSelectedItem();

            if (selected != null) {

                fillFields(selected);
            }
        });
    }


    // =========================
    // COLORFUL TABLE ROWS
    // =========================

    private void setupRowColors() {

        taskTable.setRowFactory(tableView -> {

            TableRow<Task> row =
                    new TableRow<>();

            row.itemProperty().addListener(
                    (obs, oldTask, newTask) -> {

                        if (newTask == null) {

                            row.setStyle("");

                            return;
                        }

                        if ("Completed".equalsIgnoreCase(
                                newTask.getStatus())) {

                            row.setStyle(
                                    "-fx-background-color: #dcfce7;"
                            );

                        } else if ("High".equalsIgnoreCase(
                                newTask.getPriority())) {

                            row.setStyle(
                                    "-fx-background-color: #fee2e2;"
                            );

                        } else if ("Medium".equalsIgnoreCase(
                                newTask.getPriority())) {

                            row.setStyle(
                                    "-fx-background-color: #fef3c7;"
                            );

                        } else {

                            row.setStyle(
                                    "-fx-background-color: #dcfce7;"
                            );
                        }
                    }
            );

            return row;
        });
    }


    // =========================
    // SEARCH + FILTER
    // =========================

    private void setupFilters() {

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );

        priorityFilter.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );

        statusFilter.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilters()
        );
    }


    private void applyFilters() {

        if (filteredTasks == null) {
            return;
        }

        String search =
                searchField.getText()
                        .toLowerCase()
                        .trim();

        String priority =
                priorityFilter.getValue();

        String status =
                statusFilter.getValue();


        filteredTasks.setPredicate(task -> {

            boolean matchesSearch =
                    search.isEmpty()
                            ||
                            task.getTitle()
                                    .toLowerCase()
                                    .contains(search)
                            ||
                            task.getSubject()
                                    .toLowerCase()
                                    .contains(search);


            boolean matchesPriority =
                    priority == null
                            ||
                            priority.equals("All")
                            ||
                            priority.equals(
                                    task.getPriority()
                            );


            boolean matchesStatus =
                    status == null
                            ||
                            status.equals("All")
                            ||
                            status.equals(
                                    task.getStatus()
                            );


            return matchesSearch
                    && matchesPriority
                    && matchesStatus;
        });
    }


    // =========================
    // LOAD TASKS
    // =========================

    private void loadTasks() {

        try {

            taskList.clear();

            taskList.addAll(
                    taskDAO.getAllTasks()
            );

            updateStatistics();

        } catch (Exception e) {

            showAlert(
                    "Database Error",
                    e.getMessage()
            );
        }
    }


    // =========================
    // ADD TASK
    // =========================

    @FXML
    private void addTask() {

        if (titleField.getText().isBlank()) {

            showAlert(
                    "Validation",
                    "Please enter a task title."
            );

            return;
        }


        Task task =
                new AssignmentTask(
                        0,
                        titleField.getText(),
                        descriptionField.getText(),
                        deadlineField.getText(),
                        priorityBox.getValue(),
                        statusBox.getValue(),
                        subjectField.getText()
                );


        try {

            taskDAO.insertTask(task);

            loadTasks();

            clearFields();

            showAlert(
                    "Success",
                    "Task added successfully."
            );

        } catch (Exception e) {

            showAlert(
                    "Database Error",
                    e.getMessage()
            );
        }
    }


    // =========================
    // UPDATE TASK
    // =========================

    @FXML
    private void updateTask() {

        Task selected =
                taskTable.getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showAlert(
                    "Update",
                    "Please select a task first."
            );

            return;
        }


        selected.setTitle(
                titleField.getText()
        );

        selected.setDescription(
                descriptionField.getText()
        );

        selected.setDeadline(
                deadlineField.getText()
        );

        selected.setPriority(
                priorityBox.getValue()
        );

        selected.setStatus(
                statusBox.getValue()
        );

        selected.setSubject(
                subjectField.getText()
        );


        try {

            taskDAO.updateTask(selected);

            loadTasks();

            clearFields();

            showAlert(
                    "Success",
                    "Task updated successfully."
            );

        } catch (Exception e) {

            showAlert(
                    "Database Error",
                    e.getMessage()
            );
        }
    }


    // =========================
    // DELETE TASK
    // =========================

    @FXML
    private void deleteTask() {

        Task selected =
                taskTable.getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showAlert(
                    "Delete",
                    "Please select a task first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Task"
        );

        confirmation.setHeaderText(
                "Delete selected task?"
        );

        confirmation.setContentText(
                selected.getTitle()
        );


        if (confirmation.showAndWait()
                .orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            try {

                taskDAO.deleteTask(
                        selected.getId()
                );

                loadTasks();

                clearFields();

            } catch (Exception e) {

                showAlert(
                        "Database Error",
                        e.getMessage()
                );
            }
        }
    }


    // =========================
    // MARK COMPLETE
    // =========================

    @FXML
    private void markCompleted() {

        Task selected =
                taskTable.getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showAlert(
                    "Complete Task",
                    "Please select a task first."
            );

            return;
        }


        selected.setStatus("Completed");


        try {

            taskDAO.updateTask(selected);

            loadTasks();

            showAlert(
                    "Completed",
                    "Task marked as completed!"
            );

        } catch (Exception e) {

            showAlert(
                    "Database Error",
                    e.getMessage()
            );
        }
    }


    // =========================
    // REFRESH
    // =========================

    @FXML
    private void refreshTasks() {

        loadTasks();

        applyFilters();
    }


    // =========================
    // STATISTICS
    // =========================

    private void updateStatistics() {

        int total =
                taskList.size();

        int completed = 0;

        int pending = 0;

        int highPriority = 0;


        for (Task task : taskList) {

            if ("Completed".equalsIgnoreCase(
                    task.getStatus())) {

                completed++;

            } else {

                pending++;
            }


            if ("High".equalsIgnoreCase(
                    task.getPriority())) {

                highPriority++;
            }
        }


        totalLabel.setText(
                String.valueOf(total)
        );

        completedLabel.setText(
                String.valueOf(completed)
        );

        pendingLabel.setText(
                String.valueOf(pending)
        );

        highPriorityLabel.setText(
                String.valueOf(highPriority)
        );


        double progress =
                total == 0
                        ? 0
                        : (double) completed / total;


        progressBar.setProgress(progress);


        progressLabel.setText(
                String.format(
                        "%.0f%% completed",
                        progress * 100
                )
        );
    }


    // =========================
    // CLEAR FIELDS
    // =========================

    @FXML
    private void clearFields() {

        titleField.clear();

        descriptionField.clear();

        deadlineField.clear();

        subjectField.clear();

        priorityBox.setValue(
                "Medium"
        );

        statusBox.setValue(
                "Pending"
        );

        taskTable.getSelectionModel()
                .clearSelection();
    }


    // =========================
    // JSON EXPORT
    // =========================

    @FXML
    private void exportJSON() {

        try {

            jsonService.exportTasks(
                    taskList,
                    "tasks.json"
            );

            showAlert(
                    "JSON Export",
                    "Tasks exported successfully!"
            );

        } catch (Exception e) {

            showAlert(
                    "JSON Error",
                    e.getMessage()
            );
        }
    }


    // =========================
    // JSON IMPORT
    // =========================

    @FXML
    private void importJSON() {

        try {

            List<AssignmentTask> tasks =
                    jsonService.importTasks(
                            "tasks.json"
                    );


            for (Task task : tasks) {

                taskDAO.insertTask(task);
            }


            loadTasks();


            showAlert(
                    "JSON Import",
                    "Tasks imported successfully!"
            );

        } catch (Exception e) {

            showAlert(
                    "JSON Error",
                    "Could not import tasks.json\n\n"
                            + e.getMessage()
            );
        }
    }


    // =========================
    // API + THREAD
    // =========================

    @FXML
    private void loadQuote() {

        quoteLabel.setText(
                "Loading motivation..."
        );


        Thread apiThread =
                new Thread(() -> {

                    try {

                        String quote =
                                apiService.getStudyQuote();


                        Platform.runLater(() ->
                                quoteLabel.setText(
                                        "\"" + quote + "\""
                                )
                        );


                    } catch (Exception e) {

                        Platform.runLater(() ->
                                quoteLabel.setText(
                                        "Unable to load quote."
                                )
                        );
                    }
                });


        apiThread.setDaemon(true);

        apiThread.start();
    }


    // =========================
    // TIMER
    // =========================

    @FXML
    private void startTimer() {

        if (timerRunning) {
            return;
        }


        timerRunning = true;


        timerThread =
                new Thread(() -> {

                    while (
                            timerRunning
                                    &&
                                    remainingSeconds > 0
                    ) {

                        try {

                            Thread.sleep(1000);

                            remainingSeconds--;


                            Platform.runLater(
                                    this::updateTimerLabel
                            );


                        } catch (
                                InterruptedException e) {

                            Thread.currentThread()
                                    .interrupt();

                            break;
                        }
                    }


                    timerRunning = false;


                    if (remainingSeconds == 0) {

                        Platform.runLater(() ->
                                showAlert(
                                        "Study Session",
                                        "Study session completed!"
                                )
                        );
                    }

                });


        timerThread.setDaemon(true);

        timerThread.start();
    }


    @FXML
    private void pauseTimer() {

        timerRunning = false;
    }


    @FXML
    private void resetTimer() {

        timerRunning = false;

        remainingSeconds =
                25 * 60;

        updateTimerLabel();
    }


    private void updateTimerLabel() {

        int minutes =
                remainingSeconds / 60;

        int seconds =
                remainingSeconds % 60;


        timerLabel.setText(
                String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                )
        );
    }


    // =========================
    // FILL FORM
    // =========================

    private void fillFields(Task task) {

        titleField.setText(
                task.getTitle()
        );

        descriptionField.setText(
                task.getDescription()
        );

        deadlineField.setText(
                task.getDeadline()
        );

        subjectField.setText(
                task.getSubject()
        );

        priorityBox.setValue(
                task.getPriority()
        );

        statusBox.setValue(
                task.getStatus()
        );
    }


    // =========================
    // ALERT
    // =========================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}