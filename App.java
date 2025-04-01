package App;

import Client.Controller.YearController;
import Client.Model.EventList;
import javafx.application.Application;
import javafx.stage.Stage;
import java.time.Year;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Initialize model (singleton instance)
        EventList.getInstance();
        
        // Configure primary stage properties
        primaryStage.setTitle("Calendar System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(400);
                primaryStage.setHeight(400);
                primaryStage.setWidth(800);

        // Load year view with current year
        YearController.loadYear(primaryStage, Year.now().getValue());
        primaryStage.show();    
    }

    public static void main(String[] args) {
        launch(args);
    }
}