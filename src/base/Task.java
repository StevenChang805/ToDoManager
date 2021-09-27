package base;

import java.time.LocalDate;

public class Task extends Item {

    private int complete = 0;

    public Task(LocalDate date, Text name, Text description) {
        super(date, name, description);
    }

    public int getStatus() {
        return complete;
    }

    public void setStatus(int newStatus) {
        complete = newStatus;
    }

}
