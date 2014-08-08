package Engine;

import java.awt.*;

/**
 * Created by shpatnaik on 8/3/14.
 */
public abstract class GameObject {

    // Position Vector
    protected double x;
    protected double y;

    // Velocity Vector
    protected double vx;
    protected double vy;

    // Object Height and Width
    protected int height;
    protected int width;

    // Game Manager
    protected GameManager gameManager;

    public abstract void update(int dt);
    public abstract void draw(Graphics2D g);
    public abstract void keyPressed(int key);

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public Vector2 getVelocity() {
        return (new Vector2(vx, vy));
    }

    public void setVelocity(Vector2 vector) {
        this.vx = vector.x;
        this.vy = vector.y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Rectangle getRectangle() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public abstract String getType();

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
