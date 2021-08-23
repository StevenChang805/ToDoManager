package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class DataCollector {
    private Connection conn;

    public DataCollector() {
        String error = connectToDatabase();
        if (!error.isEmpty()) {
            System.out.println("An error occurred while trying to connect to the database.");
            System.out.println("Error: " + error);
        }
    }

    public PreparedStatement prepareStatement(String query){
        try {
            PreparedStatement st = conn.prepareStatement(query);
            return st;
        } catch(SQLException e) {
            System.out.println("An error occurred while trying to connect to the database.");
            return null;
        }
    }

    public String createQuery(LocalDate startDate, LocalDate endDate) {
        String start_date = localDateToDateTime(startDate);
        String end_date = localDateToDateTime(endDate);

        String query = "SELECT type, name, description, date FROM items WHERE date >= "
                + start_date + " AND date <= " + end_date + " ORDER BY date";

        return query;
    }

    public String localDateToDateTime(LocalDate date) {
        String year = date.getYear()+"";
        String month = prefixNumber(date.getMonthValue());
        String day = prefixNumber(date.getDayOfMonth());
        return year+month+day;
    }

    public String localDateTimeToDateTime(LocalDateTime dateTime) {
        // using existing method to create date string
        LocalDate date = dateTime.toLocalDate();
        String date_string = localDateToDateTime(date);

        // creating time string
        String hour = prefixNumber(dateTime.getHour());
        String minute = prefixNumber(dateTime.getMinute());
        String second = prefixNumber(dateTime.getSecond());
        String time_string = hour+":"+minute+":"+second;

        return date_string + " " + time_string;
    }

    public String prefixNumber(int num) {
        // prefixes inputted number with a 0 if less than 10
        if (num < 10) {
            return "0"+num;
        } else {
            return Integer.toString(num);
        }
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

    public void updateDatabase(int type, String name, String description, LocalDate date,
                                 LocalDateTime start_time, LocalDateTime end_time, int complete) {
        String date_string = localDateToDateTime(date);
        String start_time_string = "null";
        String end_time_string = "null";
        if (start_time != null) {
            start_time_string = localDateTimeToDateTime(start_time);
        }
        if (end_time != null) {
            end_time_string = localDateTimeToDateTime(end_time);
        }
        String query = "INSERT INTO items (type, name, description, date, start_time, end_time, complete) VALUES ("
                + type
                + ", \"" + name + "\", "
                + "\"" + description + "\", "
                + date_string + ", "
                + start_time_string + ", "
                + end_time_string + ", "
                + complete + ");";

        try {
            prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was an issue executing the update.");
            e.printStackTrace();
        }
    }
}
