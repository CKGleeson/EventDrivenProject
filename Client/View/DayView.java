package Client.View;

import Client.Model.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Scene;

public class DayView {
    private Pane root;
    private Label titleLabel;
    private Button backButton, addButton, deleteButton, otherButton;
    private ScrollPane scrollPane;
    private VBox eventContainer;
    private int width;
    private int height;

    // Styling constants
    private double titleBarHeight = 40;
    private double controlButtonWidth = 80;
    private double controlButtonHeight = 35;
    private double horizontalSpacing = 10;
    private double edgeMargin = 15;
    private double scrollPaneCornerRadius = 5;
    private double cellBorderWidth = 1;
    private double eventBoxHeight = 80;
    private double eventBoxSpacing = 5;

    public DayView(int height, int width) {
        this.height = height;
        this.width = width;
        initializeUI();
    }

    private void initializeUI() {
        root = new Pane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Title label
        titleLabel = new Label();
        titleLabel.setFont(Font.font(18));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        titleLabel.setLayoutX(edgeMargin);
        titleLabel.setLayoutY(titleBarHeight / 2 - titleLabel.prefHeight(-1) / 2);
        
        // Control buttons with consistent styling
        backButton = createStyledButton("← Back", "#2196F3");
        addButton = createStyledButton("＋ Add", "#4CAF50");
        deleteButton = createStyledButton("🗑 Delete", "#f44336");
        otherButton = createStyledButton("Other...", "#9E9E9E");
        
        // Position buttons (right-aligned)
        double buttonX = width - (controlButtonWidth * 4 + horizontalSpacing * 3) - edgeMargin;
        backButton.setLayoutX(buttonX);
        backButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        addButton.setLayoutX(buttonX + controlButtonWidth + horizontalSpacing);
        addButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        deleteButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 2);
        deleteButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        otherButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 3);
        otherButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        // Set fixed button sizes
        backButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        addButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        deleteButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        otherButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        
        // Event container setup
        eventContainer = new VBox(eventBoxSpacing);
        eventContainer.setPadding(new Insets(10));
        eventContainer.setStyle("-fx-background-color: #ffffff;");
        
        scrollPane = new ScrollPane(eventContainer);
        scrollPane.setStyle(
            "-fx-background: #ffffff; " +
            "-fx-border-color: #e0e0e0; " +
            String.format("-fx-border-width: %.1fpx; ", cellBorderWidth) +
            String.format("-fx-background-radius: %.1fpx; ", scrollPaneCornerRadius) +
            String.format("-fx-border-radius: %.1fpx; ", scrollPaneCornerRadius)
        );
        scrollPane.setFitToWidth(true);
        
        // Position scroll pane
        scrollPane.setLayoutX(edgeMargin);
        scrollPane.setLayoutY(titleBarHeight + 10);
        scrollPane.setPrefWidth(width - 2 * edgeMargin);
        scrollPane.setPrefHeight(height - titleBarHeight - 20);
        
        root.getChildren().addAll(titleLabel, backButton, addButton, deleteButton, otherButton, scrollPane);
    }

    public void updateView(LocalDate day, boolean hasEvents) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        titleLabel.setText(day.format(formatter));
        
        eventContainer.getChildren().clear();
        if (!hasEvents) {
            Label noEvents = new Label("No events scheduled for " + day.format(DateTimeFormatter.ISO_DATE));
            noEvents.setStyle("-fx-text-fill: #666; -fx-font-size: 14;");
            eventContainer.getChildren().add(noEvents);
        }
    }

    public VBox createEventCard(Event event) {
        VBox card = new VBox(5);
        card.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-padding: 10; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );
        card.setPrefWidth(width - 2 * edgeMargin - 25); // Account for scrollbar
        card.setMinHeight(eventBoxHeight);

        // Event type label
        Label typeLabel = new Label(event.getType().toUpperCase());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // Name label
        Label nameLabel = new Label(event.getName());
        nameLabel.setStyle("-fx-font-size: 13;");

        // Time range label
        Label timeLabel = new Label(event.getFormattedTime());
        timeLabel.setStyle("-fx-text-fill: #636e72; -fx-font-size: 12;");

        // Details label
        Label detailsLabel = new Label(event.getFormattedDetails());
        detailsLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11;");

        card.getChildren().addAll(typeLabel, nameLabel, timeLabel, detailsLabel);
        
        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #f0f7ff; " +
            "-fx-border-color: #90caf9; " +
            "-fx-padding: 10; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-padding: 10; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        ));
        
        return card;
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 8 12 8 12;"
        );
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: derive(" + color + ", -20%); " +
            "-fx-background-radius: 5px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            "-fx-background-radius: 5px;"
        ));
        
        return btn;
    }

    public void updateLayout() {
        root.applyCss();
        root.layout();
        
        // Position title label
        titleLabel.setLayoutX(edgeMargin);
        titleLabel.setLayoutY(titleBarHeight / 2 - titleLabel.prefHeight(-1) / 2);
        
        // Position buttons (right-aligned)
        double buttonX = width - (controlButtonWidth * 4 + horizontalSpacing * 3) - edgeMargin;
        backButton.setLayoutX(buttonX);
        backButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        addButton.setLayoutX(buttonX + controlButtonWidth + horizontalSpacing);
        addButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        deleteButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 2);
        deleteButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        otherButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 3);
        otherButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        
        // Position scroll pane
        scrollPane.setLayoutX(edgeMargin);
        scrollPane.setLayoutY(titleBarHeight + 10);
        scrollPane.setPrefWidth(width - 2 * edgeMargin);
        scrollPane.setPrefHeight(height - titleBarHeight - 20);
    }

    // Getters
    public Scene getScene() { return new Scene(root); }
    public Pane getView() { return root; }
    public Button getBackButton() { return backButton; }
    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getOtherButton() { return otherButton; }
    public VBox getEventContainer() { return eventContainer; }
    public ScrollPane getScrollPane() { return scrollPane; }
    
    // Size setters
    public void setWidth(int width) { 
        this.width = width; 
        updateLayout();
    }
    
    public void setHeight(int height) { 
        this.height = height; 
        updateLayout();
    }
}