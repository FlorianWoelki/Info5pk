package com.florianwoelki.info5pk.neuralnetwork;

import com.florianwoelki.info5pk.math.MathUtil;
import com.florianwoelki.info5pk.neuralnetwork.exception.NNNotFullyMeshedException;
import com.florianwoelki.info5pk.neuralnetwork.exception.NotSameAmountOfNeuronsException;
import com.florianwoelki.info5pk.neuralnetwork.neuron.Connection;
import com.florianwoelki.info5pk.neuralnetwork.neuron.InputNeuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.Neuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.WorkingNeuron;

import java.awt.*;
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

    public void addInputNeuronAndMesh( InputNeuron inputNeuron ) {
        this.inputNeurons.add( inputNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            wn.addNeuronConnection( new Connection( inputNeuron, 0 ) );
        }
    }

    public void addOutputNeuronAndMesh( WorkingNeuron workingNeuron ) {
        this.outputNeurons.add( workingNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            workingNeuron.addNeuronConnection( new Connection( wn, 0 ) );
        }
    }

    public void removeInputNeuron( InputNeuron inputNeuron ) {
        this.inputNeurons.remove( inputNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            List<Connection> connectionsToRemove = new ArrayList<>();
            for ( Connection connection : wn.getConnections() ) {
                if ( connection.entryNeuron == inputNeuron ) {
                    connectionsToRemove.add( connection );
                }
            }

            for ( Connection connection : connectionsToRemove ) {
                wn.getConnections().remove( connection );
            }
        }
    }

    public void removeOutputNeuron( WorkingNeuron workingNeuron ) {
        this.outputNeurons.remove( workingNeuron );
    }

    public void generateHiddenNeurons( int amount ) {
        for ( int i = 0; i < amount; i++ ) {
            this.hiddenNeurons.add( new WorkingNeuron() );
        }
    }

    public void addInputNeuronAndMesh( InputNeuron inputNeuron ) {
        this.inputNeurons.add( inputNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            wn.addNeuronConnection( new Connection( inputNeuron, 0 ) );
        }
    }

    public void addOutputNeuronAndMesh( WorkingNeuron workingNeuron ) {
        this.outputNeurons.add( workingNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            workingNeuron.addNeuronConnection( new Connection( wn, 0 ) );
        }
    }

    public void removeInputNeuron( InputNeuron inputNeuron ) {
        this.inputNeurons.remove( inputNeuron );
        for ( WorkingNeuron wn : this.hiddenNeurons ) {
            List<Connection> connectionsToRemove = new ArrayList<>();
            for ( Connection connection : wn.getConnections() ) {
                if ( connection.entryNeuron == inputNeuron ) {
                    connectionsToRemove.add( connection );
                }
            }

            for ( Connection connection : connectionsToRemove ) {
                wn.getConnections().remove( connection );
            }
        }
    }

    public void removeOutputNeuron( WorkingNeuron workingNeuron ) {
        this.outputNeurons.remove( workingNeuron );
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

    public void randomMutation( float mutationRate ) {
        int index = MathUtil.random.nextInt( this.hiddenNeurons.size() + this.outputNeurons.size() );
        if ( index < this.hiddenNeurons.size() ) {
            this.hiddenNeurons.get( index ).randomMutation( mutationRate );
        } else {
            this.outputNeurons.get( index - this.hiddenNeurons.size() ).randomMutation( mutationRate );
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

    public InputNeuron getInputNeuronFromIndex( int index ) {
        return this.inputNeurons.get( index );
    }

    public InputNeuron getInputNeuronFromName( String name ) {
        for ( InputNeuron neuron : this.inputNeurons ) {
            if ( name.equals( neuron.getName() ) ) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getHiddenNeuronFromIndex( int index ) {
        return this.hiddenNeurons.get( index );
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

    private final float NEURON_SIZE = 15;

    public void render( Graphics g, Rectangle rect ) {
        this.calculateNeuronsRenderPosition( rect );
        float yMin = rect.y + this.NEURON_SIZE / 2;
        float yMax = rect.y + rect.height - this.NEURON_SIZE / 2;
        float strongestConnection = this.getStrongestConnection();
        renderLayer( g, this.outputNeurons, strongestConnection, 10, 10 );
        renderLayer( g, this.hiddenNeurons, strongestConnection );
        renderLayerI( g, this.inputNeurons, strongestConnection, -120, 10, true );
    }

    private void renderLayerI( Graphics g, List<InputNeuron> layer, float strongestConnection, int nameOffsetX, int nameOffsetY, boolean writeRight ) {
        for ( int i = 0; i < layer.size(); i++ ) {
            this.drawNeuron( g, layer.get( i ), strongestConnection, nameOffsetX, nameOffsetY, writeRight );
        }
    }

    private void renderLayer( Graphics g, List<WorkingNeuron> layer, float strongestConnection, int nameOffsetX, int nameOffsetY ) {
        for ( int i = 0; i < layer.size(); i++ ) {
            this.drawNeuron( g, layer.get( i ), strongestConnection, nameOffsetX, nameOffsetY, false );
        }
    }

    private void renderLayer( Graphics g, List<WorkingNeuron> layer, float strongestConnection ) {
        for ( int i = 0; i < layer.size(); i++ ) {
            this.drawNeuron( g, layer.get( i ), strongestConnection, 0, 0, false );
        }
    }

    private void drawNeuron( Graphics g, Neuron n, float strongestConnection, int nameOffsetX, int nameOffsetY, boolean writeRight ) {
        if ( n instanceof WorkingNeuron ) {
            this.drawConnections( g, n, strongestConnection );
        }

        float x = n.drawX;
        float y = n.drawY;
        Color c = Color.BLACK;
        float val = n.getValue();
        if ( val < 0 ) {
            c = Color.RED;
        } else {
            c = Color.GREEN;
        }

        float valSize = val * this.NEURON_SIZE;

        g.setColor( Color.WHITE );
        g.fillOval( (int) x, (int) y, (int) this.NEURON_SIZE / 2 + 1, (int) this.NEURON_SIZE / 2 + 1 );
        g.setColor( c );
        g.fillOval( (int) x, (int) y, (int) valSize / 2, (int) valSize / 2 );

        if ( nameOffsetX != 0 || nameOffsetY != 0 ) {
            float xPos = x + nameOffsetX;
            float yPos = y + nameOffsetY;
            if ( writeRight ) {
                xPos -= 10;
            }
            g.setColor( Color.WHITE );
            g.drawString( n.getName(), (int) xPos, (int) yPos );
        }
    }

    private void drawConnections( Graphics g, Neuron n, float strongestConnection ) {
        WorkingNeuron wn = (WorkingNeuron) n;
        for ( Connection connection : wn.getConnections() ) {
            Color color = Color.BLACK;
            float value = connection.getValue();
            float alpha = Math.abs( value ) / strongestConnection;
            if ( alpha > 1f ) {
                alpha = 1f;
            }
            if ( value > 0 ) {
                color = new Color( 0f, 1f, 0f, alpha );
            } else {
                color = new Color( 1f, 0f, 0f, alpha );
            }

            g.setColor( color );
            g.drawLine( (int) n.drawX, (int) n.drawY, (int) connection.entryNeuron.drawX, (int) connection.entryNeuron.drawY );
        }
    }

    private void calculateNeuronsRenderPosition( Rectangle rectangle ) {
        float yMin = rectangle.y + this.NEURON_SIZE / 2;
        float yMax = rectangle.y + rectangle.height - this.NEURON_SIZE / 2;
        this.calculateNeuronsRenderPositionLayer( this.outputNeurons, rectangle.x + rectangle.width - this.NEURON_SIZE / 2, yMin, yMax );
        this.calculateNeuronsRenderPositionLayer( this.hiddenNeurons, rectangle.x + rectangle.width / 2, yMin, yMax );
        this.calculateNeuronsRenderPositionLayerI( this.inputNeurons, rectangle.x + this.NEURON_SIZE / 2, yMin, yMax );
    }

    private void calculateNeuronsRenderPositionLayerI( List<InputNeuron> layer, float x, float yMin, float yMax ) {
        float yDiff = yMax - yMin;
        float distanceBetweenNeurons = yDiff / ( layer.size() - 1 );
        float currentY = yMin;
        for ( int i = 0; i < layer.size(); i++ ) {
            layer.get( i ).drawX = x;
            layer.get( i ).drawY = currentY;
            currentY += distanceBetweenNeurons;
        }
    }

    private void calculateNeuronsRenderPositionLayer( List<WorkingNeuron> layer, float x, float yMin, float yMax ) {
        float yDiff = yMax - yMin;
        float distanceBetweenNeurons = yDiff / ( layer.size() - 1 );
        float currentY = yMin;
        for ( int i = 0; i < layer.size(); i++ ) {
            layer.get( i ).drawX = x;
            layer.get( i ).drawY = currentY;
            currentY += distanceBetweenNeurons;
        }
    }

    private float getStrongestConnection() {
        return MathUtil.max( this.getStrongestLayerConnection( this.hiddenNeurons ), this.getStrongestLayerConnection( this.outputNeurons ) );
    }

    private float getStrongestLayerConnection( List<WorkingNeuron> layer ) {
        float strongestConnection = 0;
        for ( Neuron n : layer ) {
            WorkingNeuron wn = (WorkingNeuron) n;
            float strongestNeuronConnection = Math.abs( wn.getStrongestConnection() );
            if ( strongestNeuronConnection > strongestConnection ) {
                strongestConnection = strongestNeuronConnection;
            }
        }
        return strongestConnection;
    }

}
