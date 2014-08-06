package Engine;

import javax.swing.*;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class GameWindow {

    public static void show(Game game, String title, int width, int height, int scale) {
        JFrame window = new JFrame(title);
        window.setContentPane(new GamePanel(game, width, height, scale));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }

}
