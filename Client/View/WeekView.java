package Client.View;

import Client.Model.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeekView {
    private Pane root;
    private Label titleLabel;
    private Button backButton, addButton, deleteButton, prevWeekButton, nextWeekButton;
    private Label[] dayLabels = new Label[7];
    private Button[] dateButtons = new Button[7];
    private ScrollPane[] dayScrollPanes = new ScrollPane[7];
    private VBox[] eventContainers = new VBox[7];
    private int height;
    private int width;

    // Spacing and styling variables
    private double verticalSpacing = 10;
    private double horizontalSpacing = 5;
    private double spacingBetweenDays = 5;
    private double titleBarHeight = 40;
    private double headerHeight = 30;
    private double controlButtonWidth = 80;
    private double controlButtonHeight = 30;
    private double dateButtonHeightRatio = 0.15;
    private double scrollPaneCornerRadius = 5;
    private double cellBorderWidth = 1;
    private double edgeMargin = 10;
    
    // Event box variables (now relative to cell width)
    private double eventBoxHeight = 80;  // Fixed height
    private double eventBoxSpacing = 5;
    private double eventBoxWidthRatio = 0.95; // Percentage of cell width

    public WeekView(int height, int width) {
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
        
        // Control buttons with fixed size
        backButton = createStyledButton("← Back", "#2196F3");
        addButton = createStyledButton("＋ Add", "#4CAF50");
        deleteButton = createStyledButton("🗑 Delete", "#f44336");
        prevWeekButton = createStyledButton("◀ Prev", "#9E9E9E");
        nextWeekButton = createStyledButton("Next ▶", "#9E9E9E");
        
        // Set fixed size for buttons to prevent size changes
        backButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        addButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        deleteButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        prevWeekButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        nextWeekButton.setPrefSize(controlButtonWidth, controlButtonHeight);
        
        // Day headers with improved styling
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            dayLabels[i] = new Label(dayNames[i]);
            dayLabels[i].setFont(Font.font(12));
            dayLabels[i].setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
            dayLabels[i].setAlignment(Pos.CENTER);
            root.getChildren().add(dayLabels[i]);
        }
        
        // Date cells and scroll panes
        for (int i = 0; i < 7; i++) {
            dateButtons[i] = new Button();
            dateButtons[i].setStyle(
                "-fx-background-color: #ffffff; " +
                "-fx-text-fill: #333; " +
                "-fx-border-color: #e0e0e0; " +
                String.format("-fx-border-width: %.1fpx; ", cellBorderWidth) +
                String.format("-fx-border-radius: %.1fpx; ", scrollPaneCornerRadius) +
                "-fx-font-weight: bold;"
            );
            
            eventContainers[i] = new VBox(eventBoxSpacing);
            eventContainers[i].setStyle("-fx-padding: 5;");
            
            dayScrollPanes[i] = new ScrollPane(eventContainers[i]);
            dayScrollPanes[i].setStyle(
                "-fx-background: #ffffff; " +
                "-fx-border-color: #e0e0e0; " +
                String.format("-fx-border-width: %.1fpx; ", cellBorderWidth) +
                String.format("-fx-background-radius: %.1fpx; ", scrollPaneCornerRadius) +
                String.format("-fx-border-radius: %.1fpx; ", scrollPaneCornerRadius)
            );
            
            root.getChildren().addAll(dateButtons[i], dayScrollPanes[i]);
        }
        
        root.getChildren().addAll(titleLabel, backButton, addButton, deleteButton, prevWeekButton, nextWeekButton);
    }

    public void updateLayout(LocalDate weekStart) {
        // Update title
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        titleLabel.setText("Week of " + weekStart.format(formatter));
        
        root.applyCss();
        root.layout();
        
        // Position components with edge margins
        double buttonX = width - (controlButtonWidth * 5 + horizontalSpacing * 4) - edgeMargin;

        // Title and control buttons
        titleLabel.setLayoutX(edgeMargin);
        titleLabel.setLayoutY(titleBarHeight / 2 - titleLabel.prefHeight(-1) / 2);

        backButton.setLayoutX(buttonX);
        backButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);

        addButton.setLayoutX(buttonX + controlButtonWidth + horizontalSpacing);
        addButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);

        deleteButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 2);
        deleteButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);

        prevWeekButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 3);
        prevWeekButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);

        nextWeekButton.setLayoutX(buttonX + (controlButtonWidth + horizontalSpacing) * 4);
        nextWeekButton.setLayoutY(titleBarHeight / 2 - controlButtonHeight / 2);

        // Calculate available width accounting for margins and spacing between days
        double totalSpacing = (6 * spacingBetweenDays) + (2 * edgeMargin);
        double cellWidth = (width - totalSpacing) / 7;
        
        // Day headers - with edge margins and spacing between days
        for (int i = 0; i < 7; i++) {
            dayLabels[i].setPrefWidth(cellWidth);
            dayLabels[i].setLayoutX(edgeMargin + i * (cellWidth + spacingBetweenDays));
            dayLabels[i].setLayoutY(titleBarHeight + 5);
        }

        // Date cells and scroll panes - with proper spacing from bottom
        double availHeight = height - titleBarHeight - headerHeight - edgeMargin;
        double cellBtnHeight = availHeight * dateButtonHeightRatio;
        double scrollPaneHeight = availHeight * (1 - dateButtonHeightRatio) - 5;
        double cellY = titleBarHeight + headerHeight + 5;

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            dateButtons[i].setText(String.valueOf(date.getDayOfMonth()));
            dateButtons[i].setLayoutX(edgeMargin + i * (cellWidth + spacingBetweenDays));
            dateButtons[i].setLayoutY(cellY);
            dateButtons[i].setPrefSize(cellWidth, cellBtnHeight);

            dayScrollPanes[i].setLayoutX(edgeMargin + i * (cellWidth + spacingBetweenDays));
            dayScrollPanes[i].setLayoutY(cellY + cellBtnHeight);
            dayScrollPanes[i].setPrefSize(cellWidth, scrollPaneHeight);
            
            addDateButtonHoverEffect(dateButtons[i]);
            
            // Update event cards width when layout changes
            updateEventCardsWidth(cellWidth);
        }
    }

    private void updateEventCardsWidth(double cellWidth) {
    double newEventBoxWidth = cellWidth * eventBoxWidthRatio - 25;
    double referenceWidth = 200; // Original reference width for font scaling
    
    // Define font size boundaries
    double minFontSize = 8.0;
    double maxFontSize = 16.0;
    
    for (VBox container : eventContainers) {
        for (javafx.scene.Node node : container.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                card.setPrefWidth(newEventBoxWidth);
                
                // Adjust font sizes for labels
                for (javafx.scene.Node child : card.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        
                        // Calculate scaled font size with boundaries
                            System.out.println(newEventBoxWidth);
                        double newSize = 8*newEventBoxWidth/75 ;
                        System.out.println(newSize);
                        newSize = Math.max(minFontSize, Math.min(newSize, maxFontSize));
                        
                        label.setStyle(String.format("-fx-font-size: %.1f;", newSize) + 
                            label.getStyle().replaceAll("-fx-font-size:.*?;", ""));
                    }
                }
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

    private void addDateButtonHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #e3f2fd; " +
            "-fx-text-fill: #1976d2; " +
            "-fx-border-color: #90caf9; " +
            String.format("-fx-border-width: %.1fpx; ", cellBorderWidth + 1) +
            String.format("-fx-border-radius: %.1fpx; ", scrollPaneCornerRadius) +
            "-fx-font-weight: bold;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #333; " +
            "-fx-border-color: #e0e0e0; " +
            String.format("-fx-border-width: %.1fpx; ", cellBorderWidth) +
            String.format("-fx-border-radius: %.1fpx; ", scrollPaneCornerRadius) +
            "-fx-font-weight: bold;"
        ));
    }

    public VBox createEventCard(Event event, double cellWidth) {
        VBox card = new VBox(5);
        card.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-padding: 10; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );
        card.setPrefSize(cellWidth * eventBoxWidthRatio, eventBoxHeight);

        // Event type label
        Label typeLabel = new Label(event.getType().toUpperCase());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // Time range label
        Label timeLabel = new Label(event.getFormattedTime());
        timeLabel.setStyle("-fx-text-fill: #636e72; -fx-font-size: 12;");

        // Module label
        Label moduleLabel = new Label(event.getModule());
        moduleLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");

        card.getChildren().addAll(typeLabel, timeLabel, moduleLabel);
        
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

    // Getters and setters
    public double getEdgeMargin() { return edgeMargin; }
    public void setEdgeMargin(double edgeMargin) { this.edgeMargin = edgeMargin; }

    public double getVerticalSpacing() { return verticalSpacing; }
    public void setVerticalSpacing(double verticalSpacing) { 
        this.verticalSpacing = verticalSpacing; 
    }

    public double getHorizontalSpacing() { return horizontalSpacing; }
    public void setHorizontalSpacing(double horizontalSpacing) { 
        this.horizontalSpacing = horizontalSpacing; 
    }

    public double getTitleBarHeight() { return titleBarHeight; }
    public void setTitleBarHeight(double titleBarHeight) { 
        this.titleBarHeight = titleBarHeight; 
    }

    public double getHeaderHeight() { return headerHeight; }
    public void setHeaderHeight(double headerHeight) { 
        this.headerHeight = headerHeight; 
    }

    public double getControlButtonWidth() { return controlButtonWidth; }
    public void setControlButtonWidth(double controlButtonWidth) { 
        this.controlButtonWidth = controlButtonWidth; 
    }

    public double getControlButtonHeight() { return controlButtonHeight; }
    public void setControlButtonHeight(double controlButtonHeight) { 
        this.controlButtonHeight = controlButtonHeight; 
    }

    public double getDateButtonHeightRatio() { return dateButtonHeightRatio; }
    public void setDateButtonHeightRatio(double dateButtonHeightRatio) { 
        this.dateButtonHeightRatio = dateButtonHeightRatio; 
    }

    public double getScrollPaneCornerRadius() { return scrollPaneCornerRadius; }
    public void setScrollPaneCornerRadius(double scrollPaneCornerRadius) { 
        this.scrollPaneCornerRadius = scrollPaneCornerRadius; 
    }

    public double getCellBorderWidth() { return cellBorderWidth; }
    public void setCellBorderWidth(double cellBorderWidth) { 
        this.cellBorderWidth = cellBorderWidth; 
    }

    public double getEventBoxHeight() { return eventBoxHeight; }
    public void setEventBoxHeight(double eventBoxHeight) { 
        this.eventBoxHeight = eventBoxHeight; 
    }

    public double getEventBoxSpacing() { return eventBoxSpacing; }
    public void setEventBoxSpacing(double eventBoxSpacing) { 
        this.eventBoxSpacing = eventBoxSpacing; 
    }

    public double getEventBoxWidthRatio() { return eventBoxWidthRatio; }
    public void setEventBoxWidthRatio(double eventBoxWidthRatio) { 
        this.eventBoxWidthRatio = eventBoxWidthRatio; 
    }

    // Original getters
    public Pane getView() { return root; }
    public Button getBackButton() { return backButton; }
    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getPrevWeekButton() { return prevWeekButton; }
    public Button getNextWeekButton() { return nextWeekButton; }
    public Button[] getDateButtons() { return dateButtons; }
    public VBox[] getEventContainers() { return eventContainers; }
    public void setWidth(int width) { this.width = width - 20; }
    public void setHeight(int height) { this.height = height - 45; }
}