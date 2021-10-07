package base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Item {
    int id;
    int type;
    LocalDate date;
    String name;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int complete;

    public Item(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getComplete() {
        return complete;
    }

    public String getStartTimeString() {
        return timeToString(startTime);
    }

    public String getEndTimeString() {
        return timeToString(endTime);
    }

    public String timeToString(LocalDateTime t) {
        if (t == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return t.format(formatter);
    }

    public void setDate(LocalDate newDate) {
        date = newDate;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setDescription(String newDesc) {
        description = newDesc;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

}
