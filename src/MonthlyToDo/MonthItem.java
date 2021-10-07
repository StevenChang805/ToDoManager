package MonthlyToDo;

import base.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class MonthItem extends Item {
    private DataCollector dc;
    private VBox vBox;
    private VBox itemBox;
    private Label calendarLbl;
    private final Font mainFont = new Font("Helvetica", 13);

    public MonthItem(int id, int type, String name, LocalDate date, String desc, LocalDateTime startTime, LocalDateTime endTime, int complete) {
        super(id,
                type,
                date,
                name,
                desc,
                startTime,
                endTime,
                complete);
        dc = new DataCollector();
        calendarLbl = new Label();
        calendarLbl.setFont(mainFont);
        calendarLbl.setMaxWidth(Double.MAX_VALUE);
        calendarLbl.cursorProperty().set(Cursor.OPEN_HAND);

        calendarLbl.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                calendarLbl.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        calendarLbl.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                calendarLbl.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
    }

    public void displayOnCalendar(GridPane gridPane) {
        calendarLbl.setText(getName());

        calendarLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../base/ShowItem.fxml"));
                Stage stage = getStage(loader);
                ShowItemController sic = loader.<ShowItemController>getController();
                stage.setResizable(false);

                sic.setId(getId());
                sic.setType(getType());
                sic.setName(getName());
                sic.setDate(getDate());
                sic.setDesc(getDesc());
                sic.setStartTime(getStartTimeString());
                sic.setEndTime(getEndTimeString());
                sic.display();

                stage.showAndWait();

                if (sic.getSave()) {
                    setName(sic.getName());
                    calendarLbl.setText(getName());
                    setDescription(sic.getDesc());
                    setDate(sic.getDate());
                    setStartTime(sic.getStartTime());
                    setEndTime(sic.getEndTime());
                    setComplete(sic.getComplete());
                }
                if (sic.getDelete()) {
                    itemBox.getChildren().remove(calendarLbl);
                }
            }
        });

        int[] indexes = dayToPosn(getDate());
        int row = indexes[0];
        int col = indexes[1];

        StackPane pane = (StackPane) getNodeFromGridPane(gridPane, col, row);
        if (pane != null) {
            vBox = (VBox) pane.getChildren().get(0);
            itemBox = (VBox) vBox.getChildren().get(1);
            itemBox.getChildren().add(calendarLbl);
        }
    }

    public void displayOnTaskList(VBox taskList) {

    }

    public int[] dayToPosn (LocalDate date) {
        int dayOfWeek = date.withDayOfMonth(1).getDayOfWeek().getValue();
        int numDays = YearMonth.of(date.getYear(), date.getMonth().getValue()).lengthOfMonth();
        int modifier = dayOfWeek + 5;
        int today = date.getDayOfMonth();

        int rowIndex = (today + modifier) / 7 + 1;
        int colIndex = (dayOfWeek + today - 2) % 7;

        return new int[]{rowIndex, colIndex};
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }
        }
        return null;
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