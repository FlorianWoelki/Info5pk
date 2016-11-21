package com.florianwoelki.info5pk.neuralnetwork.neuron;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Connection {

    public float weight = 1;
    public Neuron entryNeuron;

    public Connection(Neuron entryNeuron, float weight) {
        this.weight = weight;
        this.entryNeuron = entryNeuron;
    }

    public float getValue() {
        return weight * entryNeuron.getValue();
    }

}
