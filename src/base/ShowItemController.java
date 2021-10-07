package base;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ShowItemController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label nameLbl;
    @FXML
    private TextField nameField;

    @FXML
    private Label dateLbl;
    @FXML
    private DatePicker datePicker;

    @FXML
    private Label descLbl;
    @FXML
    private TextArea descField;

    @FXML
    private Label startTimeLbl;
    @FXML
    private TextField startTimeField;

    @FXML
    private Label endTimeLbl;
    @FXML
    private TextField endTimeField;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;

    @FXML
    private CheckBox checkBox;

    private Stage stage;
    private DataCollector dc;
    private int id;
    private int type;
    private boolean save = false;
    private boolean delete = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dc = new DataCollector();
    }

    public void display() {
        if (type == 0) {
            startTimeLbl.setText("Completion Status");
            startTimeField.setVisible(false);
            endTimeLbl.setVisible(false);
            endTimeField.setVisible(false);
            startTimeField.layoutYProperty().set(300.0);
            endTimeLbl.layoutYProperty().set(300.0);
            endTimeField.layoutYProperty().set(300.0);
            btnSave.layoutYProperty().set(300.0);
            btnDelete.layoutYProperty().set(300.0);
            anchorPane.setPrefHeight(335.0);
        } else if (type == 1) {
            checkBox.setVisible(false);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return nameField.getText();
    }

    public void setName(String s) {
        nameField.setText(s);
    }

    public LocalDate getDate() {
        return datePicker.getValue();
    }

    public void setDate(LocalDate d) {
        datePicker.setValue(d);
    }

    public String getDesc() {
        return descField.getText();
    }
    public void setDesc(String s) {
        descField.setText(s);
    }

    public LocalDateTime getStartTime() {
        return StringToLocalDateTime(getDate(), startTimeField.getText());
    }
    public void setStartTime(String s) {
        startTimeField.setText(s);
    }

    public LocalDateTime getEndTime() {
        return StringToLocalDateTime(getDate(), endTimeField.getText());
    }
    public void setEndTime(String s) {
        endTimeField.setText(s);
    }

    public LocalDateTime StringToLocalDateTime(LocalDate date, String time) {
        if (time.isEmpty()) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse((date.format(dateFormatter) + " "  + time), formatter);
    }

    public int getComplete() {
        return (checkBox.isSelected()) ? 1 : 0;
    }

    public boolean getSave() {
        return save;
    }

    public boolean getDelete() {
        return delete;
    }

    @FXML
    public void save() {
        save = true;
        System.out.println(type);
        if (type == 0) {
            dc.updateItem(id,
                    type,
                    getName(),
                    getDesc(),
                    getDate(),
                    null,
                    null,
                    getComplete());
        } else {
            dc.updateItem(id,
                    type,
                    getName(),
                    getDesc(),
                    getDate(),
                    getStartTime(),
                    getEndTime(),
                    getComplete());
        }
        stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void delete() {
        delete = true;
        dc.deleteItem(id);
        stage = (Stage) btnDelete.getScene().getWindow();
        stage.close();
    }

}
