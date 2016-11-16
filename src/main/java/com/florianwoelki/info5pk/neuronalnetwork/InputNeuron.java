package com.florianwoelki.info5pk.neuronalnetwork;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class InputNeuron extends Neuron {

    private float value;

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public float getValue() {
        return value;
    }
}
