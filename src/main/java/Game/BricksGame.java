package Game;

import Engine.Game;
import Engine.GameManager;
import Engine.GameWindow;
import Game.Entities.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class BricksGame implements Game {

    // Constants
    // Window Constants
    private static final String GAME_TITLE = "Bricks";
    private static final int WINDOW_WIDTH = 320;
    private static final int WINDOW_HEIGHT = 240;
    private static final int WINDOW_SCALE = 2;

    // Background Constants
    private static Color BACKGROUND_COLOR = Color.DARK_GRAY;

    // Wall Constants
    private static final int WALL_HEIGHT = 10;
    private static final int WALL_WIDTH = 10;
    private static final Color WALL_COLOR = Color.white;

    // Paddle Constants
    private static final int PADDLE_WIDTH = 50;
    private static final int PADDLE_HEIGHT = 10;
    private static final int PADDLE_X = WINDOW_WIDTH / 2 - (PADDLE_WIDTH / 2);
    private static final int PADDLE_Y = (int)(WINDOW_HEIGHT * 0.9);
    private static final Color PADDLE_COLOR = Color.RED;
    private static final int PADDLE_SPEED = 10;

    // Brick Constants
    private static final int BRICK_START_Y = 30;
    private static final int BRICK_START_X = WALL_WIDTH;
    private static final int BRICK_WIDTH = 30;
    private static final int BRICK_HEIGHT = 15;
    private static final int NO_OF_ROWS_IN_FILE = 5;
    private static final int BRICK_BORDER_SIZE = 1;
    private static final Color BRICK_BORDER_COLOR = Color.black;
    private static final Color[] BRICK_COLOR = {Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE};

    // Ball Constants
    private static final int BALL_WIDTH = 4;
    private static final int BALL_HEIGHT = 4;
    private static final int BALL_START_X = 100;
    private static final int BALL_START_Y = 160;
    private static final Color BALL_COLOR = Color.ORANGE;
    private static final double BALL_START_VX = 0.1;
    private static final double BALL_START_VY = 0.1;

    // End Constants

    // Game Manager
    private GameManager gm = new GameManager();
    private int width;
    private int height;
    private int currentLevel = 1;

    // Entities
    private Background background;
    private List<Brick> bricks;
    private Wall topWall;
    private Wall leftWall;
    private Wall rightWall;
    private Paddle paddle;
    private Ball ball;

    public static void main(String[] args) {
        GameWindow.show(new BricksGame(), GAME_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_SCALE);
    }

    public void startLevel(int level) {

        // Clear Game Manager
        gm.clear();

        // Instantiate Objects
        background = new Background(width, height, BACKGROUND_COLOR);

        topWall = new Wall(0, 0, width, WALL_HEIGHT, WALL_COLOR);
        leftWall = new Wall(0, WALL_HEIGHT, WALL_WIDTH, height - WALL_HEIGHT, WALL_COLOR);
        rightWall = new Wall(width - WALL_WIDTH, WALL_HEIGHT, WALL_WIDTH, height - WALL_HEIGHT, WALL_COLOR);

        paddle = new Paddle(PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_COLOR, PADDLE_SPEED);

        ball = new Ball(BALL_START_X, BALL_START_Y, BALL_WIDTH, BALL_HEIGHT, BALL_START_VX, BALL_START_VY, BALL_COLOR);

        // Load Bricks definition file for Level
        loadFile("/levels/Level-" + level + ".txt");

        // Add Objects to GameManager

        // Add Background
        gm.add(background);

        // Add Walls
        gm.add(topWall);
        gm.add(leftWall);
        gm.add(rightWall);

        // Add Bricks
        for (Brick b: bricks) {
            gm.add(b);
        }

        // Add Paddle
        gm.add(paddle);

        // Add Ball
        gm.add(ball);
    }

    private void loadFile(String fileName) {
        InputStream in = getClass().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        bricks = new Vector<Brick>();

        try {
            for (int row = 0; row < NO_OF_ROWS_IN_FILE; row++) {
                String[] tokens = br.readLine().split("\\s");
                for (int col = 0; col < tokens.length; col++) {
                    int token = Integer.valueOf(tokens[col]);
                    if (token != 0) {
                        Brick b = new Brick(
                                (col * BRICK_WIDTH) + BRICK_START_X,
                                (row * BRICK_HEIGHT) + BRICK_START_Y,
                                BRICK_WIDTH,
                                BRICK_HEIGHT,
                                BRICK_COLOR[token],
                                BRICK_BORDER_SIZE,
                                BRICK_BORDER_COLOR
                                );
                        bricks.add(b);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(int width, int height) {
        this.height = height;
        this.width = width;

        startLevel(currentLevel);
    }

    @Override
    public void draw(Graphics2D g) {
        gm.draw(g);
    }

    @Override
    public void update(int dt) {
        gm.update(dt);

        if (ball.getRectangle().intersects(paddle.getRectangle())) {
            // Set Ball Position
            ball.setPosition(ball.getX(), paddle.getY() - ball.getHeight());

            // Set Ball Velocity
            ball.setVelocity(ball.getVx(), ball.getVy() * -1);
        }

        if (ball.getRectangle().intersects(topWall.getRectangle())) {
            // Set Ball Position
            ball.setPosition(ball.getX(), topWall.getHeight());

            // Set Ball Velocity
            ball.setVelocity(ball.getVx(), ball.getVy() * -1);
        }

        if (ball.getRectangle().intersects(leftWall.getRectangle())) {
            // Set Ball Position
            ball.setPosition(leftWall.getX() + leftWall.getWidth(), ball.getY());

            // Set Ball Velocity
            ball.setVelocity(ball.getVx() * -1, ball.getVy());
        }

        if (ball.getRectangle().intersects(rightWall.getRectangle())) {
            // Set Ball Position
            ball.setPosition(rightWall.getX() - ball.getWidth(), ball.getY());

            // Set Ball Velocity
            ball.setVelocity(ball.getVx() * -1, ball.getVy());
        }

        List<Brick> bricksToRemove = new Vector<Brick>();

        for (Brick brick : bricks) {
            if (ball.getRectangle().intersects(brick.getRectangle())) {

                // Does Ball intersect with top or bottom side
                if (ball.getY() <= (brick.getY() + brick.getHeight()) && ball.getY() > brick.getY()) {
                    if (ball.getVy() < 0) {
                        // It Hit the bottom
                        ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                    } else {
                        // It hit the top
                        ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                    }

                    ball.setVelocity(ball.getVx(), ball.getVy() * -1);
                }

                // Does Ball intersect with Left or Right
                if (ball.getX() <= (brick.getY() + brick.getWidth()) && ball.getX() > brick.getX()) {
                    if (ball.getVx() < 0) {
                        // It hit the right side
                        ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                    } else {
                        // It hit the left side
                        ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                    }

                    ball.setVelocity(ball.getVx() * -1, ball.getVy());
                }

                // Add brick in list of bricks to remove
                bricksToRemove.add(brick);
            }
        }

        for (Brick brick: bricksToRemove) {
            bricks.remove(brick);
            gm.remove(brick);
        }

        // If not bricks are present then change level
        if (!gm.contains("Brick")) {
            currentLevel += 1;
            startLevel(currentLevel);
        }
    }

    @Override
    public void keyPressed(int key) {
        gm.keyPressed(key);

        // If Paddle intersects left wall then block Paddle
        if (paddle.getRectangle().intersects(leftWall.getRectangle())) {
            paddle.setPosition(leftWall.getX() + leftWall.getWidth(), paddle.getY());
        }

        // If Paddle intersects right wall then block Paddle
        if (paddle.getRectangle().intersects(rightWall.getRectangle())) {
            paddle.setPosition(rightWall.getX() - paddle.getWidth(), paddle.getY());
        }
    }
}
