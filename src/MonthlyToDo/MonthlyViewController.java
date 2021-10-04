package MonthlyToDo;

import base.DataCollector;
import base.Main;
import base.NewItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @FXML
    public Button prevBtn;
    @FXML
    public Button nextBtn;

    private final Font mainFont = new Font("Helvetica", 13);
    private LocalDate curDate;
    private int gridPaneStart;
    private DataCollector dc;
    private MonthItem[] monthData;

    @FXML
    private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../base/AddNewItem.fxml"));
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

    private void resetGrid() {
        clearGridPane();
        monthData = getMonthData();
        setupGrid(curDate);
    }

    public void initialize(URL url, ResourceBundle rb) {
        // initialize label
        curDate = LocalDate.now();
        dc = new DataCollector();
        monthData = getMonthData();
        setLblMonth(curDate);
        centerButtons();
        centerLabels();
        gridPaneStart = gridPane.getChildren().size();
        setupGrid(curDate);
    }

    public MonthItem[] getMonthData() {
        ArrayList<MonthItem> itemList = new ArrayList<>();

        // setting start and end dates to the first & last days of the month for the data collector
        LocalDate start = curDate.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        // try to get data from database
        ResultSet itemData = dc.getItems(start, end);

        // loop through ResultSet and append name & date to ArrayLists
        try {
            while (itemData.next()) {
                // extract item name & date
                String itemName = itemData.getString("name");
                Date date = itemData.getDate("date");
                LocalDate itemDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                // update ArrayLists
                MonthItem item = new MonthItem(itemName, itemDate);
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // store ArrayList values in array and return it
        MonthItem[] items = new MonthItem[itemList.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = itemList.get(i);
        }
        return items;
    } // end of method getData()

    public void clearGridPane() {
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
            int colIndex = (dayOfWeek-1)%7;
            int rowIndex = (i+modifier)/7+1;
            StackPane curPane = makeDayPane(i);
            gridPane.add(curPane, colIndex, rowIndex);
            dayOfWeek = dayOfWeek % 7 + 1;
        }

        displayItems();
    }

    public void displayItems() {
        for (int i = 0; i < monthData.length; i++) {
            monthData[i].display(gridPane);
        }
    }

    public StackPane makeDayPane(int day) {
        StackPane pane = new StackPane();
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
