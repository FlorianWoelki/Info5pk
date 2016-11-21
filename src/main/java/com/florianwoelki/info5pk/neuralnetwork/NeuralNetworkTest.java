package com.florianwoelki.info5pk.neuralnetwork;

import com.florianwoelki.info5pk.neuralnetwork.neuron.InputNeuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.WorkingNeuron;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class NeuralNetworkTest {

    public static void main(String[] args) throws Exception {
        System.out.println( "Begin Neural Network Test" );

        NeuralNetwork nn = new NeuralNetwork();
        InputNeuron in1 = new InputNeuron();
        InputNeuron in2 = new InputNeuron();
        InputNeuron in3 = new InputNeuron();

        WorkingNeuron out1 = new WorkingNeuron();
        WorkingNeuron out2 = new WorkingNeuron();
        WorkingNeuron out3 = new WorkingNeuron();

        nn.addInputNeuron( in1 );
        nn.addInputNeuron( in2 );
        nn.addInputNeuron( in3 );

        nn.generateHiddenNeurons( 3 );

        nn.addOutputNeuron( out1 );
        nn.addOutputNeuron( out2 );
        nn.addOutputNeuron( out3 );

        nn.generateFullMesh();

        nn.randomizeAllWeights();

        NeuralNetwork nn2 = nn.cloneFullMesh();

        for ( int i = 0; i < 3; i++ ) {
            if ( nn2.getOutputNeuronFromIndex( i ).getValue() != nn.getOutputNeuronFromIndex( i ).getValue() ) {
                throw new Exception( "Not working... :(" );
            }
        }

        System.out.println( "NN Test success!" );
    }

}
