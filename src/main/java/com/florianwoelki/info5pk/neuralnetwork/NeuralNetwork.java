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

    public void addInputNeuron(InputNeuron inputNeuron) {
        inputNeurons.add(inputNeuron);
    }

    public void addHiddenNeuron(WorkingNeuron workingNeuron) {
        hiddenNeurons.add(workingNeuron);
    }

    public void addOutputNeuron(WorkingNeuron workingNeuron) {
        outputNeurons.add(workingNeuron);
    }

    public void addInputNeuronAndMesh(InputNeuron inputNeuron) {
        inputNeurons.add(inputNeuron);
        for(WorkingNeuron wn : hiddenNeurons) {
            wn.addNeuronConnection(new Connection(inputNeuron, 0));
        }
    }

    public void addOutputNeuronAndMesh(WorkingNeuron workingNeuron) {
        outputNeurons.add(workingNeuron);
        for(WorkingNeuron wn : hiddenNeurons) {
            workingNeuron.addNeuronConnection(new Connection(wn, 0));
        }
    }

    public void removeInputNeuron(InputNeuron inputNeuron) {
        inputNeurons.remove(inputNeuron);
        for(WorkingNeuron wn : hiddenNeurons) {
            List<Connection> connectionsToRemove = new ArrayList<>();
            for(Connection connection : wn.getConnections()) {
                if(connection.entryNeuron == inputNeuron) {
                    connectionsToRemove.add(connection);
                }
            }

            for(Connection connection : connectionsToRemove) {
                wn.getConnections().remove(connection);
            }
        }
    }

    public void removeOutputNeuron(WorkingNeuron workingNeuron) {
        outputNeurons.remove(workingNeuron);
    }

    public void generateHiddenNeurons(int amount) {
        for(int i = 0; i < amount; i++) {
            hiddenNeurons.add(new WorkingNeuron());
        }
    }

    public void invalidate() {
        for(WorkingNeuron wn : hiddenNeurons) {
            wn.invalidate();
        }

        for(WorkingNeuron wn : outputNeurons) {
            wn.invalidate();
        }
    }

    public void randomizeAllWeights() {
        for(WorkingNeuron hiddenNeuron : hiddenNeurons) {
            hiddenNeuron.randomizeWeights();
        }

        for(WorkingNeuron outputNeuron : outputNeurons) {
            outputNeuron.randomizeWeights();
        }
    }

    public void randomMutation(float mutationRate) {
        int index = MathUtil.random.nextInt(hiddenNeurons.size() + outputNeurons.size());
        if(index < hiddenNeurons.size()) {
            hiddenNeurons.get(index).randomMutation(mutationRate);
        } else {
            outputNeurons.get(index - hiddenNeurons.size()).randomMutation(mutationRate);
        }
    }

    public void generateFullMesh() {
        isFullyMeshedGenerated = true;

        for(WorkingNeuron hiddenNeuron : hiddenNeurons) {
            for(InputNeuron inputNeuron : inputNeurons) {
                hiddenNeuron.addNeuronConnection(inputNeuron, 1);
            }
        }

        for(WorkingNeuron outputNeuron : outputNeurons) {
            for(WorkingNeuron hiddenNeuron : hiddenNeurons) {
                outputNeuron.addNeuronConnection(hiddenNeuron, 1);
            }
        }
    }

    public InputNeuron getInputNeuronFromIndex(int index) {
        return inputNeurons.get(index);
    }

    public InputNeuron getInputNeuronFromName(String name) {
        for(InputNeuron neuron : inputNeurons) {
            if(name.equals(neuron.getName())) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getHiddenNeuronFromIndex(int index) {
        return hiddenNeurons.get(index);
    }

    public WorkingNeuron getHiddenNeuronFromName(String name) {
        for(WorkingNeuron neuron : hiddenNeurons) {
            if(name.equals(neuron.getName())) {
                return neuron;
            }
        }
        return null;
    }

    public WorkingNeuron getOutputNeuronFromIndex(int index) {
        return outputNeurons.get(index);
    }

    public WorkingNeuron getOutputNeuronFromName(String name) {
        for(WorkingNeuron neuron : outputNeurons) {
            if(name.equals(neuron.getName())) {
                return neuron;
            }
        }
        return null;
    }

    public NeuralNetwork cloneFullMesh() throws NNNotFullyMeshedException, NotSameAmountOfNeuronsException {
        if(!isFullyMeshedGenerated) {
            throw new NNNotFullyMeshedException("The Neural Network is not fully meshed generated.");
        }

        NeuralNetwork copy = new NeuralNetwork();

        for(InputNeuron inputNeuron : inputNeurons) {
            copy.addInputNeuron((InputNeuron) inputNeuron.nameCopy());
        }

        for(WorkingNeuron hiddenNeuron : hiddenNeurons) {
            copy.addHiddenNeuron((WorkingNeuron) hiddenNeuron.nameCopy());
        }

        for(WorkingNeuron outputNeuron : outputNeurons) {
            copy.addOutputNeuron((WorkingNeuron) outputNeuron.nameCopy());
        }

        copy.generateFullMesh();

        for(int i = 0; i < hiddenNeurons.size(); i++) {
            List<Connection> connectionsOriginal = hiddenNeurons.get(i).getConnections();
            List<Connection> connectionsCopy = copy.hiddenNeurons.get(i).getConnections();
            if(connectionsOriginal.size() != connectionsCopy.size()) {
                throw new NotSameAmountOfNeuronsException("Cloning the hidden neurons was not successful. Because both has not the same size.");
            }

            for(int k = 0; k < connectionsOriginal.size(); k++) {
                connectionsCopy.get(k).weight = connectionsOriginal.get(k).weight;
            }
        }

        for(int i = 0; i < outputNeurons.size(); i++) {
            List<Connection> connectionsOriginal = outputNeurons.get(i).getConnections();
            List<Connection> connectionsCopy = copy.outputNeurons.get(i).getConnections();
            if(connectionsOriginal.size() != connectionsCopy.size()) {
                throw new NotSameAmountOfNeuronsException("Cloning the hidden neurons was not successful. Because both has not the same size.");
            }

            for(int k = 0; k < connectionsOriginal.size(); k++) {
                connectionsCopy.get(k).weight = connectionsOriginal.get(k).weight;
            }
        }

        return copy;
    }

    private final float NEURON_SIZE = 15;

    public void render(Graphics g, Rectangle rect) {
        calculateNeuronsRenderPosition(rect);
        float yMin = rect.y + NEURON_SIZE / 2;
        float yMax = rect.y + rect.height - NEURON_SIZE / 2;
        float strongestConnection = getStrongestConnection();
        renderLayer(g, outputNeurons, strongestConnection, 10, 10);
        renderLayer(g, hiddenNeurons, strongestConnection);
        renderLayerI(g, inputNeurons, strongestConnection, -120, 10, true);
    }

    private void renderLayerI(Graphics g, List<InputNeuron> layer, float strongestConnection, int nameOffsetX, int nameOffsetY, boolean writeRight) {
        for(int i = 0; i < layer.size(); i++) {
            drawNeuron(g, layer.get(i), strongestConnection, nameOffsetX, nameOffsetY, writeRight);
        }
    }

    private void renderLayer(Graphics g, List<WorkingNeuron> layer, float strongestConnection, int nameOffsetX, int nameOffsetY) {
        for(int i = 0; i < layer.size(); i++) {
            drawNeuron(g, layer.get(i), strongestConnection, nameOffsetX, nameOffsetY, false);
        }
    }

    private void renderLayer(Graphics g, List<WorkingNeuron> layer, float strongestConnection) {
        for(int i = 0; i < layer.size(); i++) {
            drawNeuron(g, layer.get(i), strongestConnection, 0, 0, false);
        }
    }

    private void drawNeuron(Graphics g, Neuron n, float strongestConnection, int nameOffsetX, int nameOffsetY, boolean writeRight) {
        if(n instanceof WorkingNeuron) {
            drawConnections(g, n, strongestConnection);
        }

        float x = n.drawX;
        float y = n.drawY;
        Color c = Color.BLACK;
        float val = n.getValue();
        if(val < 0) {
            c = Color.RED;
        } else {
            c = Color.GREEN;
        }

        float valSize = val * NEURON_SIZE;

        g.setColor(Color.WHITE);
        g.fillOval((int) x, (int) y, (int) NEURON_SIZE / 2 + 1, (int) NEURON_SIZE / 2 + 1);
        g.setColor(c);
        g.fillOval((int) x, (int) y, (int) valSize / 2, (int) valSize / 2);

        if(nameOffsetX != 0 || nameOffsetY != 0) {
            float xPos = x + nameOffsetX;
            float yPos = y + nameOffsetY;
            if(writeRight) {
                xPos -= 10;
            }
            g.setColor(Color.WHITE);
            g.drawString(n.getName(), (int) xPos, (int) yPos);
        }
    }

    private void drawConnections(Graphics g, Neuron n, float strongestConnection) {
        WorkingNeuron wn = (WorkingNeuron) n;
        for(Connection connection : wn.getConnections()) {
            Color color = Color.BLACK;
            float value = connection.getValue();
            float alpha = Math.abs(value) / strongestConnection;
            if(alpha > 1f) {
                alpha = 1f;
            }
            if(value > 0) {
                color = new Color(0f, 1f, 0f, alpha);
            } else {
                color = new Color(1f, 0f, 0f, alpha);
            }

            g.setColor(color);
            g.drawLine((int) n.drawX, (int) n.drawY, (int) connection.entryNeuron.drawX, (int) connection.entryNeuron.drawY);
        }
    }

    private void calculateNeuronsRenderPosition(Rectangle rectangle) {
        float yMin = rectangle.y + NEURON_SIZE / 2;
        float yMax = rectangle.y + rectangle.height - NEURON_SIZE / 2;
        calculateNeuronsRenderPositionLayer(outputNeurons, rectangle.x + rectangle.width - NEURON_SIZE / 2, yMin, yMax);
        calculateNeuronsRenderPositionLayer(hiddenNeurons, rectangle.x + rectangle.width / 2, yMin, yMax);
        calculateNeuronsRenderPositionLayerI(inputNeurons, rectangle.x + NEURON_SIZE / 2, yMin, yMax);
    }

    private void calculateNeuronsRenderPositionLayerI(List<InputNeuron> layer, float x, float yMin, float yMax) {
        float yDiff = yMax - yMin;
        float distanceBetweenNeurons = yDiff / (layer.size() - 1);
        float currentY = yMin;
        for(int i = 0; i < layer.size(); i++) {
            layer.get(i).drawX = x;
            layer.get(i).drawY = currentY;
            currentY += distanceBetweenNeurons;
        }
    }

    private void calculateNeuronsRenderPositionLayer(List<WorkingNeuron> layer, float x, float yMin, float yMax) {
        float yDiff = yMax - yMin;
        float distanceBetweenNeurons = yDiff / (layer.size() - 1);
        float currentY = yMin;
        for(int i = 0; i < layer.size(); i++) {
            layer.get(i).drawX = x;
            layer.get(i).drawY = currentY;
            currentY += distanceBetweenNeurons;
        }
    }

    private float getStrongestConnection() {
        return MathUtil.max(getStrongestLayerConnection(hiddenNeurons), getStrongestLayerConnection(outputNeurons));
    }

    private float getStrongestLayerConnection(List<WorkingNeuron> layer) {
        float strongestConnection = 0;
        for(Neuron n : layer) {
            WorkingNeuron wn = (WorkingNeuron) n;
            float strongestNeuronConnection = Math.abs(wn.getStrongestConnection());
            if(strongestNeuronConnection > strongestConnection) {
                strongestConnection = strongestNeuronConnection;
            }
        }
        return strongestConnection;
    }

}
