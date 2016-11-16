package com.florianwoelki.info5pk.neuronalnetwork;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Neuron {

    private String name = "no name";

    public abstract float getValue();

    public abstract Neuron nameCopy();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
