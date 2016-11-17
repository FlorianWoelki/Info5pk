package com.florianwoelki.info5pk.math;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class MathUtil {

    public static final float PI = 3.1415926535f;
    public static final float DEGREETORAD = PI / 180.0f;
    public static final float RADTODEGREE = 180.0f / PI;

    public static float abs(float value) {
        return Math.abs( value );
    }

    public static float sin(float value) {
        return (float) Math.sin( value );
    }

    public static float cos(float value) {
        return (float) Math.cos( value );
    }

    public static float tan(float value) {
        return (float) Math.tan( value );
    }

    public static float asin(float value) {
        return (float) Math.asin( value );
    }

    public static float acos(float value) {
        return (float) Math.acos( value );
    }

    public static float atan(float value) {
        return (float) Math.atan( value );
    }

    public static float sigmoid(float x) {
        float et = (float) Math.pow( Math.E, x );
        return et / (1 + et);
    }

    public static float clampNegativePosition(float value) {
        if ( value < -1 ) return -1;
        if ( value > 1 ) return 1;
        return value;
    }

}
