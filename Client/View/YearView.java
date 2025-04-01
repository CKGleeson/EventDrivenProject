package Client.View;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.Scene;

public class YearView {
    private Pane root;
    private Label yearLabel;
    private Button leftArrow, rightArrow, stopButton;
    private Button[] monthButtons = new Button[12];
    private int height;
    private int width;

    // Styling constants
    private double titleBarHeight = 60;
    private double edgeMargin = 20;
    private double buttonCornerRadius = 5;
    private double monthButtonHeight = 50;
    private double monthButtonFontSize = 14;
    private double arrowButtonSize = 60;

    public YearView(int height, int width) {
        this.width = width;
        this.height = height;
        initializeUI();
    }

    private void initializeUI() {
        root = new Pane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Year label with improved styling
        yearLabel = new Label();
        yearLabel.setFont(Font.font(36));
        yearLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        double stopButtonWidth = 90;
        // Navigation buttons with consistent styling
        leftArrow = createStyledArrowButton("◀", "#2196F3", arrowButtonSize);
        rightArrow = createStyledArrowButton("▶", "#2196F3", arrowButtonSize);
        stopButton = createStyledButton("Stop", "#f44336", stopButtonWidth);
        
        // Month buttons grid
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                              "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < 12; i++) {
            monthButtons[i] = createStyledButton(monthNames[i], "#607D8B", 0);
            monthButtons[i].setStyle(
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #607D8B; " +
                String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius)
            );
            
            // Add hover effects
            addMonthButtonHoverEffect(monthButtons[i]);
            root.getChildren().add(monthButtons[i]);
        }

        root.getChildren().addAll(yearLabel, leftArrow, rightArrow, stopButton);
        updateLayout();
    }

    public void updateLayout() {
        root.applyCss();
        root.layout();
        
        // Year label positioning - center horizontally
        double yearLabelX = (width - yearLabel.prefWidth(-1)) / 2;
        double yearLabelY = titleBarHeight;
        yearLabel.setLayoutX(yearLabelX);
        yearLabel.setLayoutY(yearLabelY);

        // Arrow buttons positioning
        double arrowSpacing = 40;
        leftArrow.setLayoutX(yearLabelX - arrowSpacing - leftArrow.prefWidth(-1));
        leftArrow.setLayoutY(yearLabelY + (yearLabel.prefHeight(-1) - leftArrow.prefHeight(-1)) / 2);
        
        rightArrow.setLayoutX(yearLabelX + yearLabel.prefWidth(-1) + arrowSpacing);
        rightArrow.setLayoutY(leftArrow.getLayoutY());

        // Stop button positioning
        stopButton.setLayoutX(width - stopButton.prefWidth(-1) - edgeMargin);
        stopButton.setLayoutY(edgeMargin);
        
        // Month buttons grid
        int columns = 4;
        double horizontalSpacing = 20;
        double verticalSpacing = 15;
        double btnWidth = (width - (columns + 1) * horizontalSpacing) / columns;
        
        double gridStartX = horizontalSpacing;
        double gridStartY = yearLabel.getLayoutY() + yearLabel.prefHeight(-1) + 40;

        for (int i = 0; i < 12; i++) {
            int row = i / columns;
            int col = i % columns;
            
            monthButtons[i].setPrefSize(btnWidth, monthButtonHeight);
            monthButtons[i].setLayoutX(gridStartX + col * (btnWidth + horizontalSpacing));
            monthButtons[i].setLayoutY(gridStartY + row * (monthButtonHeight + verticalSpacing));
            
            // Scale font size based on button width
            double fontSize = Math.min(monthButtonFontSize, btnWidth * 0.12);
            monthButtons[i].setFont(Font.font(fontSize));
        }
    }

    private Button createStyledButton(String text, String color, double prefWidth) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius) +
            "-fx-padding: 8 12 8 12;"
        );
        
        if (prefWidth > 0) {
            btn.setPrefWidth(prefWidth);
            btn.setPrefHeight(prefWidth/2);
        }
        
        // Add hover effects
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: derive(" + color + ", -20%); " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius) +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius)
        ));
        
        return btn;
    }
    
    private Button createStyledArrowButton(String text, String color, double prefWidth) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius) +
            "-fx-padding: 8 12 8 12;"
        );
        
        if (prefWidth > 0) {
            btn.setPrefWidth(prefWidth);
            btn.setPrefHeight(prefWidth);
        }
        
        // Add hover effects
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: derive(" + color + ", -20%); " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius) +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: " + color + "; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius)
        ));
        
        return btn;
    }

    private void addMonthButtonHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #455A64; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius) +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #607D8B; " +
            String.format("-fx-background-radius: %.1fpx; ", buttonCornerRadius)
        ));
    }

    // Getters
    public Scene getScene() {
        return new Scene(root);
    }

    public Pane getView() { return root; }
    public Label getYearLabel() { return yearLabel; }
    public Button getLeftArrow() { return leftArrow; }
    public Button getRightArrow() { return rightArrow; }
    public Button getStopButton() { return stopButton; }
    public Button[] getMonthButtons() { return monthButtons; }

    // Size setters
    public void setWidth(int width) { 
        this.width = width - 15; 
        updateLayout();
    }
    public void setHeight(int height) { 
        this.height = height; 
        updateLayout();
    }
}