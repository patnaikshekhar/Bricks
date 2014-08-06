package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Wall extends GameObject {

    private final Color color;
    private final Boolean draw;

    public Wall(int x, int y, int width, int height, Color color, Boolean draw) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.draw = draw;
    }

    @Override
    public void update(int dt) {
        // Do Nothing
    }

    @Override
    public void draw(Graphics2D g) {
        if (draw) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Wall";
    }
}
