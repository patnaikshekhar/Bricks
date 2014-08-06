package Game.Entities;

import Engine.GameObject;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Paddle extends GameObject {

    private final Color color;
    private final int speed;

    public Paddle(int x, int y, int width, int height, Color color, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.speed = speed;
    }

    @Override
    public void update(int dt) {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_LEFT) {
            x -= speed;
        } else if (key == KeyEvent.VK_RIGHT) {
            x += speed;
        }
    }

    @Override
    public String getType() {
        return "Paddle";
    }
}
