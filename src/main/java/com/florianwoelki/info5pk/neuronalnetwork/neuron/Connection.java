package com.florianwoelki.info5pk.neuronalnetwork.neuron;

import com.florianwoelki.info5pk.neuronalnetwork.neuron.Neuron;

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
