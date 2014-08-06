package Game;

import Engine.*;
import Game.Entities.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final int BALL_WIDTH = 5;
    private static final int BALL_HEIGHT = 5;
    private static final int BALL_START_X = 100;
    private static final int BALL_START_Y = 160;
    private static final Color BALL_COLOR = Color.ORANGE;
    private static final double BALL_START_VX = 0.1;
    private static final double BALL_START_VY = 0.1;

    // Gutter Constants
    private static final int GUTTER_WIDTH = WINDOW_WIDTH;
    private static final int GUTTER_HEIGHT = 5;
    private static final int GUTTER_START_X = 0;
    private static final int GUTTER_START_Y = WINDOW_HEIGHT - GUTTER_HEIGHT;

    // Life
    private static final int LIFE_X = WINDOW_WIDTH - WALL_WIDTH - 40;
    private static final int LIFE_Y = WALL_HEIGHT + 10;
    private static final Color LIFE_COLOR = Color.WHITE;
    private static final Font LIFE_FONT = new Font("Century Gothic", Font.PLAIN, 9);
    private static final int LIFE_START  = 3;
    private static final String LIFE_LABEL = "Lives";

    // Score
    private static final int SCORE_X = WALL_WIDTH + 10;
    private static final int SCORE_Y = WALL_HEIGHT + 10;
    private static final Color SCORE_COLOR = Color.WHITE;
    private static final Font SCORE_FONT = new Font("Century Gothic", Font.PLAIN, 9);
    private static final int SCORE_START  = 0;
    private static final String SCORE_LABEL = "Score";

    // Power Up
    private static final int POWER_UP_SIZE = 10;
    private static final double POWER_UP_SPEED = 0.1;
    private static final String[] POWER_UP_TYPES = {"G", "M", "S", "B", "L", "H"};
    private static final Color[] POWER_UP_COLOR = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.MAGENTA};
    private static final Color[] POWER_UP_FONT_COLOR = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.white };
    private static final Font POWER_UP_FONT = new Font("Century", Font.BOLD, 8);
    private static final int POWER_UP_CHANCE = 1;
    private static final int POWER_UP_FONT_PADDING_X = 3;
    private static final int POWER_UP_FONT_PADDING_Y = 8;
    private static final long POWER_UP_TIME = 10000;

    // Power Up Specific
    private static final int POWER_UP_PADDLE_INCREASE_WIDTH = PADDLE_WIDTH * 2;
    private static final int POWER_UP_PADDLE_DECREASE_WIDTH = PADDLE_WIDTH / 2;
    private static final int POWER_UP_BALL_INCREASE_WIDTH = BALL_WIDTH * 3;
    private static final int POWER_UP_BALL_INCREASE_HEIGHT = BALL_HEIGHT * 3;

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
    private Wall gutter;
    private Text life;
    private Text score;

    // Power up Map - Contains a list of power ups and the time
    // They are active for
    Map<String, Long> powerUpsMap = new ConcurrentHashMap<String, Long>();

    public static void main(String[] args) {
        GameWindow.show(new BricksGame(), GAME_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_SCALE);
    }

    public void startLevel(int level) {

        // Clear Game Manager
        gm.clear();

        // Instantiate Objects
        background = new Background(width, height, BACKGROUND_COLOR);
        gm.add(background);

        topWall = new Wall(0, 0, width, WALL_HEIGHT, WALL_COLOR, true);
        leftWall = new Wall(0, WALL_HEIGHT, WALL_WIDTH, height - WALL_HEIGHT, WALL_COLOR, true);
        rightWall = new Wall(width - WALL_WIDTH, WALL_HEIGHT, WALL_WIDTH, height - WALL_HEIGHT, WALL_COLOR, true);
        gm.add(topWall);
        gm.add(leftWall);
        gm.add(rightWall);

        // Create Paddle and Ball
        resetPositions();

        // Add Gutter
        gutter = new Wall(GUTTER_START_X, GUTTER_START_Y, GUTTER_WIDTH, GUTTER_HEIGHT, WALL_COLOR, false);
        gm.add(gutter);

        // Add Life Text
        life = new Text(LIFE_X, LIFE_Y, LIFE_LABEL, LIFE_START, LIFE_COLOR, LIFE_FONT);
        gm.add(life);

        // Add Score Text
        score = new Text(SCORE_X, SCORE_Y, SCORE_LABEL, SCORE_START, SCORE_COLOR, SCORE_FONT);
        gm.add(score);

        // Load Bricks definition file for Level
        loadFile("/levels/Level-" + level + ".txt");

        // Add Bricks
        for (Brick b: bricks) {
            gm.add(b);
        }
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

                // Increment Score
                score.increment(5);

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

                createPowerUp(brick.getX(), brick.getY());

                // Add brick in list of bricks to remove
                bricksToRemove.add(brick);
            }
        }

        for (Brick brick: bricksToRemove) {
            bricks.remove(brick);
            gm.remove(brick);
        }

        if (ball.getRectangle().intersects(gutter.getRectangle())) {

            life.decrement();
            resetPositions();

            if (life.getValue() == 0) {
                System.exit(0);
            }
        }

        // Check if Power ups intersect Gutter
        for (GameObject o : gm.getObjectsOfType("PowerUp")) {
            if (o.getRectangle().intersects(gutter.getRectangle())) {
                gm.remove(o);
            }

            if (o.getRectangle().intersects(paddle.getRectangle())) {
                gm.remove(o);
                applyPowerUp(((PowerUp)o).getText());
            }
        }

        // If not bricks are present then change level
        if (bricks.isEmpty()) {
            currentLevel += 1;
            startLevel(currentLevel);
        }

        // Check if the power ups have expired
        for (String key: powerUpsMap.keySet()) {
            long value = powerUpsMap.get(key);
            value -= dt;
            if (value <= 0) {
                removePowerUp(key);
            }

            powerUpsMap.put(key, value);
        }
    }

    private void removePowerUp(String text) {
        powerUpsMap.remove(text);

        if (text.compareTo("G") == 0) {
            if (paddle != null) {
                paddle.setWidth(PADDLE_WIDTH);
            }
        }

        if (text.compareTo("S") == 0) {
            if (paddle != null) {
                paddle.setWidth(PADDLE_WIDTH);
            }
        }

        if (text.compareTo("L") == 0) {
            if (paddle != null) {
                ball.setWidth(BALL_WIDTH);
                ball.setHeight(BALL_HEIGHT);
            }
        }
    }

    private void applyPowerUp(String text) {

        if (text.compareTo("G") == 0) {
            if (paddle != null) {
                paddle.setWidth(POWER_UP_PADDLE_INCREASE_WIDTH);
                powerUpsMap.put(text, POWER_UP_TIME);
            }
        }

        if (text.compareTo("S") == 0) {
            if (paddle != null) {
                paddle.setWidth(POWER_UP_PADDLE_DECREASE_WIDTH);
                powerUpsMap.put(text, POWER_UP_TIME);
            }
        }

        if (text.compareTo("L") == 0) {
            if (ball != null) {
                ball.setWidth(POWER_UP_BALL_INCREASE_WIDTH);
                ball.setHeight(POWER_UP_BALL_INCREASE_HEIGHT);
                powerUpsMap.put(text, POWER_UP_TIME);
            }
        }

        if (text.compareTo("H") == 0) {
            life.increment();
        }
    }

    private void createPowerUp(int x, int y) {
        if (Utilities.randomNumber(1, POWER_UP_CHANCE) == 1) {
            int powerUpType = Utilities.randomNumber(0, POWER_UP_TYPES.length - 1);

            PowerUp p = new PowerUp(x, y, POWER_UP_SIZE, POWER_UP_SPEED,
                    POWER_UP_TYPES[powerUpType], POWER_UP_FONT_COLOR[powerUpType],
                    POWER_UP_COLOR[powerUpType], POWER_UP_FONT,
                    POWER_UP_FONT_PADDING_X, POWER_UP_FONT_PADDING_Y);

            gm.add(p);
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

    public void resetPositions() {

        if (paddle != null && ball != null) {
            gm.remove(paddle);
            gm.remove(ball);
        }

        paddle = new Paddle(PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_COLOR, PADDLE_SPEED);
        ball = new Ball(BALL_START_X, BALL_START_Y, BALL_WIDTH, BALL_HEIGHT, BALL_START_VX, BALL_START_VY, BALL_COLOR);

        gm.add(paddle);
        gm.add(ball);
    }
}
