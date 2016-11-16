package com.florianwoelki.info5pk.neuronalnetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class WorkingNeuron extends Neuron {

    private float value;
    private List<Connection> connections = new ArrayList<>();

    public void addNeuronConnection(Neuron neuron, float weight) {
        addNeuronConnection( new Connection( neuron, weight ) );
    }

    private void addNeuronConnection(Connection connection) {
        connections.add( connection );
    }

    public void randomizeWeights() {
        for ( Connection connection : connections ) {
            connection.weight = (float) (Math.random() * 10);
        }
    }

    public void invalidate() {
        value = 0f;
    }

    private void calculate() {
        float value = 0;
        for ( Connection connection : connections ) {
            value += connection.getValue();
        }

        value = Neuron.sigmoid( value );
        this.value = value;
    }

    @Override
    public float getValue() {
        if ( value == 0f ) {
            calculate();
        }
        return value;
    }

}
