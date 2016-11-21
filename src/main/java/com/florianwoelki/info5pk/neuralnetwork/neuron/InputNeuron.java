package com.florianwoelki.info5pk.neuralnetwork.neuron;

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

    @Override
    public Neuron nameCopy() {
        InputNeuron clone = new InputNeuron();
        clone.setName( getName() );
        return clone;
    }
}
