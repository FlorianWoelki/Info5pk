package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.math.MathUtil;
import com.florianwoelki.info5pk.neuronalnetwork.InputNeuron;
import com.florianwoelki.info5pk.neuronalnetwork.NeuronalNetwork;
import com.florianwoelki.info5pk.neuronalnetwork.WorkingNeuron;

import java.awt.*;
import java.util.Random;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Creature {

    protected final float START_ENERGY = 150;
    protected final float MINIMUM_SURVIVAL_ENERGY = 100;

    protected final int SIZE = 16;

    protected float x;
    protected float y;
    protected float viewAngle;

    protected float feelerX;
    protected float feelerY;
    protected float feelerAngle;

    protected float energy = 150;
    protected float age = 0;

    protected NeuronalNetwork brain;

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

    private Random random = new Random();

    public Creature(float x, float y, float viewAngle) {
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;

        inBias.setName( NAME_IN_BIAS );
        inFoodValuePosition.setName( NAME_IN_FOOD_VALUE_POSITION );
        inFoodValueFeeler.setName( NAME_IN_FOOD_VALUE_FEELER );
        inOcclusionFeeler.setName( NAME_IN_OCCLUSION_FEELER );
        inEnergy.setName( NAME_IN_ENERGY );
        inAge.setName( NAME_IN_AGE );
        inGeneticDifference.setName( NAME_IN_GENETIC_DIFFERENCE );
        inWasAttacked.setName( NAME_IN_WAS_ATTACKED );
        inWaterOnFeeler.setName( NAME_IN_WATER_ON_FEELER );
        inWaterOnCreature.setName( NAME_IN_WATER_ON_CREATURE );

        outBirth.setName( NAME_OUT_BIRTH );
        outRotate.setName( NAME_OUT_ROTATE );
        outForward.setName( NAME_OUT_FORWARD );
        outFeelerAngle.setName( NAME_OUT_FEELER_ANGLE );
        outAttack.setName( NAME_OUT_ATTACK );
        outEat.setName( NAME_OUT_EAT );

        brain = new NeuronalNetwork();

        brain.addInputNeuron( inBias );
        brain.addInputNeuron( inFoodValuePosition );
        brain.addInputNeuron( inFoodValueFeeler );
        brain.addInputNeuron( inOcclusionFeeler );
        brain.addInputNeuron( inEnergy );
        brain.addInputNeuron( inAge );
        brain.addInputNeuron( inGeneticDifference );
        brain.addInputNeuron( inWasAttacked );
        brain.addInputNeuron( inWaterOnFeeler );
        brain.addInputNeuron( inWaterOnCreature );

        brain.generateHiddenNeurons( 10 );

        brain.addOutputNeuron( outBirth );
        brain.addOutputNeuron( outRotate );
        brain.addOutputNeuron( outForward );
        brain.addOutputNeuron( outFeelerAngle );
        brain.addOutputNeuron( outAttack );
        brain.addOutputNeuron( outEat );

        brain.generateFullMesh();

        brain.randomizeAllWeights();
        calculateFeelerPosition();
    }

    public Creature(Creature mother) {
        this.x = mother.x;
        this.y = mother.y;
        this.viewAngle = (float) (random.nextDouble() * MathUtil.PI * 2);
        try {
            this.brain = mother.brain.cloneFullMesh();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        inBias = brain.getInputNeuronFromName( NAME_IN_BIAS );
        inFoodValuePosition = brain.getInputNeuronFromName( NAME_IN_FOOD_VALUE_POSITION );
        inFoodValueFeeler = brain.getInputNeuronFromName( NAME_IN_FOOD_VALUE_FEELER );
        inOcclusionFeeler = brain.getInputNeuronFromName( NAME_IN_OCCLUSION_FEELER );
        inEnergy = brain.getInputNeuronFromName( NAME_IN_ENERGY );
        inAge = brain.getInputNeuronFromName( NAME_IN_AGE );
        inGeneticDifference = brain.getInputNeuronFromName( NAME_IN_GENETIC_DIFFERENCE );
        inWasAttacked = brain.getInputNeuronFromName( NAME_IN_WAS_ATTACKED );
        inWaterOnFeeler = brain.getInputNeuronFromName( NAME_IN_WATER_ON_FEELER );
        inWaterOnCreature = brain.getInputNeuronFromName( NAME_IN_WATER_ON_CREATURE );

        outBirth = brain.getOutputNeuronFromName( NAME_OUT_BIRTH );
        outRotate = brain.getOutputNeuronFromName( NAME_OUT_ROTATE );
        outForward = brain.getOutputNeuronFromName( NAME_OUT_FORWARD );
        outFeelerAngle = brain.getOutputNeuronFromName( NAME_OUT_FEELER_ANGLE );
        outAttack = brain.getOutputNeuronFromName( NAME_OUT_ATTACK );
        outEat = brain.getOutputNeuronFromName( NAME_OUT_EAT );

        calculateFeelerPosition();
    }

    public void readSensors() {
        brain.invalidate();

        inBias.setValue( 1 );
        inFoodValuePosition.setValue( 0 ); // TODO: Find real value
        inFoodValueFeeler.setValue( 0 ); // TODO: Find real value
        inOcclusionFeeler.setValue( 0 ); // TODO: Find real value
        inEnergy.setValue( (energy - MINIMUM_SURVIVAL_ENERGY) / (START_ENERGY - MINIMUM_SURVIVAL_ENERGY) );
        inAge.setValue( age );
        inGeneticDifference.setValue( 0 ); // TODO: Find real value
        inWasAttacked.setValue( 0 ); // TODO: Find real value
        inWaterOnFeeler.setValue( 0 ); // TODO: Find real value
        inWaterOnCreature.setValue( 0 ); // TODO: Find real value
    }

    public void act() {
        float rotateForce = MathUtil.clampNegativePosition( outRotate.getValue() );
        viewAngle += rotateForce / 10;

        float forwardX = MathUtil.sin( viewAngle );
        float forwardY = MathUtil.cos( viewAngle );
        float forwardForce = MathUtil.clampNegativePosition( outForward.getValue() );
        forwardX *= forwardForce;
        forwardY *= forwardForce;

        x += forwardX;
        y += forwardY;

        float birthWish = outBirth.getValue();
        if ( birthWish > 0 ) tryToGiveBirth();

        feelerAngle = MathUtil.clampNegativePosition( outFeelerAngle.getValue() ) * MathUtil.PI;
        calculateFeelerPosition();
    }

    public void tryToGiveBirth() {
        if ( isAbleToGiveBirth() ) {
            giveBirth();
        }
    }

    public void giveBirth() {
        CreatureFactory.getInstance().addCreature( new TestCreature( this ) );
        energy -= START_ENERGY;
    }

    public boolean isAbleToGiveBirth() {
        return energy > START_ENERGY + MINIMUM_SURVIVAL_ENERGY * 1.1f;
    }

    public void calculateFeelerPosition() {
        float angle = feelerAngle + viewAngle;
        float x = MathUtil.sin( angle ) * 100;
        float y = MathUtil.cos( angle ) * 100;
        feelerX = this.x + x;
        feelerY = this.y + y;
    }

    public abstract void update();

    public abstract void render(Graphics g);

}
