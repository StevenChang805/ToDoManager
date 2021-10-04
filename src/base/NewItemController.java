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
import javafx.stage.Stage;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private int itemType;
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
        int hours = 0;
        int minutes = 0;
        boolean multiply = true;
        boolean changed = false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ':') {
                changed = true;
                i++;
            }
            if (!changed) {
                if (multiply) {
                    hours += (s.charAt(i)-'0') * 10;
                    multiply = false;
                } else {
                    hours += (s.charAt(i)-'0');
                    multiply = true;
                }
            } else {
                if (multiply) {
                    minutes += (s.charAt(i)-'0') * 10;
                    multiply = false;
                } else {
                    minutes += (s.charAt(i) - '0');
                    multiply = true;
                }
            }
        }

        LocalDate date = getDate();
        String dateISO = date.toString();
        String hourISO = Integer.toString(hours);
        if (hourISO.length() == 1) {
            hourISO = "0"+hourISO;
        }
        String minuteISO = Integer.toString(minutes);

        if (minuteISO.length() == 1) {
            minuteISO = "0"+minuteISO;
        }
        String isoFormat = dateISO + "T" + hourISO + ":" + minuteISO + ":00.000";
        LocalDateTime time = LocalDateTime.parse(isoFormat);
        System.out.println(isoFormat);
        return time;
    }

    @FXML
    private void save() {
        if (itemType == 0) {
            dc.addNewItem(1, getName(), getDesc(), getDate(), null, null, 0);
        } else if (itemType == 1) {
            dc.addNewItem(1, getName(), getDesc(), getDate(), getStartTime(), getEndTime(), 0);
        }
        stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
