package base;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class NewItemController implements Initializable {

    @FXML
    private Button btnSave;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descField;

    @FXML
    private Label startTime;
    @FXML
    private Label endTime;

    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    @FXML
    private DatePicker dP;

    @FXML
    private Label lblWarning;

    private int itemType = -1;
    private DataCollector dc;

    private Stage stage;

    public void initialize(URL url, ResourceBundle rb) {
        choiceBox.getItems().addAll("Task", "Schedule Item");
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.equals("Schedule Item")) {
                    allowTimeEntry(true);
                    itemType = 1;
                } else {
                    allowTimeEntry(false);
                    itemType = 0;
                }
            }
        });
        dc = new DataCollector();
        lblWarning.setTextAlignment(TextAlignment.RIGHT);
        lblWarning.setTextFill(Color.RED);
        allowTimeEntry(false);
    }

    public void allowTimeEntry(boolean b) {
        startTime.setVisible(b);
        endTime.setVisible(b);
        startTimeField.setVisible(b);
        endTimeField.setVisible(b);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getDesc() {
        return descField.getText();
    }

    public LocalDate getDate() {
        return dP.getValue();
    }

    public LocalDateTime getStartTime() {
        String t = startTimeField.getText();
        return stringToLocalDateTime(t);
    }

    public LocalDateTime getEndTime() {
        String t = endTimeField.getText();
        return stringToLocalDateTime(t);
    }

    public LocalDateTime stringToLocalDateTime(String s) {
        int colPosn = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ':') {
                colPosn = i;
            }
        }
        if (colPosn == -1 || s.length() < 4 || (s.length() == 4 && colPosn == 2)) {
            return null;
        }

        int multiplier = 1;
        int hours = 0;
        for (int i = colPosn-1; i >= 0; i--) {
            hours += (s.charAt(i)-'0') * multiplier;
            multiplier *= 10;
        }

        multiplier = 10;
        int minutes = 0;
        for (int i = colPosn+1; i < colPosn+3; i++) {
            minutes += (s.charAt(i)-'0') * multiplier;
            multiplier /= 10;
        }

        LocalDate date = getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dateISO = date.format(formatter);
        String hourISO = (hours < 10) ? "0"+hours : Integer.toString(hours);
        String minuteISO = (minutes < 10) ? "0"+minutes : Integer.toString(minutes);

        String iso = dateISO + "T" + hourISO + ":" + minuteISO + ":00.000";

        System.out.println(iso);
        LocalDateTime dateTime = LocalDateTime.parse(iso);
        return dateTime;
    }

    @FXML
    private void save() {
        if (itemType == -1) {
            lblWarning.setText("Please select an item type.");
            return;
        }
        if (itemType == 0) {
            dc.addNewItem(0, getName(), getDesc(), getDate(), null, null, 0);
        } else if (itemType == 1) {
            if (getStartTime() == null || getEndTime() == null) {
                lblWarning.setText("Invalid time entered.");
                return;
            } else {
                dc.addNewItem(1, getName(), getDesc(), getDate(), getStartTime(), getEndTime(), 0);
            }
        }
        stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
