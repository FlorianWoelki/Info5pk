package com.florianwoelki.info5pk.neuronalnetwork;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Neuron {

    public abstract float getValue();

    public static float sigmoid(float x) {
        float et = (float) Math.pow( Math.E, x );
        return et / (1 + et);
    }

}
