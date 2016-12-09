package com.florianwoelki.info5pk.neuralnetwork.neuron;

import com.florianwoelki.info5pk.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class WorkingNeuron extends Neuron {

    private Float value = null  ;
    private List<Connection> connections = new ArrayList<>();

    public void randomMutation( float mutationRate ) {
        Connection connection = this.connections.get( MathUtil.random.nextInt( this.connections.size() ) );
        connection.weight += (float) MathUtil.random.nextDouble() * 2 * mutationRate - mutationRate;
    }

    public void addNeuronConnection( Neuron neuron, float weight ) {
        this.addNeuronConnection( new Connection( neuron, weight ) );
    }

    private void addNeuronConnection( Connection connection ) {
        this.connections.add( connection );
    }

    public void invalidate() {
        this.value = null;
    }

    public void randomizeWeights() {
        for ( Connection connection : this.connections ) {
            connection.weight = (float) ( MathUtil.random.nextDouble() * 2 - 1 );
        }
    }

    private void calculate() {
        float value = 0;
        for ( Connection connection : this.connections ) {
            value += connection.getValue();
        }

        value = MathUtil.sigmoid( value );
        this.value = value;
    }

    @Override
    public float getValue() {
        if ( this.value == null ) {
            calculate();
        }
        return this.value;
    }

    @Override
    public Neuron nameCopy() {
        WorkingNeuron clone = new WorkingNeuron();
        clone.setName( getName() );
        return clone;
    }

    public float getStrongestConnection() {
        float strongest = 0;
        for ( Connection connection : this.connections ) {
            float val = MathUtil.abs( connection.weight );
            if ( val > strongest ) {
                strongest = val;
            }
        }
        return strongest;
    }

    public List<Connection> getConnections() {
        return this.connections;
    }
}
