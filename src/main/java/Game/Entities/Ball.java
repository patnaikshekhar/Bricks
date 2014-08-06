package Game.Entities;

import Engine.GameObject;

import java.awt.*;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Ball extends GameObject {

    private final Color color;

    public Ball(int x, int y, int width, int height, double vx, double vy, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vx = vx;
        this.vy = vy;
        this.color = color;
    }

    @Override
    public void update(int dt) {
        x += (vx * dt);
        y += (vy * dt);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    @Override
    public void keyPressed(int key) {
        // Do Nothing
    }

    @Override
    public String getType() {
        return "Ball";
    }
}
