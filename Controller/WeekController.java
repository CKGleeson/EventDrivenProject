package Controller;

import View.WeekView;
import Model.Event;
import Model.EventList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Scene;         // For creating the scene
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Alert; // For the information alerts
import javafx.scene.control.Label; // For the labels
import javafx.scene.layout.Pane;   // For the root pane
import javafx.scene.layout.VBox;   // For event containers

public class WeekController {
    private WeekView view;
    private Stage stage;
    private LocalDate currentWeekStart;

    public WeekController(Stage stage, LocalDate weekStart) {
        this.stage = stage;
        this.view = new WeekView((int)stage.getHeight(), (int)stage.getWidth());
        this.currentWeekStart = weekStart.with(DayOfWeek.MONDAY);
        
        initialize();
        loadWeek();
        show();
    }
    
    public static void loadWeek(Stage stage, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        new WeekController(stage, date);
    }

    private void initialize() {
        setupEventHandlers();
        setupResponsiveLayout();
    }

    private void setupEventHandlers() {
        view.getBackButton().setOnAction(e -> navigateBack());
        view.getPrevWeekButton().setOnAction(e -> navigateWeek(-1));
        view.getNextWeekButton().setOnAction(e -> navigateWeek(1));
        view.getAddButton().setOnAction(e -> showAddInfo());
        view.getDeleteButton().setOnAction(e -> showDeleteInfo());
        
        for (int i = 0; i < 7; i++) {
            int dayIndex = i;
            view.getDateButtons()[i].setOnAction(e -> 
                handleDaySelection(currentWeekStart.plusDays(dayIndex))
            );
        }
    }

    private void setupResponsiveLayout() {
        view.getView().widthProperty().addListener((obs, oldVal, newVal) -> {
            view.setWidth((int)stage.getWidth());
            view.updateLayout(currentWeekStart);
        });
        
        view.getView().heightProperty().addListener((obs, oldVal, newVal) -> {
            view.setHeight((int)stage.getHeight());
            view.updateLayout(currentWeekStart);
        });
    }

    private void loadWeek() {
        view.setHeight((int)stage.getHeight());
        view.setWidth((int)stage.getWidth());
        view.updateLayout(currentWeekStart);
        updateEventDisplays();
    }

    private void updateEventDisplays() {
        VBox[] eventContainers = view.getEventContainers();
        Button[] dateButtons = view.getDateButtons();

        for (int i = 0; i < 7; i++) {
            LocalDate date = currentWeekStart.plusDays(i);
            VBox container = eventContainers[i];
            container.getChildren().clear();

            ArrayList<Event> events = EventList.getInstance().geteventsondate(date);
            if (!events.isEmpty()) {
                double cellWidth = stage.getWidth()/7 - 30 ;
                events.forEach(event -> 
                    container.getChildren().add(view.createEventCard(event, cellWidth))
                );
            } else {
                container.getChildren().add(new Label("No events"));
            }
        }
    }

    private void navigateWeek(int weeks) {
        currentWeekStart = currentWeekStart.plusWeeks(weeks);
        loadWeek();
    }

    private void navigateBack() {
        new MonthController(stage, currentWeekStart.getYear(), 
            currentWeekStart.getMonth().toString().substring(0, 1) +
            currentWeekStart.getMonth().toString().substring(1).toLowerCase());
    }

    private void handleDaySelection(LocalDate date) {
        new DayController(stage, date, "week");
    }

    private void showAddInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Click a day to add events").showAndWait();
    }

    private void showDeleteInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Click a day to remove events").showAndWait();
    }

    public void show() {
        stage.setScene(new Scene(view.getView(), stage.getWidth(), stage.getHeight()));
        stage.show();
    }
}