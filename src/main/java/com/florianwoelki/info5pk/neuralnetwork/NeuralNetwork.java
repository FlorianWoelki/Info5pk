package com.florianwoelki.info5pk.neuralnetwork;

import com.florianwoelki.info5pk.neuralnetwork.exception.NNNotFullyMeshedException;
import com.florianwoelki.info5pk.neuralnetwork.exception.NotSameAmountOfNeuronsException;
import com.florianwoelki.info5pk.neuralnetwork.neuron.Connection;
import com.florianwoelki.info5pk.neuralnetwork.neuron.InputNeuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.WorkingNeuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class NeuralNetwork {

    private boolean isFullyMeshedGenerated;

    private List<InputNeuron> inputNeurons = new ArrayList<>();
    private List<WorkingNeuron> hiddenNeurons = new ArrayList<>();
    private List<WorkingNeuron> outputNeurons = new ArrayList<>();

    public void addInputNeuron( InputNeuron inputNeuron ) {
        this.inputNeurons.add( inputNeuron );
    }

    public void addHiddenNeuron( WorkingNeuron workingNeuron ) {
        this.hiddenNeurons.add( workingNeuron );
    }

    public void addOutputNeuron( WorkingNeuron workingNeuron ) {
        this.outputNeurons.add( workingNeuron );
    }

    public void generateHiddenNeurons( int amount ) {
        for ( int i = 0; i < amount; i++ ) {
            this.hiddenNeurons.add( new WorkingNeuron() );
        }
    }

    public void invalidate() {
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            wn.invalidate();
        }

        for ( WorkingNeuron wn : this.outputNeurons ) {
            wn.invalidate();
        }
    }

    public void randomizeAllWeights() {
        for ( WorkingNeuron hiddenNeuron : this.hiddenNeurons ) {
            hiddenNeuron.randomizeWeights();
        }

        for ( WorkingNeuron outputNeuron : this.outputNeurons ) {
            outputNeuron.randomizeWeights();
        }
    }

    public void generateFullMesh() {
        this.isFullyMeshedGenerated = true;

        for ( WorkingNeuron hiddenNeuron : this.hiddenNeurons ) {
            for ( InputNeuron inputNeuron : this.inputNeurons ) {
                hiddenNeuron.addNeuronConnection( inputNeuron, 1 );
            }
        }

        for ( WorkingNeuron outputNeuron : this.outputNeurons ) {
            for ( WorkingNeuron hiddenNeuron : this.hiddenNeurons ) {
                outputNeuron.addNeuronConnection( hiddenNeuron, 1 );
            }
        }
    }

    public InputNeuron getInputNeuronFromName( String name ) {
        for ( InputNeuron neuron : this.inputNeurons ) {
            if ( name.equals( neuron.getName() ) ) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getHiddenNeuronFromName( String name ) {
        for ( WorkingNeuron neuron : this.hiddenNeurons ) {
            if ( name.equals( neuron.getName() ) ) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getOutputNeuronFromIndex( int index ) {
        return this.outputNeurons.get( index );
    }

    public WorkingNeuron getOutputNeuronFromName( String name ) {
        for ( WorkingNeuron neuron : this.outputNeurons ) {
            if ( name.equals( neuron.getName() ) ) {
                return neuron;
            }
        }
        return null;
    }

    public NeuralNetwork cloneFullMesh() throws NNNotFullyMeshedException, NotSameAmountOfNeuronsException {
        if ( !this.isFullyMeshedGenerated ) {
            throw new NNNotFullyMeshedException( "The Neural Network is not fully meshed generated." );
        }

        NeuralNetwork copy = new NeuralNetwork();

        for ( InputNeuron inputNeuron : this.inputNeurons ) {
            copy.addInputNeuron( (InputNeuron) inputNeuron.nameCopy() );
        }

        for ( WorkingNeuron hiddenNeuron : this.hiddenNeurons ) {
            copy.addHiddenNeuron( (WorkingNeuron) hiddenNeuron.nameCopy() );
        }

        for ( WorkingNeuron outputNeuron : this.outputNeurons ) {
            copy.addOutputNeuron( (WorkingNeuron) outputNeuron.nameCopy() );
        }

        copy.generateFullMesh();

        for ( int i = 0; i < this.hiddenNeurons.size(); i++ ) {
            List<Connection> connectionsOriginal = this.hiddenNeurons.get( i ).getConnections();
            List<Connection> connectionsCopy = copy.hiddenNeurons.get( i ).getConnections();
            if ( connectionsOriginal.size() != connectionsCopy.size() ) {
                throw new NotSameAmountOfNeuronsException( "Cloning the hidden neurons was not successful. Because both has not the same size." );
            }

            for ( int k = 0; k < connectionsOriginal.size(); k++ ) {
                connectionsCopy.get( k ).weight = connectionsOriginal.get( k ).weight;
            }
        }

        for ( int i = 0; i < this.outputNeurons.size(); i++ ) {
            List<Connection> connectionsOriginal = this.outputNeurons.get( i ).getConnections();
            List<Connection> connectionsCopy = copy.outputNeurons.get( i ).getConnections();
            if ( connectionsOriginal.size() != connectionsCopy.size() ) {
                throw new NotSameAmountOfNeuronsException( "Cloning the hidden neurons was not successful. Because both has not the same size." );
            }

            for ( int k = 0; k < connectionsOriginal.size(); k++ ) {
                connectionsCopy.get( k ).weight = connectionsOriginal.get( k ).weight;
            }
        }

        return copy;
    }

}
