package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer {
    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
