package MonthlyToDo;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.YearMonth;

public class MonthItem {
    private String name;
    private LocalDate date;
    private final Font mainFont = new Font("Helvetica", 13);

    public MonthItem(String itemName, LocalDate itemDate) {
        name = itemName;
        date = itemDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void display(GridPane gridPane) {
        Label lbl = new Label();
        lbl.setFont(mainFont);
        lbl.setTextAlignment(TextAlignment.RIGHT);
        lbl.setText(name);

        int[] indexes = dayToPosn(date);
        int row = indexes[0];
        int col = indexes[1];

        Pane pane = (Pane) getNodeFromGridPane(gridPane, col, row);
        System.out.println(col + ", " + row);
        if (pane != null) {
            VBox vBox = (VBox) pane.getChildren().get(0);
            vBox.getChildren().add(lbl);
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