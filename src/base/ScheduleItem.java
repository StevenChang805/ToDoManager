package base;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleItem extends Item {
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleItem(LocalDate date, Text name, Text description,  LocalTime start, LocalTime end) {
        super(date, name, description);
        startTime = start;
        endTime = end;
    }

    public LocalTime getStart() {
        return startTime;
    }

    public LocalTime getEnd() {
        return endTime;
    }

    public void setStart(LocalTime newStart) {
        startTime = newStart;
    }

    public void setEnd(LocalTime newEnd) {
        endTime = newEnd;
    }

    @Override
    public void draw() {

    }
}
