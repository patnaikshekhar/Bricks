package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/6/14.
 */
public class Text extends GameObject {

    private final String title;
    private final Color color;
    private int value;
    private final Font font;

    public Text(int x, int y, String title, int startValue, Color color, Font font) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.value = startValue;
        this.color = color;
        this.font = font;
    }

    @Override
    public void update(int dt) {
        // Do Nothing
    }

    @Override
    public void draw(Graphics2D g) {
        String text = title + " : " + String.valueOf(value);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Text";
    }

    public void increment() {
        this.value += 1;
    }

    public void increment(int d) {
        this.value += d;
    }

    public void decrement() {
        this.value -= 1;
    }

    public void decrement(int d) {
        this.value -= d;
    }

    public int getValue() {
        return value;
    }
}
