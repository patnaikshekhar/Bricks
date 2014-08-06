package Engine;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shpatnaik on 8/6/14.
 */
public class SoundManager {

    public SoundManager() {
    }

    public static void playSound(String name) {

        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    SoundManager.class.getResourceAsStream(name));
            clip.open(inputStream);
            clip.loop(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
