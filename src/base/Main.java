package base;

import MonthlyToDo.MonthlyView;
import MonthlyToDo.MonthlyViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main extends Application {

    public static Stage primaryStage;
    public MonthlyViewController mvc;

    // int type, String name, String description, LocalDate date,
    //                                 LocalDateTime start_time, LocalDateTime end_time, int complete

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        // initialize the application with monthly view
        Parent root = FXMLLoader.load(getClass().getResource("../MonthlyToDo/MonthlyToDo.fxml"));
        DataCollector dc = new DataCollector();
        dc.updateDatabase(1, "Computer Science IA", "Finish programming", LocalDate.of(2021, 10, 15),
                null, null, 0);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
