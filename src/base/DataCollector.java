package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Properties;

public class DataCollector {
    private Connection conn;

    public DataCollector() {
        String error = connect();
        if (!error.isEmpty()) {
            System.out.println("An error occurred while trying to connect to the database.");
            System.out.println("Error: " + error);
        }
    }

    public String connect() {
        if (conn != null) {
            System.out.println("There is already an existing database connection.");
            return "";
        }
        Properties prop;
        try {
            // store username & password to database in db.properties file
            FileInputStream f = new FileInputStream("test/db.properties");
            prop = new Properties();
            prop.load(f);
        } catch (IOException e) {
            return "There was an issue locating the file db.properties.";
        }
        // establish a connection to the database using DriverManager
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database has been established.");
        } catch(SQLException | ClassNotFoundException e) {
            return "There was an issue connecting to the database.";
        }
        return "";
    } // end of method connect

    public Statement getStmt() {
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            System.out.println("An error occurred in getStmt().");
            return null;
        }
    } // end of method getStmt

    public PreparedStatement getPreparedStmt(String sql){
        try {
            return conn.prepareStatement(sql);
        } catch(SQLException e) {
            System.out.println("An error occurred in getPreparedStmt().");
            return null;
        }
    } // end of method getPreparedStmt

    public CallableStatement getCallableStmt(String sql) {
        try {
            return conn.prepareCall(sql);
        } catch (SQLException e) {
            System.out.println("An error occurred in getCallableStmt().");
            return null;
        }
    } // end of method getCallableStmt


    public void updateQuery(String query, String error) {
        try {
            getPreparedStmt(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println(error);
            e.printStackTrace();
        }
    } // end of method updateQuery

    public ResultSet infoQuery(String query, String error) {
        ResultSet rSet;
        try {
            rSet = getStmt().executeQuery(query);
        } catch (SQLException e) {
            System.out.println(error);
            rSet = null;
        }
        return rSet;
    } // end of method infoQuery


    public void addNewItem(int type, String name, String description, LocalDate date,
                           LocalDateTime start_time, LocalDateTime end_time, int complete) {
        // prepare components of SQL query
        String date_string = localDateToDateTime(date);
        String start_time_string = "null";
        String end_time_string = "null";
        if (start_time != null) {
            start_time_string = "'"+localDateTimeToDateTime(start_time)+"'";
        }
        if (end_time != null) {
            end_time_string = "'"+localDateTimeToDateTime(end_time)+"'";
        }
        // create the SQL query to add a new item
        String query = "INSERT INTO items (type, name, description, date, start_time, end_time, complete) VALUES ("
                + type
                + ", \"" + name + "\", "
                + "\"" + description + "\", "
                + date_string + ", "
                + start_time_string + ", "
                + end_time_string + ", "
                + complete + ");";
        // execute the query via updateQuery (secure)
        updateQuery(query, "There was an issue adding new items into the database.");
    } // end of method styleSheet.css

    public void updateItem(int id, int type, String name, String description, LocalDate date,
                           LocalDateTime start_time, LocalDateTime end_time, int complete) {
        // prepare components of SQL query
        String date_string = localDateToDateTime(date);
        String start_time_string = "null";
        String end_time_string = "null";

        if (start_time != null) {
            start_time_string = "'"+localDateTimeToDateTime(start_time)+"'";
        }
        if (end_time != null) {
            end_time_string = "'"+localDateTimeToDateTime(end_time)+"'";
        }
        // create SQL query
        String query = "UPDATE items SET " +
                "type="+type+", " +
                "name=\""+name+"\", " +
                "description=\""+description+"\", " +
                "date="+date_string+", " +
                "start_time="+start_time_string+", " +
                "end_time="+end_time_string+", " +
                "complete="+complete+" " +
                "WHERE id="+id+";";
        System.out.println(query);
        // execute SQL query via updateQuery (secure)
        updateQuery(query, "There was an issue updating an item in the database.");
    } // end of method updateItem

    public void deleteItem(int id) {
        // create SQL query
        String query = "DELETE from items WHERE id="+id+";";
        // execute SQL query via updateQuery (secure)
        updateQuery(query, "There was an issue deleting items from the database.");
    }

    public ResultSet getItems(LocalDate start_date, LocalDate end_date) {
        // prepare components of SQL query
        String start_date_string = localDateToDateTime(start_date);
        String end_date_string = localDateToDateTime(end_date);
        // create SQL query
        String query = "SELECT * FROM items WHERE date >= "
                + start_date_string + " AND date <= " + end_date_string + " ORDER BY date";
        // execute SQL query via infoQuery
        ResultSet itemData = null;
        itemData = infoQuery(query, "There was an issue getting items from the database.");
        return itemData;
    }


    public String localDateToDateTime(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    } // end of method localDateToDateTime

    public String localDateTimeToDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    } // end of method localDateTimeToDateTime

    public String prefixNumber(int num) {
        // prefixes inputted number with a 0 if less than 10
        if (num < 10) {
            return "0"+num;
        } else {
            return Integer.toString(num);
        }
    } // end of method prefixNumber

}
