package View;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerStatusView {
    private Stage primaryStage;

    public void start(Stage stage) {
        primaryStage = stage;
        
        Label statusLabel = new Label("Server is running...");
        Button closeButton = new Button("Close Server");
        
        // Action for close button
        closeButton.setOnAction(e -> shutdownServer());
        
        VBox root = new VBox(20, statusLabel, closeButton);
        root.setStyle("-fx-padding: 20px; -fx-alignment: center;");
        
        Scene scene = new Scene(root, 250, 150);
        primaryStage.setTitle("Lecture Scheduler Server");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        // Handle window close (X button)
        primaryStage.setOnCloseRequest(this::handleWindowClose);
        
        primaryStage.show();
    }

    private void handleWindowClose(WindowEvent event) {
        event.consume(); // Consume the event to prevent immediate closing
        shutdownServer();
    }

    private void shutdownServer() {
        Platform.exit();
        System.exit(0);
    }
}