package base;

import java.time.LocalDate;

public abstract class Item {
    LocalDate date;
    Text name;
    Text description;

    public Item(LocalDate date, Text name, Text description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public Text getName() {
        return name;
    }

    public Text getDesc() {
        return description;
    }

    public void setDate(LocalDate newDate) {
        date = newDate;
    }

    public void setName(Text newName) {
        name = newName;
    }

    public void setDescription(Text newDesc) {
        description = newDesc;
    }

    public abstract void draw();
}
