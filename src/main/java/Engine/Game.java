package Engine;

import java.awt.*;

/**
 * Created by shpatnaik on 8/3/14.
 */
public interface Game {

    public void init(int width, int height);
    public void draw(Graphics2D g);
    public void update(int dt);
    public void keyPressed(int key);

}
