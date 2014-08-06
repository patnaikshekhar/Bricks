package Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by shpatnaik on 8/3/14.
 */
public class GamePanel extends JPanel implements KeyListener, Runnable {


    // Dimentions
    private int width;
    private int height;
    private int scale;

    // The main thread
    private Thread thread;

    // The Graphics Context
    private BufferedImage image;
    private Graphics2D g;

    // The Game
    Game game;

    // FPS
    private static final int FPS = 60;
    private static final long TARGET_TIME = 1000 / FPS;

    public GamePanel(Game game, int width, int height, int scale) {
        super();

        this.game = game;
        this.width = width;
        this.height = height;
        this.scale = scale;

        setPreferredSize(new Dimension(width * scale, height * scale));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        game.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void init() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
    }

    @Override
    public void run() {

        init();
        game.init(width, height);

        long start;
        long elapsed;
        long wait;
        long previousStart = System.nanoTime();

        // Main Game.Main Loop
        while(true) {

            start = System.nanoTime();

            game.update((int)(start - previousStart) / 1000000);
            game.draw(g);
            drawToScreen();

            elapsed = System.nanoTime() - start;
            wait = TARGET_TIME - (elapsed / 1000000);

            if (wait > 0) {
                try {
                    thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            previousStart = start;
        }
    }

    private void drawToScreen() {
        Graphics gx = getGraphics();
        gx.drawImage(image, 0, 0, width * scale, height * scale, null);
        gx.dispose();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
