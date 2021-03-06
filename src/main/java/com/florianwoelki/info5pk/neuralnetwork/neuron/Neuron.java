package com.florianwoelki.info5pk.neuralnetwork.neuron;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Neuron {

    private String name = "no name";

    public float drawX;
    public float drawY;

    public abstract float getValue();

    public abstract Neuron nameCopy();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
