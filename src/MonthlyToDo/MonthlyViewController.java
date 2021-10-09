package MonthlyToDo;

import base.DataCollector;
import base.Main;
import base.NewItemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public class MonthlyViewController implements Initializable {
    @FXML public Label lblMonth;
    @FXML public Label lblMon;
    @FXML public Label lblTue;
    @FXML public Label lblWed;
    @FXML public Label lblThu;
    @FXML public Label lblFri;
    @FXML public Label lblSat;
    @FXML public Label lblSun;
    @FXML public Label lblTasks;

    @FXML public GridPane gridPane;
    @FXML public Pane prevBtnPane;
    @FXML public Pane nextBtnPane;
    @FXML public Pane monthPane;
    @FXML public Pane monPane;
    @FXML public Pane tuePane;
    @FXML public Pane wedPane;
    @FXML public Pane thuPane;
    @FXML public Pane friPane;
    @FXML public Pane satPane;
    @FXML public Pane sunPane;
    @FXML public Pane taskLblPane;

    @FXML public ListView<MonthItem> tasks;

    @FXML public Button prevBtn;
    @FXML public Button nextBtn;

    private final Font mainFont = new Font("Helvetica", 13);
    private LocalDate curDate;
    private int gridPaneStart;
    private DataCollector dc;
    private ObservableList<MonthItem> allItems;
    private ObservableList<MonthItem> allTasks;

    @FXML
    private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../base/NewItem.fxml"));
        Stage stage = getStage(loader);
        NewItemController nic = loader.<NewItemController>getController();
        stage.setResizable(false);

        stage.showAndWait();
        resetGrid();
    }

    @FXML
    private void prevMonth() {
        curDate = curDate.minusMonths(1);
        setLblMonth(curDate);
        resetGrid();
    }

    @FXML
    private void nextMonth() {
        curDate = curDate.plusMonths(1);
        setLblMonth(curDate);
        resetGrid();
    }

    public void resetGrid() {
        clearScreen();
        getMonthData();
        setupGrid(curDate);
    }

    public void removeItem(MonthItem item) {
        allItems.remove(item);
        if (item.getType() == 0) {
            allTasks.remove(item);
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        // initialize label
        curDate = LocalDate.now();
        dc = new DataCollector();
        allItems = FXCollections.observableArrayList();
        allTasks = FXCollections.observableArrayList();
        getMonthData();
        tasks.setItems(allTasks);
        tasks.setOnMouseClicked(mouseEvent -> tasks.getSelectionModel().getSelectedItem().onMouseClick());
        setLblMonth(curDate);
        centerButtons();
        centerLabels();
        gridPaneStart = gridPane.getChildren().size();
        setupGrid(curDate);
    }

    public void getMonthData() {
        allItems.clear();
        // setting start and end dates to the first & last days of the month for the data collector
        LocalDate start = curDate.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        // try to get data from database
        ResultSet itemData = dc.getItems(start, end);

        // loop through ResultSet and append name & date to ArrayLists
        try {
            while (itemData.next()) {
                // extract item name & date
                Date date = itemData.getDate("date");
                Time startTime = itemData.getTime("start_time");
                Time endTime = itemData.getTime("end_time");

                int itemId = itemData.getInt("id");
                int itemType = itemData.getInt("type");
                String itemName = itemData.getString("name");
                String itemDesc = itemData.getString("description");
                LocalDate itemDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDateTime itemStartTime = null;
                LocalDateTime itemEndTime = null;
                if (startTime != null) {
                    itemStartTime = new Timestamp(startTime.getTime()).toLocalDateTime();
                }
                if (endTime != null) {
                    itemEndTime = new Timestamp(endTime.getTime()).toLocalDateTime();
                }

                int complete = itemData.getInt("complete");

                // update ArrayLists
                MonthItem item = new MonthItem(itemId, itemType, itemName, itemDate, itemDesc, itemStartTime, itemEndTime, complete, this);
                allItems.add(item);
                if (item.getType() == 0) {
                    allTasks.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of method getData()

    public void clearScreen() {
        int gridPaneEnd = gridPane.getChildren().size();
        gridPane.getChildren().remove(gridPaneStart, gridPaneEnd);
    }

    public void setLblMonth(LocalDate date) {
        // extract month & year
        String month = date.getMonth().name();
        month = month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
        int year = date.getYear();
        // set month & year on label
        lblMonth.setText(month + " " + year);
    } // end of method setLblMonth

    public void centerButtons() {
        // fix previous month button to the center of the pane
        centerBtnToPane(prevBtn, prevBtnPane);
        // fix next month button to the center of the pane
        centerBtnToPane(nextBtn, nextBtnPane);
    } // end of method centerButtons

    public void centerLabels() {
        // fix month label to the center of the pane
        centerLblToPane(lblMonth, monthPane);
        // fix day of week labels to the center of their respective panes
        centerLblToPane(lblMon, monPane);
        centerLblToPane(lblTue, tuePane);
        centerLblToPane(lblWed, wedPane);
        centerLblToPane(lblThu, thuPane);
        centerLblToPane(lblFri, friPane);
        centerLblToPane(lblSat, satPane);
        centerLblToPane(lblSun, sunPane);
        // fix upcoming tasks label to its pane
        centerLblToPane(lblTasks, taskLblPane);
    }

    public void centerBtnToPane(Button btn, Pane pane) {
        btn.layoutXProperty().bind(pane.widthProperty().subtract(btn.widthProperty()).divide(2));
        btn.layoutYProperty().bind(pane.heightProperty().subtract(btn.heightProperty()).divide(2));
    }

    public void centerLblToPane(Label lbl, Pane pane) {
        lbl.layoutXProperty().bind(pane.widthProperty().subtract(lbl.widthProperty()).divide(2));
        lbl.layoutYProperty().bind(pane.heightProperty().subtract(lbl.heightProperty()).divide(2));
    }

    public void setupGrid(LocalDate date) {
        int dayOfWeek = date.withDayOfMonth(1).getDayOfWeek().getValue();
        int numDays = YearMonth.of(date.getYear(), date.getMonth().getValue()).lengthOfMonth();
        int modifier = dayOfWeek + 5;

        for (int i = 1; i <= numDays; i++) {
            int colIndex = (dayOfWeek - 1) % 7;
            int rowIndex = (i + modifier) / 7 + 1;
            StackPane curPane = makeDayPane(i);
            gridPane.add(curPane, colIndex, rowIndex);
            dayOfWeek = dayOfWeek % 7 + 1;
        }
        displayItemsOnCalendar();
    }

    public void displayItemsOnCalendar() {
        for (int i = 0; i < allItems.size(); i++) {
            allItems.get(i).displayOnCalendar(gridPane);
        }
    }

    public StackPane makeDayPane(int day) {
        StackPane pane = new StackPane();
        // top right bottom left
//        pane.setPadding(new Insets(0.0, 2.0, 0.0, 2.0));
        VBox vBox = new VBox();
        VBox itemBox = new VBox();
        Label lbl = new Label();
        Font font = getFont();
        lbl.setFont(font);

        lbl.setText(Integer.toString(day));
        itemBox.setAlignment(Pos.TOP_LEFT);
        itemBox.prefWidthProperty().bind(vBox.widthProperty());
        vBox.getChildren().add(lbl);
        vBox.getChildren().add(itemBox);
        vBox.setAlignment(Pos.TOP_RIGHT);
        pane.getChildren().add(vBox);

        return pane;
    }

    public Font getFont() {
        return mainFont;
    }

    public Stage getStage(FXMLLoader loader){
        // Loads the scene from the fxml file
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // Create the stage
        Stage inputStage = new Stage();

        inputStage.initOwner(Main.primaryStage);
        // Add the Scene to the stage
        inputStage.setScene(newScene);

        return inputStage;
    }
}
