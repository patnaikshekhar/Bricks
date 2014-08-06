package Game.Entities;

import Engine.GameObject;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Paddle extends GameObject {

    // Gun Constants
    private static Color GUN_COLOR = Color.BLACK;
    private static int GUN_PADDING = 4;
    private static final int GUN_WIDTH = 4;
    private static final int GUN_HEIGHT = 10;

    // Bullet Constants
    private static int BULLET_WIDTH = 2;
    private static int BULLET_HEIGHT = 4;
    private static double BULLET_SPEED = -0.5;
    private static Color BULLET_COLOR = Color.WHITE;

    private final Color color;
    private final int speed;
    private boolean gunsActive = false;

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

        if (gunsActive) {
            g.setColor(GUN_COLOR);
            g.fillRect(x + GUN_PADDING, y - GUN_HEIGHT, GUN_WIDTH, GUN_HEIGHT);
            g.fillRect(x + width - GUN_PADDING - GUN_WIDTH, y - GUN_HEIGHT, GUN_WIDTH, GUN_HEIGHT);
        }
    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_LEFT) {
            x -= speed;
        } else if (key == KeyEvent.VK_RIGHT) {
            x += speed;
        }

        if (gunsActive) {
            if (key == KeyEvent.VK_SPACE) {
                // Add Bullets
                Bullet leftBullet = new Bullet(x + GUN_PADDING + GUN_WIDTH / 2, y - GUN_HEIGHT, BULLET_WIDTH, BULLET_HEIGHT, BULLET_SPEED, BULLET_COLOR);
                Bullet rightBullet = new Bullet(x + width - GUN_PADDING + GUN_WIDTH / 2, y - GUN_HEIGHT, BULLET_WIDTH, BULLET_HEIGHT, BULLET_SPEED, BULLET_COLOR);
                this.gameManager.add(leftBullet);
                this.gameManager.add(rightBullet);
            }
        }
    }

    @Override
    public String getType() {
        return "Paddle";
    }

    public void activateGun() {
        this.gunsActive = true;
    }

    public void deactivateGun() {
        this.gunsActive = false;
    }
}
