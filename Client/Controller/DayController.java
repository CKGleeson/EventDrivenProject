package Client.Controller;

import Client.Model.EventList;
import Client.Model.ClientConnection;
import Client.Model.Event;
import Client.View.DayView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;

public class DayController {
    private final DayView view;
    private final Stage stage;
    private LocalDate currentDay;
    private String previousView;

    public DayController(Stage stage, LocalDate day, String previousView) {
        this.stage = stage;
        this.view = new DayView((int)stage.getHeight(), (int)stage.getWidth());
        this.currentDay = day;
        this.previousView = previousView;
        
        initialize();
        loadDay();
        
        stage.setScene(view.getScene());
        view.setWidth((int)stage.getWidth()-10);
        view.getScrollPane().setPrefWidth(stage.getWidth() - 30);
        view.setHeight((int)stage.getHeight());
        view.getScrollPane().setPrefHeight(stage.getHeight()- 96);
    }
    
    public static void loadDay(Stage stage, int year, int month, int day, String previous) {
        LocalDate date = LocalDate.of(year, month, day);
        new DayController(stage, date, previous);
    }

    private void initialize() {
        setupEventHandlers();
        setupResponsiveLayout();
    }

    private void setupEventHandlers() {
        view.getBackButton().setOnAction(e -> handleBack());
        view.getAddButton().setOnAction(e -> handleAddEvent());
        view.getDeleteButton().setOnAction(e -> handleDeleteEvent());
        view.getOtherButton().setOnAction(e -> handleOtherButton());
    }

    private void setupResponsiveLayout() {
        view.getView().widthProperty().addListener((obs, oldVal, newVal) -> {
            view.setWidth(newVal.intValue());
            view.getScrollPane().setPrefWidth(newVal.doubleValue() - 15);
        });
        
        view.getView().heightProperty().addListener((obs, oldVal, newVal) -> {
            view.setHeight(newVal.intValue());
            view.getScrollPane().setPrefHeight(newVal.doubleValue() - 87);
        });
    }

    private void loadDay() {
        ArrayList<Event> events = EventList.getInstance().geteventsondate(currentDay);
        view.updateView(currentDay, !events.isEmpty());
        
        for (Event event : events) {
            view.getEventContainer().getChildren().add(view.createEventCard(event));
        }
    }

    private void handleBack() {
        if ("week".equals(previousView)) {
            new WeekController(stage, currentDay.minusDays(currentDay.getDayOfWeek().getValue() - 1));
        } else {
            new MonthController(stage, currentDay.getYear(), 
                currentDay.getMonth().toString().substring(0, 1) +
                currentDay.getMonth().toString().substring(1).toLowerCase());
        }
    }

    private void handleAddEvent() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add Event");
        
        // Create grid layout for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create form fields
        TextField nameField = new TextField();
        ComboBox<String> startHour = new ComboBox<>();
        ComboBox<String> startMinute = new ComboBox<>();
        ComboBox<String> endHour = new ComboBox<>();
        ComboBox<String> endMinute = new ComboBox<>();
        TextField locationField = new TextField();
        TextField moduleField = new TextField();
        ComboBox<String> typeField = new ComboBox<>();
        TextField lecturerField = new TextField();

        // Populate time selection
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d", i);
            startHour.getItems().add(hour);
            endHour.getItems().add(hour);
        }
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            startMinute.getItems().add(minute);
            endMinute.getItems().add(minute);
        }

        // Set default values
        startHour.setValue("09");
        startMinute.setValue("00");
        endHour.setValue("10");
        endMinute.setValue("00");
        typeField.getItems().addAll("Lecture", "Lab", "Tutorial");
        typeField.setValue("Lecture");

        // Create time input boxes
        HBox startTimeBox = new HBox(5, startHour, new Label(":"), startMinute);
        startTimeBox.setAlignment(Pos.CENTER_LEFT);
        HBox endTimeBox = new HBox(5, endHour, new Label(":"), endMinute);
        endTimeBox.setAlignment(Pos.CENTER_LEFT);

        // Add fields to grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Start Time:"), 0, 1);
        grid.add(startTimeBox, 1, 1);
        grid.add(new Label("End Time:"), 0, 2);
        grid.add(endTimeBox, 1, 2);
        grid.add(new Label("Location:"), 0, 3);
        grid.add(locationField, 1, 3);
        grid.add(new Label("Module:"), 0, 4);
        grid.add(moduleField, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeField, 1, 5);
        grid.add(new Label("Lecturer:"), 0, 6);
        grid.add(lecturerField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new String[]{
                    nameField.getText().trim(),
                    startHour.getValue() + ":" + startMinute.getValue(),
                    endHour.getValue() + ":" + endMinute.getValue(),
                    locationField.getText().trim(),
                    moduleField.getText().trim(),
                    typeField.getValue(),
                    lecturerField.getText().trim()
                };
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                // Validate input
                if (Arrays.stream(input).anyMatch(s -> s.isEmpty())) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                // Parse times and create event
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime start = LocalTime.parse(input[1], timeFormatter);
                LocalTime end = LocalTime.parse(input[2], timeFormatter);
                LocalDateTime startDateTime = LocalDateTime.of(currentDay, start);
                LocalDateTime endDateTime = LocalDateTime.of(currentDay, end);

                // Create and add event
                Event newEvent = new Event(
                    input[0], startDateTime, endDateTime,
                    input[3], input[4], input[5], input[6]
                );

                // Send to server
                String command = String.format("ADD|%s|%s|%s|%s|%s|%s|%s",
                    input[0],
                    startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    input[3], input[4], input[5], input[6]);

                String response = ClientConnection.getInstance().sendMessage(command);
                
                if (!response.startsWith("Error:")) {
                    EventList.getInstance().add(newEvent);
                    refreshView();
                } else {
                    showAlert("Error", response, AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Error", e.getMessage(), AlertType.ERROR);
            }
        });
    }

    private void handleDeleteEvent() {
        ArrayList<Event> events = EventList.getInstance().geteventsondate(currentDay);
        events = EventList.sortList(events);

        if (events.isEmpty()) {
            showAlert("No Events", "No events scheduled for " + currentDay.format(DateTimeFormatter.ISO_DATE), AlertType.INFORMATION);
            return;
        }

        // Create event selection dialog
        ChoiceDialog<Event> eventDialog = new ChoiceDialog<>(events.get(0), events);
        eventDialog.setTitle("Delete Event");
        eventDialog.setHeaderText("Select an event to delete:");
        eventDialog.setContentText("Event:");

        Optional<Event> result = eventDialog.showAndWait();
        result.ifPresent(event -> {
            Alert confirmDialog = new Alert(
                AlertType.CONFIRMATION,
                "Are you sure you want to delete this event?\n\n" +
                "Name: " + event.getName() + "\n" +
                "Time: " + event.getFormattedTime() + "\n" +
                "Location: " + event.getLocation() + "\n" +
                "Module: " + event.getModule() + "\n" +
                "Lecturer: " + event.getLecturer(),
                ButtonType.YES, ButtonType.NO
            );
            confirmDialog.setTitle("Confirm Deletion");
            
            Optional<ButtonType> confirmResult = confirmDialog.showAndWait();
            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.YES) {
                try {
                    String command = String.format("REMOVE|%s|%s",
                        event.getName(),
                        event.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                    String response = ClientConnection.getInstance().sendMessage(command);
                    
                    if (response.startsWith("Event removed successfully")) {
                        EventList.getInstance().remove(event);
                        refreshView();
                    } else {
                        showAlert("Error", response, AlertType.ERROR);
                    }
                } catch (Exception e) {
                    showAlert("Error", e.getMessage(), AlertType.ERROR);
                }
            }
        });
    }

    private void handleOtherButton() {
        showAlert("Information", "\"Other...\" Button Pressed", AlertType.INFORMATION);
    }

    private void refreshView() {
        view.getEventContainer().getChildren().clear();
        loadDay();
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        
        alert.getDialogPane().setContent(messageLabel);
        alert.showAndWait();
    }
}