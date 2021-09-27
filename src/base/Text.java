package base;

import javafx.scene.text.Font;

public class Text {
    String content;
    Font font;

    public Text(String content, Font font) {
        this.content = content;
        this.font = font;
    }

    public String getContent() {
        return content;
    }
}
