package com.florianwoelki.info5pk.neuralnetwork.neuron;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class InputNeuron extends Neuron {

    private float value;

    public void setValue( Float value ) {
        this.value = value;
    }

    @Override
    public float getValue() {
        if ( this.value == 0.0f ) {
            return 0.0f;
        }

        return this.value;
    }

    @Override
    public Neuron nameCopy() {
        InputNeuron clone = new InputNeuron();
        clone.setName( getName() );
        return clone;
    }
}
