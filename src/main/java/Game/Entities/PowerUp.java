package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/6/14.
 */
public class PowerUp extends GameObject {

    private final Color fontColor;
    private final Color color;
    private final Font font;
    private final double speed;
    private final String text;
    private final int fontPaddingX;
    private final int fontPaddingY;

    public PowerUp(double x, double y, int size, double speed, String text, Color fontColor, Color color, Font font, int fontPaddingX, int fontPaddingY) {
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;

        this.speed = speed;

        this.fontColor = fontColor;
        this.color = color;
        this.font = font;
        this.text = text;
        this.fontPaddingX = fontPaddingX;
        this.fontPaddingY = fontPaddingY;
    }

    @Override
    public void update(int dt) {
        y += (dt * speed);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)x, (int)y, width, height);
        g.setColor(fontColor);
        g.setFont(font);
        g.drawString(text, (int)x + fontPaddingX, (int)y + fontPaddingY);
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "PowerUp";
    }

    public String getText() {
        return text;
    }
}
