package Game.Entities;

import Engine.GameManager;
import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Brick extends GameObject {

    private Color color;
    private final int borderSize;
    private final Color borderColor;

    public Brick(int x, int y, int width, int height, Color color, int borderSize, Color borderColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderSize = borderSize;
        this.borderColor = borderColor;
    }

    @Override
    public void update(int dt) {
        // Do Nothing
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(borderColor);
        g.fillRect(x, y, width, height);
        g.setColor(color);
        g.fillRect(x + borderSize, y + borderSize, width - (2 * borderSize), height - (2 * borderSize));
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Brick";
    }
}
