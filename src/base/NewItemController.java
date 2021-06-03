package base;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class NewItemController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TextField textField;

    private Stage stage;

    public void initialize(URL url, ResourceBundle rb) {
        textField.setText("Hello");
    }

    public String getTextField() {
        return textField.getText();
    }

    public void setTextField(String text) {
        textField.setText(text);
    }

    @FXML
    private void close() {
        stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
