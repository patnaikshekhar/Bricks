package Engine;

import javax.sound.sampled.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shpatnaik on 8/6/14.
 */
public class SoundManager {

    public static final Map<String, Clip> soundCache = new ConcurrentHashMap<String, Clip>();

    public SoundManager() {
    }

    public static void loadSounds(String[] names) {
        for (String name : names) {
            loadSound(name);
        }
    }

    public static void loadSound(String name) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    SoundManager.class.getResourceAsStream(name));
            clip.open(inputStream);
            soundCache.put(name, clip);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSound(String name) {

        if (!soundCache.containsKey(name)) {
            loadSound(name);
        }

        Clip clip = soundCache.get(name);
        clip.setFramePosition(0);
        clip.loop(0);
    }
}
