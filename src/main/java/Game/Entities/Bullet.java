package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/6/14.
 */
public class Bullet extends GameObject {

    private final Color color;

    public Bullet(double x, double y, int width, int height, double vy, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vy = vy;
        this.color = color;
    }

    @Override
    public void update(int dt) {
        y += (int)(dt * vy);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int)x, (int)y, width, height);
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Bullet";
    }
}
