package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.level.Level;
import com.florianwoelki.info5pk.level.tile.Tile;
import com.florianwoelki.info5pk.math.MathUtil;
import com.florianwoelki.info5pk.neuralnetwork.NeuralNetwork;
import com.florianwoelki.info5pk.neuralnetwork.neuron.InputNeuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.WorkingNeuron;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Creature {

    public static int maximumGeneration = 1;
    public static int currentId;
    public static Creature oldestCreatureEver = new TestCreature(null, 0, 0, 0);

    public java.util.List<Creature> children = new ArrayList<>();
    private long motherId;
    private long id;
    private java.util.List<Long> childIds = new ArrayList<>();
    protected Creature mother;
    protected int generation = 1;

    protected final float COST_EAT = 0.1f;
    protected final float GAIN_EAT = 1f;
    protected final float COST_PERMANENT = 0.01f;
    protected final float COST_WALK = 0.05f;
    protected final float COST_ROTATE = 0.05f;

    protected final float FOOD_DROP_PERCENTAGE = 0;

    protected final float MOVE_SPEED = 2f;

    protected final float START_ENERGY = 150;
    protected final float MINIMUM_SURVIVAL_ENERGY = 100;

    protected final int SIZE = 8;
    protected final int FEELER_SIZE = 4;

    protected float x;
    protected float y;
    protected float viewAngle;

    protected float feelerX;
    protected float feelerY;
    protected float feelerAngle;

    protected float energy = START_ENERGY;
    protected float age = 0;

    protected NeuralNetwork brain;

    protected final String NAME_IN_BIAS = "bias";
    protected final String NAME_IN_FOOD_VALUE_POSITION = "Food Value Position";
    protected final String NAME_IN_FOOD_VALUE_FEELER = "Food Value Feeler";
    protected final String NAME_IN_OCCLUSION_FEELER = "Occlusion Feeler";
    protected final String NAME_IN_ENERGY = "Energy";
    protected final String NAME_IN_AGE = "Age";
    protected final String NAME_IN_GENETIC_DIFFERENCE = "Genetic Difference";
    protected final String NAME_IN_WAS_ATTACKED = "Was Attacked";
    protected final String NAME_IN_WATER_ON_FEELER = "Water On Feeler";
    protected final String NAME_IN_WATER_ON_CREATURE = "Water On Creature";
    protected final String NAME_IN_MEMORY1 = "Input Memory #1";

    protected final String NAME_OUT_BIRTH = "Birth";
    protected final String NAME_OUT_ROTATE = "Rotate";
    protected final String NAME_OUT_FORWARD = "Forward";
    protected final String NAME_OUT_FEELER_ANGLE = "Feeler Angle";
    protected final String NAME_OUT_ATTACK = "Attack";
    protected final String NAME_OUT_EAT = "Eat";
    protected final String NAME_OUT_MEMORY1 = "Output Memory #1";

    protected InputNeuron inBias = new InputNeuron();
    protected InputNeuron inFoodValuePosition = new InputNeuron();
    protected InputNeuron inFoodValueFeeler = new InputNeuron();
    protected InputNeuron inOcclusionFeeler = new InputNeuron();
    protected InputNeuron inEnergy = new InputNeuron();
    protected InputNeuron inAge = new InputNeuron();
    protected InputNeuron inGeneticDifference = new InputNeuron();
    protected InputNeuron inWasAttacked = new InputNeuron();
    protected InputNeuron inWaterOnFeeler = new InputNeuron();
    protected InputNeuron inWaterOnCreature = new InputNeuron();
    protected InputNeuron inMemory1 = new InputNeuron();

    protected WorkingNeuron outBirth = new WorkingNeuron();
    protected WorkingNeuron outRotate = new WorkingNeuron();
    protected WorkingNeuron outForward = new WorkingNeuron();
    protected WorkingNeuron outFeelerAngle = new WorkingNeuron();
    protected WorkingNeuron outAttack = new WorkingNeuron();
    protected WorkingNeuron outEat = new WorkingNeuron();
    protected WorkingNeuron outMemory1 = new WorkingNeuron();

    protected Level level;
    public float mouseWheelScale = 1;

    protected Color color;

    public Creature(Level level, float x, float y, float viewAngle) {
        this.id = currentId++;

        this.level = level;
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;

        inBias.setName(NAME_IN_BIAS);
        inFoodValuePosition.setName(NAME_IN_FOOD_VALUE_POSITION);
        inFoodValueFeeler.setName(NAME_IN_FOOD_VALUE_FEELER);
        inOcclusionFeeler.setName(NAME_IN_OCCLUSION_FEELER);
        inEnergy.setName(NAME_IN_ENERGY);
        inAge.setName(NAME_IN_AGE);
        inGeneticDifference.setName(NAME_IN_GENETIC_DIFFERENCE);
        inWasAttacked.setName(NAME_IN_WAS_ATTACKED);
        inWaterOnFeeler.setName(NAME_IN_WATER_ON_FEELER);
        inWaterOnCreature.setName(NAME_IN_WATER_ON_CREATURE);
        inMemory1.setName(NAME_IN_MEMORY1);

        outBirth.setName(NAME_OUT_BIRTH);
        outRotate.setName(NAME_OUT_ROTATE);
        outForward.setName(NAME_OUT_FORWARD);
        outFeelerAngle.setName(NAME_OUT_FEELER_ANGLE);
        outAttack.setName(NAME_OUT_ATTACK);
        outEat.setName(NAME_OUT_EAT);
        outMemory1.setName(NAME_OUT_MEMORY1);

        this.brain = new NeuralNetwork();

        brain.addInputNeuron(inBias);
        brain.addInputNeuron(inFoodValuePosition);
        brain.addInputNeuron(inFoodValueFeeler);
        brain.addInputNeuron(inOcclusionFeeler);
        brain.addInputNeuron(inEnergy);
        brain.addInputNeuron(inAge);
        brain.addInputNeuron(inGeneticDifference);
        brain.addInputNeuron(inWasAttacked);
        brain.addInputNeuron(inWaterOnFeeler);
        brain.addInputNeuron(inWaterOnCreature);
        brain.addInputNeuron(inMemory1);

        brain.generateHiddenNeurons(10);

        brain.addOutputNeuron(outBirth);
        brain.addOutputNeuron(outRotate);
        brain.addOutputNeuron(outForward);
        brain.addOutputNeuron(outFeelerAngle);
        brain.addOutputNeuron(outAttack);
        brain.addOutputNeuron(outEat);
        brain.addOutputNeuron(outMemory1);

        brain.generateFullMesh();

        brain.randomizeAllWeights();
        calculateFeelerPosition();

        color = new Color((float) MathUtil.random.nextDouble(), (float) MathUtil.random.nextDouble(), (float) MathUtil.random.nextDouble());
    }

    public Creature(Level level, Creature mother) {
        id = currentId++;

        this.level = level;
        this.mother = mother;
        generation = mother.generation + 1;
        if(generation > maximumGeneration) {
            maximumGeneration = generation;
        }
        x = mother.x;
        y = mother.y;
        viewAngle = (float) (MathUtil.random.nextDouble() * MathUtil.PI * 2);
        try {
            brain = mother.brain.cloneFullMesh();
        } catch(Exception e) {
            e.printStackTrace();
        }

        inBias = brain.getInputNeuronFromName(NAME_IN_BIAS);
        inFoodValuePosition = brain.getInputNeuronFromName(NAME_IN_FOOD_VALUE_POSITION);
        inFoodValueFeeler = brain.getInputNeuronFromName(NAME_IN_FOOD_VALUE_FEELER);
        inOcclusionFeeler = brain.getInputNeuronFromName(NAME_IN_OCCLUSION_FEELER);
        inEnergy = brain.getInputNeuronFromName(NAME_IN_ENERGY);
        inAge = brain.getInputNeuronFromName(NAME_IN_AGE);
        inGeneticDifference = brain.getInputNeuronFromName(NAME_IN_GENETIC_DIFFERENCE);
        inWasAttacked = brain.getInputNeuronFromName(NAME_IN_WAS_ATTACKED);
        inWaterOnFeeler = brain.getInputNeuronFromName(NAME_IN_WATER_ON_FEELER);
        inWaterOnCreature = brain.getInputNeuronFromName(NAME_IN_WATER_ON_CREATURE);
        inMemory1 = brain.getInputNeuronFromName(NAME_IN_MEMORY1);

        outBirth = brain.getOutputNeuronFromName(NAME_OUT_BIRTH);
        outRotate = brain.getOutputNeuronFromName(NAME_OUT_ROTATE);
        outForward = brain.getOutputNeuronFromName(NAME_OUT_FORWARD);
        outFeelerAngle = brain.getOutputNeuronFromName(NAME_OUT_FEELER_ANGLE);
        outAttack = brain.getOutputNeuronFromName(NAME_OUT_ATTACK);
        outEat = brain.getOutputNeuronFromName(NAME_OUT_EAT);
        outMemory1 = brain.getOutputNeuronFromName(NAME_OUT_MEMORY1);

        calculateFeelerPosition();
        mutateConnections();

        float r = mother.color.getRed() / 255f;
        float g = mother.color.getGreen() / 255f;
        float b = mother.color.getBlue() / 255f;

        color = new Color(r, g, b);
    }

    private void mutateConnections() {
        for(int i = 0; i < 7; i++) {
            brain.randomMutation(0.1f);
        }
    }

    public void readSensors() {
        inMemory1.setValue(outMemory1.getValue());

        brain.invalidate();

        Tile creatureTile = level.getTile((int) x / 16, (int) y / 16);
        Tile feelerTile = level.getTile((int) feelerX / 16, (int) feelerY / 16);

        inBias.setValue(1f);
        if(x / 16 > 0 && x / 16 <= level.width && y / 16 > 0 && y / 16 <= level.height) {
            inFoodValuePosition.setValue(level.foodValues[(int) (x / 16)][(int) (y / 16)] / level.MAXIMUM_FOOD_PER_TILE);
        } else {
            inFoodValuePosition.setValue(0f);
        }
        if(feelerX / 16 > 0 && feelerX / 16 <= level.width && feelerY / 16 > 0 && feelerY / 16 <= level.height) {
            inFoodValueFeeler.setValue(level.foodValues[(int) (x / 16)][(int) (y / 16)] / level.MAXIMUM_FOOD_PER_TILE);
        } else {
            inFoodValueFeeler.setValue(0f);
        }
        inOcclusionFeeler.setValue(0f);
        inEnergy.setValue((energy - MINIMUM_SURVIVAL_ENERGY) / (START_ENERGY - MINIMUM_SURVIVAL_ENERGY));
        inAge.setValue(age / 10f);
        inGeneticDifference.setValue(0f);
        inWasAttacked.setValue(0f);
        inWaterOnFeeler.setValue(feelerTile.isGrass() ? 0f : 1f);
        inWaterOnCreature.setValue(creatureTile.isGrass() ? 0f : 1f);
    }

    public void act() {
        Tile tile = level.getTile((int) x / 16, (int) y / 16);
        float costMult = calculateCostMultiplier(tile);
        actRotate(costMult);
        actMove(costMult);
        actBirth();
        actFeelerRotate();
        actEat(costMult, tile);

        age += level.TIME_PER_TICK;

        if(age > oldestCreatureEver.age) {
            oldestCreatureEver = this;
        }

        if(energy < 100 || Float.isNaN(energy)) {
            kill(tile);
        }
    }

    private void kill(Tile tile) {
        if(tile.isGrass()) {
            if(x / 16 > 0 && x / 16 <= level.width && y / 16 > 0 && y / 16 <= level.height) {
                level.foodValues[(int) (x / 16)][(int) (y / 16)] += energy * FOOD_DROP_PERCENTAGE;
            }
        }
        level.creatureFactory.removeCreature(this);
    }

    private void actRotate(float costMult) {
        float rotateForce = MathUtil.clampNegativePosition(outRotate.getValue());
        viewAngle += rotateForce / 10;
        energy -= MathUtil.abs(rotateForce * COST_ROTATE * costMult);
    }

    private void actMove(float costMult) {
        float forwardX = MathUtil.sin(viewAngle) * MOVE_SPEED;
        float forwardY = MathUtil.cos(viewAngle) * MOVE_SPEED;
        float forwardForce = MathUtil.clampNegativePosition(outForward.getValue());
        forwardX *= forwardForce;
        forwardY *= forwardForce;
        x += forwardX;
        y += forwardY;
        energy -= MathUtil.abs(forwardForce * COST_WALK * costMult);
    }

    private void actBirth() {
        float birthWish = outBirth.getValue();
        if(birthWish > 0) {
            tryToGiveBirth();
        }
    }

    private void actFeelerRotate() {
        feelerAngle = MathUtil.clampNegativePosition(outFeelerAngle.getValue()) * MathUtil.PI;
        calculateFeelerPosition();
    }

    private void actEat(float costMult, Tile creatureTile) {
        float eatWish = MathUtil.clamp(outEat.getValue());
        if(eatWish > 0) {
            eat(eatWish, creatureTile);
            energy -= eatWish * COST_EAT * costMult;
        }
    }

    private void eat(float eatWish, Tile tile) {
        if(x / 16 > 0 && x / 16 <= level.width && y / 16 > 0 && y / 16 <= level.height) {
            if(tile.isGrass()) {
                float foodValue = level.foodValues[(int) x / 16][(int) y / 16];
                if(foodValue > 0) {
                    if(foodValue > GAIN_EAT * eatWish) {
                        energy += GAIN_EAT * eatWish;
                        level.foodValues[(int) x / 16][(int) y / 16] -= GAIN_EAT;
                    } else {
                        energy += foodValue;
                        level.foodValues[(int) x / 16][(int) y / 16] = 0;
                    }
                }
            }
        }
    }

    private void tryToGiveBirth() {
        if(isAbleToGiveBirth()) {
            giveBirth();
        }
    }

    private void giveBirth() {
        Creature child = new TestCreature(level, this);
        children.add(child);
        level.creatureFactory.addCreature(new TestCreature(level, this));
        energy -= START_ENERGY;
    }

    private boolean isAbleToGiveBirth() {
        return energy > START_ENERGY + MINIMUM_SURVIVAL_ENERGY * 1.1f;
    }

    private void calculateFeelerPosition() {
        float angle = feelerAngle + viewAngle;
        float x = MathUtil.sin(angle) * 12;
        float y = MathUtil.cos(angle) * 12;
        feelerX = this.x + x;
        feelerY = this.y + y;
    }

    private float calculateCostMultiplier(Tile creatureTile) {
        return age * (creatureTile.isGrass() ? 1 : 2);
    }

    public abstract void update();

    public abstract void render(Graphics g, int xOffset, int yOffset);

}
