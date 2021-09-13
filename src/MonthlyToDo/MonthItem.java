package MonthlyToDo;

import java.time.LocalDate;

public class MonthItem {
    String name;
    LocalDate date;

    public MonthItem(String itemName, LocalDate itemDate) {
        name = itemName;
        date = itemDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}