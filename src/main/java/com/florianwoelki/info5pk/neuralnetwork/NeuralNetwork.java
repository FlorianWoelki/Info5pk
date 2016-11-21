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

    public void addInputNeuron(InputNeuron inputNeuron) {
        inputNeurons.add( inputNeuron );
    }

    public void addHiddenNeuron(WorkingNeuron workingNeuron) {
        hiddenNeurons.add( workingNeuron );
    }

    public void addOutputNeuron(WorkingNeuron workingNeuron) {
        outputNeurons.add( workingNeuron );
    }

    public void generateHiddenNeurons(int amount) {
        for ( int i = 0; i < amount; i++ ) {
            hiddenNeurons.add( new WorkingNeuron() );
        }
    }

    public void invalidate() {
        for ( WorkingNeuron wn : hiddenNeurons ) {
            wn.invalidate();
        }

        for ( WorkingNeuron wn : outputNeurons ) {
            wn.invalidate();
        }
    }

    public void randomizeAllWeights() {
        for ( WorkingNeuron hiddenNeuron : hiddenNeurons ) {
            hiddenNeuron.randomizeWeights();
        }

        for ( WorkingNeuron outputNeuron : outputNeurons ) {
            outputNeuron.randomizeWeights();
        }
    }

    public void generateFullMesh() {
        isFullyMeshedGenerated = true;

        for ( WorkingNeuron hiddenNeuron : hiddenNeurons ) {
            for ( InputNeuron inputNeuron : inputNeurons ) {
                hiddenNeuron.addNeuronConnection( inputNeuron, 1 );
            }
        }

        for ( WorkingNeuron outputNeuron : outputNeurons ) {
            for ( WorkingNeuron hiddenNeuron : hiddenNeurons ) {
                outputNeuron.addNeuronConnection( hiddenNeuron, 1 );
            }
        }
    }

    public InputNeuron getInputNeuronFromName(String name) {
        for ( InputNeuron neuron : inputNeurons ) {
            if ( name == neuron.getName() ) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getHiddenNeuronFromName(String name) {
        for ( WorkingNeuron neuron : hiddenNeurons ) {
            if ( name == neuron.getName() ) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getOutputNeuronFromIndex(int index) {
        return outputNeurons.get( index );
    }

    public WorkingNeuron getOutputNeuronFromName(String name) {
        for ( WorkingNeuron neuron : outputNeurons ) {
            if ( name == neuron.getName() ) {
                return neuron;
            }
        }
        return null;
    }

    public NeuralNetwork cloneFullMesh() throws NNNotFullyMeshedException, NotSameAmountOfNeuronsException {
        if ( !isFullyMeshedGenerated ) {
            throw new NNNotFullyMeshedException( "The Neural Network is not fully meshed generated." );
        }

        NeuralNetwork copy = new NeuralNetwork();

        for ( InputNeuron inputNeuron : inputNeurons ) {
            copy.addInputNeuron( (InputNeuron) inputNeuron.nameCopy() );
        }

        for ( WorkingNeuron hiddenNeuron : hiddenNeurons ) {
            copy.addHiddenNeuron( (WorkingNeuron) hiddenNeuron.nameCopy() );
        }

        for ( WorkingNeuron outputNeuron : outputNeurons ) {
            copy.addOutputNeuron( (WorkingNeuron) outputNeuron.nameCopy() );
        }

        copy.generateFullMesh();

        for ( int i = 0; i < hiddenNeurons.size(); i++ ) {
            List<Connection> connectionsOriginal = hiddenNeurons.get( i ).getConnections();
            List<Connection> connectionsCopy = copy.hiddenNeurons.get( i ).getConnections();
            if ( connectionsOriginal.size() != connectionsCopy.size() ) {
                throw new NotSameAmountOfNeuronsException( "Cloning the hidden neurons was not successful. Because both has not the same size." );
            }

            for ( int k = 0; k < connectionsOriginal.size(); k++ ) {
                connectionsCopy.get( k ).weight = connectionsOriginal.get( k ).weight;
            }
        }

        for ( int i = 0; i < outputNeurons.size(); i++ ) {
            List<Connection> connectionsOriginal = outputNeurons.get( i ).getConnections();
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
