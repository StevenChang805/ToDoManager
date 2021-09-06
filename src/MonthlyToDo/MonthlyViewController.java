package MonthlyToDo;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    @FXML
    private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../base/AddNewItem.fxml"));
        Stage stage = getStage(loader);
        NewItemController nic = loader.<NewItemController>getController();
        nic.setTextField("Hi");

        stage.showAndWait();

        System.out.println(nic.getTextField());
    }

    @FXML
    private void prevMonth() {
        curDate = curDate.minusMonths(1);
        clearGridPane();
        setLblMonth(curDate);
        setupGrid(curDate);
    }

    @FXML
    private void nextMonth() {
        curDate = curDate.plusMonths(1);
        clearGridPane();
        setLblMonth(curDate);
        setupGrid(curDate);
    }

    public void initialize(URL url, ResourceBundle rb) {
        // initialize label
        curDate = LocalDate.now();
        setLblMonth(curDate);
        centerButtons();
        centerLabels();
        gridPaneStart = gridPane.getChildren().size();
        setupGrid(curDate);
    }

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
            Pane curPane = makeDayPane(i);
            gridPane.add(curPane, colIndex, rowIndex);
            curPane.setPrefWidth(100);
            dayOfWeek = dayOfWeek % 7 + 1;
        }
    }

    public Pane makeDayPane(int day) {
        Pane pane = new Pane();
        Label lbl = new Label();
        Font font = getFont();
        lbl.setFont(font);
        lbl.setPrefWidth(100);
        lbl.setBackground(new Background(new BackgroundFill(Color.CYAN, new CornerRadii(0), new Insets(0))));
        lbl.setText(""+day);
        lbl.setAlignment(Pos.TOP_CENTER);

        pane.getChildren().add(lbl);
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
            System.out.println("Fail");
            System.out.println(e);
            return null;
        }
        // Create the stage
        Stage inputStage = new Stage();
        // Sets the owner to being this window NOTE primaryStage is set up in StockManagement
        inputStage.initOwner(Main.primaryStage);
        // Add the Scene to the stage
        inputStage.setScene(newScene);

        return inputStage;
    }
}
