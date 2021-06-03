package MonthlyToDo;

import base.Main;
import base.NewItemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class MonthlyViewController implements Initializable {
    @FXML
    public Label lblMonth;

    @FXML
    private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../base/AddNewItem.fxml"));
        Stage stage = getStage(loader);
        NewItemController nic = loader.<NewItemController>getController();
        nic.setTextField("Hi");

        stage.showAndWait();

        System.out.println(nic.getTextField());
    }

    public void initialize(URL url, ResourceBundle rb) {
        // initialize label
        setLblMonth(LocalDate.now());
    }

    public void setLblMonth(LocalDate date) {
        String month = date.getMonth().name();
        month = month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
        int year = date.getYear();

        lblMonth.setAlignment(Pos.CENTER);
        lblMonth.setText(month + " " + year);
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
