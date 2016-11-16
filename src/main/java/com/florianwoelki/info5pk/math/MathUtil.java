package com.florianwoelki.info5pk.math;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class MathUtil {

    public static float sigmoid(float x) {
        float et = (float) Math.pow( Math.E, x );
        return et / (1 + et);
    }

}
