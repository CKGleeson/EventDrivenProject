package Controller;

import View.MonthView;
import Model.EventList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.Month;

public class MonthController {
    private MonthView view;
    private Stage stage;
    private Scene scene;
    private YearMonth currentYearMonth;

    public MonthController(Stage primaryStage, int year, String monthName) {
        this.stage = primaryStage;
        this.view = new MonthView((int)stage.getHeight(),(int)stage.getWidth());
        this.currentYearMonth = YearMonth.of(year, Month.valueOf(monthName.toUpperCase()));

        initializeScene();
        initializeEventHandlers();
        setupSizeBindings();
        loadMonth();
        setupGridInteractions();
        
    }
    
    public static void loadMonth(Stage stage, int year, String month) {
        new MonthController(stage, year, month);
    }

    private void initializeScene() {
        stage.setScene(view.getScene());
        stage.setMinWidth(800); // Optional minimum size
        stage.setMinHeight(400);
        scene = view.getView().getScene(); // Get the scene reference
        setupSizeBindings(); // Call after scene is set
    }

    private void setupSizeBindings() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (view.getRowButtons() != null) {
                view.setWidth((int)stage.getWidth());
                view.updateLayout(view.getRowButtons().length);
            }
        });
        
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (view.getRowButtons() != null) {
                view.setHeight((int)stage.getHeight());
                view.updateLayout(view.getRowButtons().length);
            }
        });
    }

    private void initializeEventHandlers() {
        view.getBackButton().setOnAction(e -> YearController.loadYear(stage, currentYearMonth.getYear()));
        view.getPrevMonthButton().setOnAction(e -> navigateMonth(-1));
        view.getNextMonthButton().setOnAction(e -> navigateMonth(1));
        view.getAddButton().setOnAction(e -> showAddInfo());
        view.getDeleteButton().setOnAction(e -> showDeleteInfo());
        
    }

    private void setupGridInteractions() {
    // Week row handlers
    if (view.getRowButtons() != null && view.getRowButtons().length > 0) {
        for (int row = 0; row < view.getRowButtons().length; row++) {
            int finalRow = row;
            view.getRowButtons()[row].setOnAction(e -> {
                System.out.println("Week button clicked - row: " + finalRow);
                new WeekController(stage, calculateWeekStart(finalRow));
            });
        }
    } else {
        System.out.println("Row buttons not initialized");
    }

    // Date cell handlers
    if (view.getDateButtons() != null && view.getDateButtons().length > 0) {
        for (int i = 0; i < view.getDateButtons().length; i++) {
            int index = i;
            view.getDateButtons()[i].setOnAction(e -> {
                System.out.println("Date button clicked - index: " + index);
                new DayController(stage, calculateDate(index), "month");
            });
        }
    } else {
        System.out.println("Date buttons not initialized");
    }
}

    private LocalDate calculateWeekStart(int row) {
        return currentYearMonth.atDay(1)
            .minusDays(currentYearMonth.atDay(1).getDayOfWeek().getValue() - 1)
            .plusDays(row * 7);
    }

    private LocalDate calculateDate(int index) {
        return currentYearMonth.atDay(1)
            .minusDays(currentYearMonth.atDay(1).getDayOfWeek().getValue() - 1)
            .plusDays(index);
    }

    private void loadMonth() {
    view.getTitleLabel().setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());
    
    LocalDate firstOfMonth = currentYearMonth.atDay(1);
    LocalDate firstCellDate = firstOfMonth.minusDays(firstOfMonth.getDayOfWeek().getValue() - 1); // Start from Monday
    
    int daysInMonth = currentYearMonth.lengthOfMonth();
    int daysFromPrevMonth = firstOfMonth.getDayOfWeek().getValue() - 1; // Days needed from previous month
    
    // Calculate total weeks needed
    int rowCount = (int) Math.ceil((daysFromPrevMonth + daysInMonth) / 7.0);
    
    // Handle edge cases
    if (rowCount < 4) rowCount = 4; // Minimum 4 rows (very rare)
    if (rowCount > 6) rowCount = 6; // Maximum 6 rows
    
    view.createCalendarGrid(currentYearMonth, firstCellDate, rowCount);
    updateEventIndicators();
    view.updateLayout(rowCount);
}

    // In MonthController.java
private void updateEventIndicators() {
    for (int i = 0; i < view.getDateButtons().length; i++) {
        LocalDate date = calculateDate(i);
        int eventCount = EventList.getInstance().alleventsondate(date).size();
        
        if (eventCount == 1) {
            view.getNoteLabels()[i].setText(eventCount + " event");
        } else {
            view.getNoteLabels()[i].setText(eventCount + " events");
        }
    }
}    
    // In MonthController.java
public static boolean isDateInCurrentMonth(LocalDate date, YearMonth currentYearMonth) {
    return date.getMonth() == currentYearMonth.getMonth() && 
           date.getYear() == currentYearMonth.getYear();
}

    private void navigateMonth(int delta) {
    YearMonth newYearMonth = currentYearMonth.plusMonths(delta);
    MonthController.loadMonth(stage, newYearMonth.getYear(), 
        newYearMonth.getMonth().toString().substring(0, 1) +
        newYearMonth.getMonth().toString().substring(1).toLowerCase());
}


    private void showYearView() {
    YearController.loadYear(stage, currentYearMonth.getYear());
}


    private void showAddInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Click a day to add events").showAndWait();
    }

    private void showDeleteInfo() {
        new Alert(Alert.AlertType.INFORMATION, "Click a day to remove events").showAndWait();
    }
}