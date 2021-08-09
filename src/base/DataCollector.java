package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class DataCollector {
    private Connection conn;

    private LocalDate startDate;
    private LocalDate endDate;

    public DataCollector() {
        String error = connectToDatabase();
        if (!error.isEmpty()) {
            System.out.println("An error occurred while trying to connect to the database.");
            System.out.println("Error: " + error);
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

//    public ResultSet doQuery(String query) {
//
//    }

    public String createQuery(LocalDate startDate, LocalDate endDate) {
        return "Work in Progress.";
    }

    public String connectToDatabase() {
        if (conn != null) {
            System.out.println("There is already an existing database connection.");
            return "";
        }
        Properties prop;
        try {
            FileInputStream f = new FileInputStream("test/db.properties");
            prop = new Properties();
            prop.load(f);
        } catch (IOException e) {
            String error = "Unable to locate the file db.properties.";
            return error;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database has been established.");
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            String error = "Unable to make a connection with the database.";
            return error;
        }
        return "";
    }

//    public String updateDatabase(String sql) {
//
//    }

    public void setStartDate(LocalDate startDate) {

    }

    public void setEndDate(LocalDate endDate) {

    }
}
