package Engine;

import java.util.Random;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Utilities {

    public static int randomNumber(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
