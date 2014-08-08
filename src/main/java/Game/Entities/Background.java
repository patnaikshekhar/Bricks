package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Background extends GameObject {

    private final Color color;

    public Background(int width, int height, Color color) {
        this.color = color;
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void update(int dt) {
        // Do Nothing
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw background
        g.setColor(color);
        g.fillRect((int)x, (int)y, width, height);
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Background";
    }
}
