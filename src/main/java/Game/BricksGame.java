package Game;

import Engine.*;
import Game.Entities.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private static final int BALL_WIDTH = 10;
    private static final int BALL_HEIGHT = 10;
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
    private static final Color[] POWER_UP_COLOR = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE,
            Color.CYAN, Color.MAGENTA};
    private static final Color[] POWER_UP_FONT_COLOR = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.white };
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
    private static final double POWER_UP_MULTI_BALLS_TO_ADD_VX1 = 0.15;
    private static final double POWER_UP_MULTI_BALLS_TO_ADD_VY1 = 0.15;
    private static final double POWER_UP_MULTI_BALLS_TO_ADD_VX2 = -0.15;
    private static final double POWER_UP_MULTI_BALLS_TO_ADD_VY2 = -0.15;

    // End Constants

    // Game Managers
    private GameManager gm = new GameManager();

    // Sounds
    private static final String BRICK_SMASH_SOUND = "/sounds/brick_smash.wav";
    private static final String POWER_UP_SOUND = "/sounds/power_up.wav";
    private static final String PADDLE_HIT_SOUND = "/sounds/paddle_hit.wav";
    private static final String WALL_HIT_SOUND = "/sounds/wall_hit.wav";
    private static final String GUN_FIRE_SOUND = "/sounds/gun_fire.wav";
    private static final String BACKGROUND_SOUND = "/sounds/background.wav";
    private static final String[] ALL_SOUNDS = { BRICK_SMASH_SOUND, POWER_UP_SOUND, PADDLE_HIT_SOUND, WALL_HIT_SOUND,
            GUN_FIRE_SOUND, BACKGROUND_SOUND };

    // Fields
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

        // Add Life Text
        if (life != null) {
            life = new Text(LIFE_X, LIFE_Y, LIFE_LABEL, life.getValue(), LIFE_COLOR, LIFE_FONT);
        } else {
            life = new Text(LIFE_X, LIFE_Y, LIFE_LABEL, LIFE_START, LIFE_COLOR, LIFE_FONT);
        }

        gm.add(life);

        // Add Score Text
        if (score != null) {
            score = new Text(SCORE_X, SCORE_Y, SCORE_LABEL, score.getValue(), SCORE_COLOR, SCORE_FONT);
        } else {
            score = new Text(SCORE_X, SCORE_Y, SCORE_LABEL, SCORE_START, SCORE_COLOR, SCORE_FONT);
        }

        gm.add(score);

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

        // Pre load sounds
        SoundManager.loadSounds(ALL_SOUNDS);
        SoundManager.playSound(BACKGROUND_SOUND, -1);

        startLevel(currentLevel);
    }

    @Override
    public void draw(Graphics2D g) {
        gm.draw(g);
    }

    @Override
    public void update(int dt) {
        gm.update(dt);

        List<GameObject> balls = (List<GameObject>)((ArrayList<GameObject>) gm.getObjectsOfType("Ball")).clone();

        for (GameObject object : balls) {
            Ball ball = (Ball) object;

            if (ball.getRectangle().intersects(paddle.getRectangle())) {
                // Set Ball Position
                ball.setPosition(ball.getX(), paddle.getY() - ball.getHeight());

                // Set Ball Velocity
                //ball.setVelocity(ball.getVx(), ball.getVy() * -1);
                Utilities.Vector bounceVector = calculateBounceVector(ball);
                ball.setVelocity(bounceVector.x, bounceVector.y);

                SoundManager.playSound(PADDLE_HIT_SOUND, 0);
            }

            if (ball.getRectangle().intersects(topWall.getRectangle())) {
                // Set Ball Position
                ball.setPosition(ball.getX(), topWall.getHeight());

                // Set Ball Velocity
                ball.setVelocity(ball.getVx(), ball.getVy() * -1);

                SoundManager.playSound(WALL_HIT_SOUND, 0);
            }

            if (ball.getRectangle().intersects(leftWall.getRectangle())) {
                // Set Ball Position
                ball.setPosition(leftWall.getX() + leftWall.getWidth(), ball.getY());

                // Set Ball Velocity
                ball.setVelocity(ball.getVx() * -1, ball.getVy());

                SoundManager.playSound(WALL_HIT_SOUND, 0);
            }

            if (ball.getRectangle().intersects(rightWall.getRectangle())) {
                // Set Ball Position
                ball.setPosition(rightWall.getX() - ball.getWidth(), ball.getY());

                // Set Ball Velocity
                ball.setVelocity(ball.getVx() * -1, ball.getVy());

                SoundManager.playSound(WALL_HIT_SOUND, 0);
            }

            List<Brick> bricksToRemove = new Vector<Brick>();

            for (Brick brick : bricks) {

                // If Bullet hits brick then remove
                for (GameObject bullet: gm.getObjectsOfType("Bullet")) {
                    if (brick.getRectangle().intersects(bullet.getRectangle())) {
                        gm.remove(bullet);
                        createPowerUp(brick.getX(), brick.getY());
                        bricksToRemove.add(brick);
                        score.increment(2);
                        SoundManager.playSound(BRICK_SMASH_SOUND, 0);
                    }
                }

                if (ball.getRectangle().intersects(brick.getRectangle())) {

                    // Increment Score
                    score.increment(5);
                    SoundManager.playSound(BRICK_SMASH_SOUND, 0);

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
                gm.remove(ball);

                if (!gm.contains("Ball")) {
                    life.decrement();
                    resetPositions();

                    if (life.getValue() == 0) {
                        System.exit(0);
                    }
                }
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
            } else {
                powerUpsMap.put(key, value);
            }
        }

        // Check if bullet is out of bounds
        for (GameObject bullet: gm.getObjectsOfType("Bullet")) {
            if (bullet.getRectangle().intersects(topWall.getRectangle())) {
                gm.remove(bullet);
            }
        }
    }

    private void removePowerUp(String text) {

        for (String key : powerUpsMap.keySet()) {
            if (key.compareTo(text) == 0) {
                powerUpsMap.remove(text);
            }
        }

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
                for (GameObject o: gm.getObjectsOfType("Ball")) {
                    Ball ball = (Ball) o;
                    ball.setWidth(BALL_WIDTH);
                    ball.setHeight(BALL_HEIGHT);
                }
            }
        }

        if (text.compareTo("B") == 0) {
            if (paddle != null) {
                paddle.deactivateGun();
            }
        }
    }

    private void applyPowerUp(String text) {

        SoundManager.playSound(POWER_UP_SOUND, 0);

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
            for (GameObject o: gm.getObjectsOfType("Ball")) {
                Ball ball = (Ball) o;
                ball.setWidth(POWER_UP_BALL_INCREASE_WIDTH);
                ball.setHeight(POWER_UP_BALL_INCREASE_HEIGHT);
                powerUpsMap.put(text, POWER_UP_TIME);
            }
        }

        if (text.compareTo("H") == 0) {
            life.increment();
        }

        if (text.compareTo("B") == 0) {
            if (paddle != null) {
                paddle.activateGun();
                powerUpsMap.put(text, POWER_UP_TIME);
            }
        }

        if (text.compareTo("M") == 0) {

            List<GameObject> balls = (List<GameObject>)((ArrayList<GameObject>) gm.getObjectsOfType("Ball")).clone();

            for (GameObject object : balls) {
                Ball ball = (Ball) object;

                Ball ball1 = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(),
                        POWER_UP_MULTI_BALLS_TO_ADD_VX1, POWER_UP_MULTI_BALLS_TO_ADD_VY1, BALL_COLOR);
                Ball ball2 = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(),
                        POWER_UP_MULTI_BALLS_TO_ADD_VX2, POWER_UP_MULTI_BALLS_TO_ADD_VY2, BALL_COLOR);

                gm.add(ball1);
                gm.add(ball2);
            }

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

        if (paddle != null) {
            gm.remove(paddle);
        }

        paddle = new Paddle(PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_COLOR, PADDLE_SPEED);
        Ball ball = new Ball(BALL_START_X, BALL_START_Y, BALL_WIDTH, BALL_HEIGHT, BALL_START_VX, BALL_START_VY, BALL_COLOR);

        gm.add(paddle);
        gm.add(ball);
    }

    public Utilities.Vector calculateBounceVector(Ball ball) {
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double paddleCenterY = paddle.getY() + paddle.getHeight() / 2;
        double magnitude = new Utilities.Vector(ball.getVx(), ball.getVy()).magnitude();

        return new Utilities.Vector(ball.getX() -  paddleCenterX, ball.getY() - paddleCenterY).unit().multiply(magnitude);
    }
}
