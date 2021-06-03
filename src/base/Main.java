package base;

import MonthlyToDo.MonthlyView;
import MonthlyToDo.MonthlyViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;
    public MonthlyViewController mvc;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        // initialize the application with monthly view
        Parent root = FXMLLoader.load(getClass().getResource("../MonthlyToDo/MonthlyToDo.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
