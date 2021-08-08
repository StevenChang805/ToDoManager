package base;

import java.sql.ResultSet;
import java.time.LocalDate;

public class DataCollector {
    private LocalDate startDate;
    private LocalDate endDate;

    public DataCollector() {

    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ResultSet doQuery(String query) {

    }

    public String createQuery(LocalDate startDate, LocalDate endDate) {

    }

    public String connectToDatabase() {

    }

    public String updateDatabase(String sql) {

    }

    public void setStartDate(LocalDate startDate) {

    }

    public void setEndDate(LocalDate endDate) {

    }
}
