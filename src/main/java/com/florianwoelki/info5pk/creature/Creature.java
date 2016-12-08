package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.math.MathUtil;
import com.florianwoelki.info5pk.neuralnetwork.NeuralNetwork;
import com.florianwoelki.info5pk.neuralnetwork.neuron.InputNeuron;
import com.florianwoelki.info5pk.neuralnetwork.neuron.WorkingNeuron;

import java.awt.*;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Creature {

    protected final float START_ENERGY = 150;
    protected final float MINIMUM_SURVIVAL_ENERGY = 100;

    protected final int SIZE = 16;
    protected final int FEELER_SIZE = 10;

    protected float x;
    protected float y;
    protected float viewAngle;

    protected float feelerX;
    protected float feelerY;
    protected float feelerAngle;

    protected float energy = 150;
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

    protected final String NAME_OUT_BIRTH = "Birth";
    protected final String NAME_OUT_ROTATE = "Rotate";
    protected final String NAME_OUT_FORWARD = "Forward";
    protected final String NAME_OUT_FEELER_ANGLE = "Feeler Angle";
    protected final String NAME_OUT_ATTACK = "Attack";
    protected final String NAME_OUT_EAT = "Eat";

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

    protected WorkingNeuron outBirth = new WorkingNeuron();
    protected WorkingNeuron outRotate = new WorkingNeuron();
    protected WorkingNeuron outForward = new WorkingNeuron();
    protected WorkingNeuron outFeelerAngle = new WorkingNeuron();
    protected WorkingNeuron outAttack = new WorkingNeuron();
    protected WorkingNeuron outEat = new WorkingNeuron();

    public Creature( float x, float y, float viewAngle ) {
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;

        this.inBias.setName( this.NAME_IN_BIAS );
        this.inFoodValuePosition.setName( this.NAME_IN_FOOD_VALUE_POSITION );
        this.inFoodValueFeeler.setName( this.NAME_IN_FOOD_VALUE_FEELER );
        this.inOcclusionFeeler.setName( this.NAME_IN_OCCLUSION_FEELER );
        this.inEnergy.setName( this.NAME_IN_ENERGY );
        this.inAge.setName( this.NAME_IN_AGE );
        this.inGeneticDifference.setName( this.NAME_IN_GENETIC_DIFFERENCE );
        this.inWasAttacked.setName( this.NAME_IN_WAS_ATTACKED );
        this.inWaterOnFeeler.setName( this.NAME_IN_WATER_ON_FEELER );
        this.inWaterOnCreature.setName( this.NAME_IN_WATER_ON_CREATURE );

        this.outBirth.setName( this.NAME_OUT_BIRTH );
        this.outRotate.setName( this.NAME_OUT_ROTATE );
        this.outForward.setName( this.NAME_OUT_FORWARD );
        this.outFeelerAngle.setName( this.NAME_OUT_FEELER_ANGLE );
        this.outAttack.setName( this.NAME_OUT_ATTACK );
        this.outEat.setName( this.NAME_OUT_EAT );

        this.brain = new NeuralNetwork();

        this.brain.addInputNeuron( this.inBias );
        this.brain.addInputNeuron( this.inFoodValuePosition );
        this.brain.addInputNeuron( this.inFoodValueFeeler );
        this.brain.addInputNeuron( this.inOcclusionFeeler );
        this.brain.addInputNeuron( this.inEnergy );
        this.brain.addInputNeuron( this.inAge );
        this.brain.addInputNeuron( this.inGeneticDifference );
        this.brain.addInputNeuron( this.inWasAttacked );
        this.brain.addInputNeuron( this.inWaterOnFeeler );
        this.brain.addInputNeuron( this.inWaterOnCreature );

        this.brain.generateHiddenNeurons( 10 );

        this.brain.addOutputNeuron( this.outBirth );
        this.brain.addOutputNeuron( this.outRotate );
        this.brain.addOutputNeuron( this.outForward );
        this.brain.addOutputNeuron( this.outFeelerAngle );
        this.brain.addOutputNeuron( this.outAttack );
        this.brain.addOutputNeuron( this.outEat );

        this.brain.generateFullMesh();

        this.brain.randomizeAllWeights();

        this.calculateFeelerPosition();
    }

    public Creature( Creature mother ) {
        this.x = mother.x;
        this.y = mother.y;
        this.viewAngle = (float) ( MathUtil.random.nextDouble() * MathUtil.PI * 2 );
        try {
            this.brain = mother.brain.cloneFullMesh();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        this.inBias = this.brain.getInputNeuronFromName( this.NAME_IN_BIAS );
        this.inFoodValuePosition = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_POSITION );
        this.inFoodValueFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_FEELER );
        this.inOcclusionFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_OCCLUSION_FEELER );
        this.inEnergy = this.brain.getInputNeuronFromName( this.NAME_IN_ENERGY );
        this.inAge = this.brain.getInputNeuronFromName( this.NAME_IN_AGE );
        this.inGeneticDifference = this.brain.getInputNeuronFromName( this.NAME_IN_GENETIC_DIFFERENCE );
        this.inWasAttacked = this.brain.getInputNeuronFromName( this.NAME_IN_WAS_ATTACKED );
        this.inWaterOnFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_FEELER );
        this.inWaterOnCreature = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_CREATURE );

        this.outBirth = this.brain.getOutputNeuronFromName( this.NAME_OUT_BIRTH );
        this.outRotate = this.brain.getOutputNeuronFromName( this.NAME_OUT_ROTATE );
        this.outForward = this.brain.getOutputNeuronFromName( this.NAME_OUT_FORWARD );
        this.outFeelerAngle = this.brain.getOutputNeuronFromName( this.NAME_OUT_FEELER_ANGLE );
        this.outAttack = this.brain.getOutputNeuronFromName( this.NAME_OUT_ATTACK );
        this.outEat = this.brain.getOutputNeuronFromName( this.NAME_OUT_EAT );

        this.calculateFeelerPosition();
    }

    public void readSensors() {
        this.brain.invalidate();

        this.inBias.setValue( 1 );
        this.inFoodValuePosition.setValue( 0 ); // TODO: Find real value
        this.inFoodValueFeeler.setValue( 0 ); // TODO: Find real value
        this.inOcclusionFeeler.setValue( 0 ); // TODO: Find real value
        this.inEnergy.setValue( ( this.energy - this.MINIMUM_SURVIVAL_ENERGY ) / ( this.START_ENERGY - this.MINIMUM_SURVIVAL_ENERGY ) );
        this.inAge.setValue( this.age );
        this.inGeneticDifference.setValue( 0 ); // TODO: Find real value
        this.inWasAttacked.setValue( 0 ); // TODO: Find real value
        this.inWaterOnFeeler.setValue( 0 ); // TODO: Find real value
        this.inWaterOnCreature.setValue( 0 ); // TODO: Find real value
    }

    public void act() {
        float rotateForce = MathUtil.clampNegativePosition( this.outRotate.getValue() );
        this.viewAngle += rotateForce / 10;

        float forwardX = MathUtil.sin( this.viewAngle );
        float forwardY = MathUtil.cos( this.viewAngle );
        float forwardForce = MathUtil.clampNegativePosition( this.outForward.getValue() );
        forwardX *= forwardForce;
        forwardY *= forwardForce;

        this.x += forwardX;
        this.y += forwardY;

        float birthWish = this.outBirth.getValue();
        if ( birthWish > 0 ) this.tryToGiveBirth();

        this.feelerAngle = MathUtil.clampNegativePosition( this.outFeelerAngle.getValue() ) * MathUtil.PI;
        this.calculateFeelerPosition();
    }

    public void tryToGiveBirth() {
        if ( this.isAbleToGiveBirth() ) {
            this.giveBirth();
        }
    }

    public void giveBirth() {
        CreatureFactory.getInstance().addCreature( new TestCreature( this ) );
        this.energy -= this.START_ENERGY;
    }

    public boolean isAbleToGiveBirth() {
        return this.energy > this.START_ENERGY + this.MINIMUM_SURVIVAL_ENERGY * 1.1f;
    }

    public void calculateFeelerPosition() {
        float angle = this.feelerAngle + this.viewAngle;
        float x = MathUtil.sin( angle ) * 100;
        float y = MathUtil.cos( angle ) * 100;
        this.feelerX = this.x + x;
        this.feelerY = this.y + y;
    }

    public abstract void update();

    public abstract void render( Graphics g );

}
