package Controller;

import Model.EventList;
import Model.Event;
import Model.ClientConnection;
import View.YearView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class YearController {
    private YearView view;
    private Stage stage;
    private Scene scene;
    private int currentYear;

    public static void loadYear(Stage stage, int year) {
        new YearController(stage, year);
    }
    
    public YearController(Stage stage, int year) {
        this.stage = stage;
        this.view = new YearView((int)stage.getHeight(), (int)stage.getWidth());
        this.currentYear = year;
        
        initializeScene();
        initializeEventHandlers();
        setupSizeBindings();
        loadInitialData();
        
        view.getYearLabel().setText(String.valueOf(currentYear));
        view.setHeight((int)this.stage.getHeight());
        view.setWidth((int)this.stage.getWidth());
        view.updateLayout();
    }

    private void initializeScene() {
        stage.setScene(view.getScene());
        stage.setMinWidth(800);
        stage.setMinHeight(400);
        scene = view.getView().getScene();
        
    }

    private void setupSizeBindings() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            view.setWidth((int)stage.getWidth());
            view.updateLayout();
        });
        
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            view.setHeight((int)stage.getHeight());
            view.updateLayout();
        });
    }

    private void initializeEventHandlers() {
        // Year navigation
        view.getLeftArrow().setOnAction(e -> updateYear(-1));
        view.getRightArrow().setOnAction(e -> updateYear(1));
        
        // Month selection
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            view.getMonthButtons()[i].setOnAction(e -> {
                new MonthController(stage, currentYear, months[finalI]);
            });
        }
        
        // Server shutdown
        view.getStopButton().setOnAction(e -> {
            try {
                String response = ClientConnection.getInstance().sendMessage("STOP");
                ClientConnection.getInstance().disconnect();
                Platform.exit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void updateYear(int delta) {
        currentYear += delta;
        view.getYearLabel().setText(String.valueOf(currentYear));
    }

    private void loadInitialData() {
        try {
            String csvData = ClientConnection.getInstance().sendMessage("LOAD_CSV");
            ArrayList<Event> events = new ArrayList<>();
            
            for (String line : csvData.split("\n")) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                
                events.add(new Event(
                    parts[0].trim(),
                    LocalDateTime.parse(parts[1].trim()),
                    LocalDateTime.parse(parts[2].trim()),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim()
                ));
            }
            EventList.getInstance().setArrayList(events);
        } catch (Exception e) {
            showConnectionError(e);
        }
    }

    private void showConnectionError(Exception e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Server Communication Failed");
            alert.setContentText("Application will exit.\nError: " + e.getMessage());
            alert.showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }
}