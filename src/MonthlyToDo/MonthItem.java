package MonthlyToDo;

import base.Item;
import base.Text;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.YearMonth;

public class MonthItem extends Item {
    private final Font mainFont = new Font("Helvetica", 13);

    public MonthItem(String itemName, LocalDate itemDate) {
        super(itemDate, new Text(itemName, new Font("Helvetica", 13)), new Text("", new Font("Helvetica", 13)));

    }

    public void display(GridPane gridPane) {
        Label lbl = new Label();
        lbl.setFont(mainFont);
        lbl.setTextAlignment(TextAlignment.RIGHT);
        lbl.setText(getName());

        lbl.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

        lbl.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lbl.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        lbl.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lbl.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        int[] indexes = dayToPosn(getDate());
        int row = indexes[0];
        int col = indexes[1];

        StackPane pane = (StackPane) getNodeFromGridPane(gridPane, col, row);
        if (pane != null) {
            VBox vBox = (VBox) pane.getChildren().get(0);
            VBox itemBox = (VBox) vBox.getChildren().get(1);
            itemBox.getChildren().add(lbl);
        }
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


}