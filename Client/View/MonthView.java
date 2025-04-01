package Client.View;

import Client.Controller.MonthController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.YearMonth;
import javafx.scene.Scene;

public class MonthView {
    private Pane root;
    private Label titleLabel;
    private Button backButton, addButton, deleteButton, prevMonthButton, nextMonthButton;
    private Label[] dayLabels = new Label[7];
    private Button[] rowButtons;
    private Button[] dateButtons;
    private Label[] noteLabels;
    private int height;
    private int width;
    
    // Spacing and styling variables
    private double verticalSpacing = 5;
    private double horizontalSpacing = 5;
    private double spacingBetweenButtonAndLabel = 8;
    private double cellCornerRadius = 10;
    private double cellBorderWidth = 1;
    private double titleBarHeight = 40;
    private double headerHeight = 30;
    private double rowLabelWidth = 40;
    private double controlButtonWidth = 80;
    private double controlButtonHeight = 35;

    public MonthView(int height, int width) {
        this.width = width;
        this.height = height;
        initializeUI();
    }

    private void initializeUI() {
        root = new Pane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Title label
        titleLabel = new Label();
        titleLabel.setFont(Font.font(20));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Control buttons with consistent styling
        backButton = createStyledButton("← Back", "#2196F3");
        addButton = createStyledButton("＋ Add", "#4CAF50");
        deleteButton = createStyledButton("🗑 Delete", "#f44336");
        prevMonthButton = createStyledButton("◀ Prev", "#9E9E9E");
        nextMonthButton = createStyledButton("Next ▶", "#9E9E9E");
        
        // Day headers with improved styling
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            dayLabels[i] = new Label(dayNames[i]);
            dayLabels[i].setFont(Font.font(12));
            dayLabels[i].setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
            dayLabels[i].setAlignment(javafx.geometry.Pos.CENTER);
            root.getChildren().add(dayLabels[i]);
        }
        
        root.getChildren().addAll(titleLabel, backButton, addButton, deleteButton, prevMonthButton, nextMonthButton);
    }

    public void createCalendarGrid(YearMonth yearMonth, LocalDate firstCellDate, int rowCount) {
        // Clear existing grid
        if (dateButtons != null) {
            root.getChildren().removeAll(rowButtons);
            root.getChildren().removeAll(dateButtons);
            root.getChildren().removeAll(noteLabels);
        }
        
        // Create week row buttons with proper hover effects
        rowButtons = new Button[rowCount];
for (int row = 0; row < rowCount; row++) {
    final int finalRow = row; // Create a final copy of the loop variable
    rowButtons[row] = new Button(String.valueOf(row + 1));
    rowButtons[row].setStyle(
        "-fx-font-size: 12; " +
        "-fx-font-weight: bold; " +
        "-fx-text-fill: white; " +
        "-fx-background-color: #607D8B; " +
        "-fx-background-radius: 5px;"
    );
    
    // Add hover effects using the final variable
    rowButtons[row].setOnMouseEntered(e -> rowButtons[finalRow].setStyle(
        "-fx-font-size: 12; " +
        "-fx-font-weight: bold; " +
        "-fx-text-fill: white; " +
        "-fx-background-color: #455A64; " +
        "-fx-background-radius: 5px; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
    ));
    
    rowButtons[row].setOnMouseExited(e -> rowButtons[finalRow].setStyle(
        "-fx-font-size: 12; " +
        "-fx-font-weight: bold; " +
        "-fx-text-fill: white; " +
        "-fx-background-color: #607D8B; " +
        "-fx-background-radius: 5px;"
    ));
    
    root.getChildren().add(rowButtons[row]);
}
        
        // Create date cells
        int totalCells = rowCount * 7;
        dateButtons = new Button[totalCells];
        noteLabels = new Label[totalCells];
        
        for (int i = 0; i < totalCells; i++) {
            dateButtons[i] = new Button();
            noteLabels[i] = new Label();
            noteLabels[i].setStyle("-fx-wrap-text: true; -fx-font-size: 11; -fx-text-fill: #666;");
            
            LocalDate date = firstCellDate.plusDays(i);
            dateButtons[i].setText(String.valueOf(date.getDayOfMonth()));
            
            boolean isCurrentMonth = MonthController.isDateInCurrentMonth(date, yearMonth);
            String initialStyle = getCellStyle(isCurrentMonth);
            dateButtons[i].setStyle(initialStyle);
            dateButtons[i].setUserData(initialStyle);
            
            addHoverEffect(dateButtons[i], isCurrentMonth);
            root.getChildren().addAll(dateButtons[i], noteLabels[i]);
        }
       
        updateLayout(rowCount);
    }

    private String getCellStyle(boolean isCurrentMonth) {
        if (isCurrentMonth) {
            return String.format(
                "-fx-background-color: #ffffff; " +
                "-fx-text-fill: #333; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: %.1fpx; " +
                "-fx-background-radius: %.1fpx; " +
                "-fx-border-radius: %.1fpx; " +
                "-fx-font-weight: bold;",
                cellBorderWidth, cellCornerRadius, cellCornerRadius
            );
        } else {
            return String.format(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #999; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: %.1fpx; " +
                "-fx-background-radius: %.1fpx; " +
                "-fx-border-radius: %.1fpx;",
                cellBorderWidth, cellCornerRadius, cellCornerRadius
            );
        }
    }

    public void updateLayout(int rowCount) {
        root.applyCss();
        root.layout();
        
        // Title and control buttons layout
        titleLabel.setLayoutX(15);
        titleLabel.setLayoutY(titleBarHeight / 2 - titleLabel.prefHeight(-1) / 2);

        // Position control buttons
        double buttonX = width - (controlButtonWidth * 5 + horizontalSpacing * 4) - 40;
        backButton.setLayoutX(buttonX);
        backButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        backButton.setPrefSize(controlButtonWidth, controlButtonHeight);

        addButton.setLayoutX(buttonX + controlButtonWidth + horizontalSpacing);
        addButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        addButton.setPrefSize(controlButtonWidth, controlButtonHeight);

        deleteButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 2);
        deleteButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        deleteButton.setPrefSize(controlButtonWidth, controlButtonHeight);

        prevMonthButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 3);
        prevMonthButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        prevMonthButton.setPrefSize(controlButtonWidth, controlButtonHeight);

        nextMonthButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 4);
        nextMonthButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);
        nextMonthButton.setPrefSize(controlButtonWidth, controlButtonHeight);

        // Calculate cell dimensions with spacing
        double availWidth = width - rowLabelWidth - 20 - (6 * horizontalSpacing);
        double availHeight = height - titleBarHeight - 50 - headerHeight - ((rowCount - 1) * verticalSpacing); 
        double cellWidth = (availWidth / 7);
        double cellHeight = (availHeight / rowCount);

        // Day headers positioning
        for (int col = 0; col < 7; col++) {
            dayLabels[col].setLayoutX(rowLabelWidth + col * (cellWidth + horizontalSpacing));
            dayLabels[col].setLayoutY(titleBarHeight + 5);
            dayLabels[col].setPrefWidth(cellWidth);
        }

        // Week row buttons positioning
        if (rowButtons != null) {
            for (int row = 0; row < rowCount; row++) {
                Button rowButton = rowButtons[row];
                rowButton.setLayoutX(5);
                rowButton.setPrefWidth(rowLabelWidth - 10);
                rowButton.setPrefHeight(cellHeight * 0.8);
                rowButton.setLayoutY(titleBarHeight + headerHeight + row * (cellHeight + verticalSpacing) + 
                                   (cellHeight - rowButton.getPrefHeight()) / 2);
            }
        }

        // Date cells and note labels
        if (dateButtons != null && noteLabels != null) {
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < 7; col++) {
                    int index = row * 7 + col;
                    double x = rowLabelWidth + col * (cellWidth + horizontalSpacing);
                    double y = titleBarHeight + headerHeight + row * (cellHeight + verticalSpacing);

                    dateButtons[index].setLayoutX(x);
                    dateButtons[index].setLayoutY(y);
                    dateButtons[index].setPrefSize(cellWidth, cellHeight * 0.4);                    

                    noteLabels[index].setLayoutX(x + 1);
                    noteLabels[index].setLayoutY(y + cellHeight * 0.4 + spacingBetweenButtonAndLabel);
                    noteLabels[index].setPrefSize(cellWidth - 2, cellHeight * 0.6 - spacingBetweenButtonAndLabel - 2);
                }
            }
        }
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
        
        // Add hover effects
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

    private void addHoverEffect(Button button, boolean isCurrentMonth) {
        String initialStyle = (String) button.getUserData();
        
        button.setOnMouseEntered(e -> {
            if (isCurrentMonth) {
                button.setStyle(
                    "-fx-background-color: #e3f2fd; " +
                    "-fx-text-fill: #1976d2; " +
                    "-fx-border-color: #90caf9; " +
                    String.format("-fx-border-width: %.1fpx; ", cellBorderWidth + 1) +
                    String.format("-fx-background-radius: %.1fpx; ", cellCornerRadius) +
                    String.format("-fx-border-radius: %.1fpx; ", cellCornerRadius) +
                    "-fx-effect: dropshadow(gaussian, rgba(25,118,210,0.2), 3, 0, 0, 1);"
                );
            } else {
                button.setStyle(
                    "-fx-background-color: #eceff1; " +
                    "-fx-text-fill: #78909c; " +
                    "-fx-border-color: #cfd8dc; " +
                    String.format("-fx-border-width: %.1fpx; ", cellBorderWidth + 1) +
                    String.format("-fx-background-radius: %.1fpx; ", cellCornerRadius) +
                    String.format("-fx-border-radius: %.1fpx; ", cellCornerRadius)
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(initialStyle);
        });
    }
    
    // Getters and setters
    public double getVerticalSpacing() { return verticalSpacing; }
    public void setVerticalSpacing(double verticalSpacing) { 
        this.verticalSpacing = verticalSpacing; 
    }

    public double getHorizontalSpacing() { return horizontalSpacing; }
    public void setHorizontalSpacing(double horizontalSpacing) { 
        this.horizontalSpacing = horizontalSpacing; 
    }

    public double getSpacingBetweenButtonAndLabel() { return spacingBetweenButtonAndLabel; }
    public void setSpacingBetweenButtonAndLabel(double spacing) { 
        this.spacingBetweenButtonAndLabel = spacing; 
    }

    public double getCellCornerRadius() { return cellCornerRadius; }
    public void setCellCornerRadius(double radius) { 
        this.cellCornerRadius = radius; 
    }

    public double getCellBorderWidth() { return cellBorderWidth; }
    public void setCellBorderWidth(double width) { 
        this.cellBorderWidth = width; 
    }

    public Scene getScene() { return new Scene(root); }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    
    // Original getters
    public Pane getView() { return root; }
    public Label getTitleLabel() { return titleLabel; }
    public Button getBackButton() { return backButton; }
    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getPrevMonthButton() { return prevMonthButton; }
    public Button getNextMonthButton() { return nextMonthButton; }
    public Button[] getRowButtons() { return rowButtons; }
    public Button[] getDateButtons() { return dateButtons; }
    public Label[] getNoteLabels() { return noteLabels; }
}