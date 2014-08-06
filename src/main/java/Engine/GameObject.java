package Engine;

import java.awt.*;

/**
 * Created by shpatnaik on 8/3/14.
 */
public abstract class GameObject {

    // Position Vector
    protected int x;
    protected int y;

    // Velocity Vector
    protected double vx;
    protected double vy;

    // Object Height and Width
    protected int height;
    protected int width;

    public abstract void update(int dt);
    public abstract void draw(Graphics2D g);
    public abstract void keyPressed(int key);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
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
        return new Rectangle(x, y, width, height);
    }

    public abstract String getType();
}
