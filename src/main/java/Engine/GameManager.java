package Engine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shpatnaik on 8/3/14.
 */
public class GameManager {

    List<GameObject> objects;

    public GameManager() {
        objects = new ArrayList<GameObject>();
    }

    public void update(int dt) {
        for (GameObject object : objects) {
            object.update(dt);
        }
    }

    public void draw(Graphics2D g) {
        for (GameObject object : objects) {
            object.draw(g);
        }
    }

    public void keyPressed(int key) {
        for (GameObject object : objects) {
            object.keyPressed(key);
        }
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    public void remove(GameObject object) {
        objects.remove(object);
    }

    public void clear() {
        objects.clear();
    }

    public boolean contains(String type) {
        for (GameObject object : objects) {
            if (object.getType().compareTo(type) == 0) {
                return true;
            }
        }

        return false;
    }
}
