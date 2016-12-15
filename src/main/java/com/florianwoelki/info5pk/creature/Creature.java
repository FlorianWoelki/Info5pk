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
    public static Creature oldestCreatureEver = new TestCreature( null, 0, 0, 0 );

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
    protected final float COST_PER_MEMORY_NEURON = 1f;

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

    protected float energy = this.START_ENERGY;
    protected float age = 0;

    protected NeuralNetwork brain;

    protected final String NAME_IN_BIAS = "bias";
    protected final String NAME_IN_FOOD_VALUE_POSITION = "Food Value Position";
    protected final String NAME_IN_FOOD_VALUE_FEELER = "Food Value Feeler";
    protected final String NAME_IN_ENERGY = "Energy";
    protected final String NAME_IN_AGE = "Age";
    protected final String NAME_IN_WATER_ON_FEELER = "Water On Feeler";
    protected final String NAME_IN_WATER_ON_CREATURE = "Water On Creature";
    protected final String NAME_IN_MEMORY = "Input Memory #";

    protected final String NAME_OUT_BIRTH = "Birth";
    protected final String NAME_OUT_ROTATE = "Rotate";
    protected final String NAME_OUT_FORWARD = "Forward";
    protected final String NAME_OUT_FEELER_ANGLE = "Feeler Angle";
    protected final String NAME_OUT_EAT = "Eat";
    protected final String NAME_OUT_MEMORY = "Output Memory #";

    protected InputNeuron inBias = new InputNeuron();
    protected InputNeuron inFoodValuePosition = new InputNeuron();
    protected InputNeuron inFoodValueFeeler = new InputNeuron();
    protected InputNeuron inEnergy = new InputNeuron();
    protected InputNeuron inAge = new InputNeuron();
    protected InputNeuron inWaterOnFeeler = new InputNeuron();
    protected InputNeuron inWaterOnCreature = new InputNeuron();
    protected InputNeuron[] inMemory = null;

    protected WorkingNeuron outBirth = new WorkingNeuron();
    protected WorkingNeuron outRotate = new WorkingNeuron();
    protected WorkingNeuron outForward = new WorkingNeuron();
    protected WorkingNeuron outFeelerAngle = new WorkingNeuron();
    protected WorkingNeuron outEat = new WorkingNeuron();
    protected WorkingNeuron[] outMemory = null;

    public int amountOfMemory = 1;

    protected int amountOfMemory = 1;

    protected Level level;
    public float mouseWheelScale = 1;

    protected Color color;

    public Creature( Level level, float x, float y, float viewAngle ) {
        this.id = currentId++;

        this.level = level;
        this.x = x;
        this.y = y;
        this.viewAngle = viewAngle;

        this.inBias.setName( this.NAME_IN_BIAS );
        this.inFoodValuePosition.setName( this.NAME_IN_FOOD_VALUE_POSITION );
        this.inFoodValueFeeler.setName( this.NAME_IN_FOOD_VALUE_FEELER );
        this.inEnergy.setName( this.NAME_IN_ENERGY );
        this.inAge.setName( this.NAME_IN_AGE );
        this.inWaterOnFeeler.setName( this.NAME_IN_WATER_ON_FEELER );
        this.inWaterOnCreature.setName( this.NAME_IN_WATER_ON_CREATURE );
        this.inMemory = new InputNeuron[this.amountOfMemory];
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.inMemory[i] = new InputNeuron();
            this.inMemory[i].setName( this.NAME_IN_MEMORY + ( i + 1 ) );
        }

        this.outBirth.setName( this.NAME_OUT_BIRTH );
        this.outRotate.setName( this.NAME_OUT_ROTATE );
        this.outForward.setName( this.NAME_OUT_FORWARD );
        this.outFeelerAngle.setName( this.NAME_OUT_FEELER_ANGLE );
        this.outEat.setName( this.NAME_OUT_EAT );
        this.outMemory = new WorkingNeuron[this.amountOfMemory];
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.outMemory[i] = new WorkingNeuron();
            this.outMemory[i].setName( this.NAME_OUT_MEMORY + ( i + 1 ) );
        }

        this.brain = new NeuralNetwork();

        this.brain.addInputNeuron( this.inBias );
        this.brain.addInputNeuron( this.inFoodValuePosition );
        this.brain.addInputNeuron( this.inFoodValueFeeler );
        this.brain.addInputNeuron( this.inEnergy );
        this.brain.addInputNeuron( this.inAge );
        this.brain.addInputNeuron( this.inWaterOnFeeler );
        this.brain.addInputNeuron( this.inWaterOnCreature );
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.brain.addInputNeuron( this.inMemory[i] );
        }

        this.brain.generateHiddenNeurons( 10 );

        this.brain.addOutputNeuron( this.outBirth );
        this.brain.addOutputNeuron( this.outRotate );
        this.brain.addOutputNeuron( this.outForward );
        this.brain.addOutputNeuron( this.outFeelerAngle );
        this.brain.addOutputNeuron( this.outEat );
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.brain.addOutputNeuron( this.outMemory[i] );
        }

        this.brain.generateFullMesh();

        this.brain.randomizeAllWeights();
        this.calculateFeelerPosition();

        this.color = new Color( (float) MathUtil.random.nextDouble(), (float) MathUtil.random.nextDouble(), (float) MathUtil.random.nextDouble() );
    }

    public Creature( Level level, Creature mother ) {
        this.id = currentId++;

        this.level = level;
        this.mother = mother;
        this.generation = mother.generation + 1;
        if ( this.generation > maximumGeneration ) {
            maximumGeneration = this.generation;
        }
        this.x = mother.x;
        this.y = mother.y;
        this.viewAngle = (float) ( MathUtil.random.nextDouble() * MathUtil.PI * 2 );
        try {
            this.brain = mother.brain.cloneFullMesh();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        this.amountOfMemory = mother.amountOfMemory;
<<<<<<< HEAD

        this.inBias = this.brain.getInputNeuronFromName( this.NAME_IN_BIAS );
        this.inFoodValuePosition = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_POSITION );
        this.inFoodValueFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_FEELER );
        this.inEnergy = this.brain.getInputNeuronFromName( this.NAME_IN_ENERGY );
        this.inAge = this.brain.getInputNeuronFromName( this.NAME_IN_AGE );
        this.inWaterOnFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_FEELER );
        this.inWaterOnCreature = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_CREATURE );

        this.outBirth = this.brain.getOutputNeuronFromName( this.NAME_OUT_BIRTH );
        this.outRotate = this.brain.getOutputNeuronFromName( this.NAME_OUT_ROTATE );
        this.outForward = this.brain.getOutputNeuronFromName( this.NAME_OUT_FORWARD );
        this.outFeelerAngle = this.brain.getOutputNeuronFromName( this.NAME_OUT_FEELER_ANGLE );
        this.outEat = this.brain.getOutputNeuronFromName( this.NAME_OUT_EAT );

        this.calculateFeelerPosition();
        this.mutateConnections();
=======
        this.inMemory = new InputNeuron[this.amountOfMemory];
        this.outMemory = new WorkingNeuron[this.amountOfMemory];

        this.setupVariablesFromBrain();
        this.calculateFeelerPosition();

        if ( MathUtil.random.nextFloat() > 0.05f ) {
            this.mutateConnections();
        } else {
            this.mutateMemory();
        }

        this.mutateColor( mother );
        this.generateColorInv();

        this.generateColorInv();
    }

    private void mutateMemory() {
        if ( MathUtil.random.nextFloat() > 0.5f && this.amountOfMemory > 0 ) {
            // Remove a memory neuron
            InputNeuron inRemove = this.inMemory[this.amountOfMemory - 1];
            WorkingNeuron outRemove = this.outMemory[this.amountOfMemory - 1];
            this.brain.removeInputNeuron( inRemove );
            this.brain.removeOutputNeuron( outRemove );
            InputNeuron[] newInputNeurons = new InputNeuron[this.amountOfMemory - 1];
            WorkingNeuron[] newOutputNeurons = new WorkingNeuron[this.amountOfMemory - 1];
            for ( int i = 0; i < this.amountOfMemory - 1; i++ ) {
                newInputNeurons[i] = this.inMemory[i];
                newOutputNeurons[i] = this.outMemory[i];
            }

            this.inMemory = newInputNeurons;
            this.outMemory = newOutputNeurons;
            this.amountOfMemory--;
        } else {
            // Add a memory neuron
            InputNeuron newIn = new InputNeuron();
            WorkingNeuron newOut = new WorkingNeuron();
            newIn.setName( this.NAME_IN_MEMORY + ( this.amountOfMemory + 1 ) );
            newOut.setName( this.NAME_OUT_MEMORY + ( this.amountOfMemory + 1 ) );
            this.brain.addInputNeuronAndMesh( newIn );
            this.brain.addOutputNeuronAndMesh( newOut );
            InputNeuron[] newInputNeurons = new InputNeuron[this.amountOfMemory + 1];
            WorkingNeuron[] newOutputNeurons = new WorkingNeuron[this.amountOfMemory + 1];
            for ( int i = 0; i < this.amountOfMemory; i++ ) {
                newInputNeurons[i] = this.inMemory[i];
                newOutputNeurons[i] = this.outMemory[i];
            }
            newInputNeurons[this.amountOfMemory] = newIn;
            newOutputNeurons[this.amountOfMemory] = newOut;
            this.inMemory = newInputNeurons;
            this.outMemory = newOutputNeurons;
            this.amountOfMemory++;
        }
    }

    private void mutateConnections() {
        for ( int i = 0; i < 10; i++ ) {
            this.brain.randomMutation( 0.1f );
        }
    }
>>>>>>> origin/master

    private void mutateColor( Creature mother ) {
        int r = mother.color.getRed();
        int g = mother.color.getGreen();
        int b = mother.color.getBlue();

        r += -5 + MathUtil.random.nextInt( 6 - -5 + 1 );
        g += -5 + MathUtil.random.nextInt( 6 - -5 + 1 );
        b += -5 + MathUtil.random.nextInt( 6 - -5 + 1 );

        r = MathUtil.clampColorValue( r );
        g = MathUtil.clampColorValue( g );
        b = MathUtil.clampColorValue( b );

        this.color = new Color( r, g, b );
    }

<<<<<<< HEAD
    private void mutateConnections() {
        for ( int i = 0; i < 10; i++ ) {
            this.brain.randomMutation( 0.1f );
=======
    private void generateColorInv() {
        this.colorInv = new Color( 255 - this.color.getRed(), 255 - this.color.getGreen(), 255 - this.color.getBlue() );
    }

    private void setupVariablesFromBrain() {
        this.inBias = this.brain.getInputNeuronFromName( this.NAME_IN_BIAS );
        this.inFoodValuePosition = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_POSITION );
        this.inFoodValueFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_FOOD_VALUE_FEELER );
        this.inEnergy = this.brain.getInputNeuronFromName( this.NAME_IN_ENERGY );
        this.inAge = this.brain.getInputNeuronFromName( this.NAME_IN_AGE );
        this.inWaterOnFeeler = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_FEELER );
        this.inWaterOnCreature = this.brain.getInputNeuronFromName( this.NAME_IN_WATER_ON_CREATURE );
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.inMemory[i] = this.brain.getInputNeuronFromName( this.NAME_IN_MEMORY + ( i + 1 ) );
        }

        this.outBirth = this.brain.getOutputNeuronFromName( this.NAME_OUT_BIRTH );
        this.outRotate = this.brain.getOutputNeuronFromName( this.NAME_OUT_ROTATE );
        this.outForward = this.brain.getOutputNeuronFromName( this.NAME_OUT_FORWARD );
        this.outFeelerAngle = this.brain.getOutputNeuronFromName( this.NAME_OUT_FEELER_ANGLE );
        this.outEat = this.brain.getOutputNeuronFromName( this.NAME_OUT_EAT );
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.outMemory[i] = this.brain.getOutputNeuronFromName( this.NAME_OUT_MEMORY + ( i + 1 ) );
>>>>>>> origin/master
        }
    }

    protected void readSensors() {
        for ( int i = 0; i < this.amountOfMemory; i++ ) {
            this.inMemory[i].setValue( this.outMemory[i].getValue() );
        }

        this.brain.invalidate();

        Tile creatureTile = this.level.getTile( (int) this.x / 16, (int) this.y / 16 );
        Tile feelerTile = this.level.getTile( (int) this.feelerX / 16, (int) this.feelerY / 16 );

        this.inBias.setValue( 1f );
<<<<<<< HEAD
        if ( this.x / 16 > 0 && this.x / 16 <= this.level.width && this.y / 16 > 0 && this.y / 16 <= this.level.height ) {
            this.inFoodValuePosition.setValue( this.level.foodValues[(int) ( this.x / 16 )][(int) ( this.y / 16 )] / this.level.MAXIMUM_FOOD_PER_TILE );
        } else {
            this.inFoodValuePosition.setValue( 0f );
        }
        if ( this.feelerX / 16 > 0 && this.feelerX / 16 <= this.level.width && this.feelerY / 16 > 0 && this.feelerY / 16 <= this.level.height ) {
            this.inFoodValueFeeler.setValue( this.level.foodValues[(int) ( this.x / 16 )][(int) ( this.y / 16 )] / this.level.MAXIMUM_FOOD_PER_TILE );
        } else {
            this.inFoodValueFeeler.setValue( 0f );
        }
=======
        this.inFoodValuePosition.setValue( creatureTile.food / this.level.MAXIMUM_FOOD_PER_TILE );
        this.inFoodValueFeeler.setValue( feelerTile.food / this.level.MAXIMUM_FOOD_PER_TILE );
>>>>>>> origin/master
        this.inEnergy.setValue( ( this.energy - this.MINIMUM_SURVIVAL_ENERGY ) / ( this.START_ENERGY - this.MINIMUM_SURVIVAL_ENERGY ) );
        this.inAge.setValue( this.age / 10f );
        this.inWaterOnFeeler.setValue( feelerTile.isGrass() ? 0f : 1f );
        this.inWaterOnCreature.setValue( creatureTile.isGrass() ? 0f : 1f );
    }

    protected void act() {
        Tile tile = this.level.getTile( (int) this.x / 16, (int) this.y / 16 );
        float costMult = this.calculateCostMultiplier( tile );
        this.actRotate( costMult );
        this.actMove( costMult );
        this.actBirth();
        this.actFeelerRotate();
        this.actEat( costMult, tile );

        this.energy -= this.COST_PERMANENT * this.level.TIME_PER_TICK * costMult;
        this.energy -= this.COST_PER_MEMORY_NEURON * this.level.TIME_PER_TICK * costMult * this.amountOfMemory;

        this.age += this.level.TIME_PER_TICK;

        if ( this.age > oldestCreatureEver.age ) {
            oldestCreatureEver = this;
        }

<<<<<<< HEAD
        if ( this.energy < 100 ) {
=======
        if ( this.energy < 100 || Float.isNaN( this.energy ) ) {
>>>>>>> origin/master
            this.kill( tile );
        }
    }

    private void kill( Tile tile ) {
        this.level.creatureFactory.removeCreature( this );
    }

    private void actRotate( float costMult ) {
        float rotateForce = MathUtil.clampNegativePosition( this.outRotate.getValue() );
        this.viewAngle += rotateForce / 10;
        this.energy -= MathUtil.abs( rotateForce * this.COST_ROTATE * costMult );
    }

    private void actMove( float costMult ) {
        float forwardX = MathUtil.sin( this.viewAngle ) * this.MOVE_SPEED;
        float forwardY = MathUtil.cos( this.viewAngle ) * this.MOVE_SPEED;
        float forwardForce = MathUtil.clampNegativePosition( this.outForward.getValue() );
        forwardX *= forwardForce;
        forwardY *= forwardForce;
        this.x += forwardX;
        this.y += forwardY;
        this.energy -= MathUtil.abs( forwardForce * this.COST_WALK * costMult );
    }

    private void actBirth() {
        float birthWish = this.outBirth.getValue();
        if ( birthWish > 0 ) {
            this.tryToGiveBirth();
        }
    }

    private void actFeelerRotate() {
        this.feelerAngle = MathUtil.clampNegativePosition( this.outFeelerAngle.getValue() ) * MathUtil.PI;
        this.calculateFeelerPosition();
    }

    private void actEat( float costMult, Tile creatureTile ) {
        float eatWish = MathUtil.clamp( this.outEat.getValue() );
        if ( eatWish > 0 ) {
            this.eat( eatWish, creatureTile );
            this.energy -= eatWish * this.COST_EAT * costMult;
        }
    }

    private void eat( float eatWish, Tile tile ) {
        if ( this.x / 16 > 0 && this.x / 16 <= this.level.width && this.y / 16 > 0 && this.y / 16 <= this.level.height ) {
            if ( tile.isGrass() ) {
                float foodValue = this.level.foodValues[(int) this.x / 16][(int) this.y / 16];
                if ( foodValue > 0 ) {
                    if ( foodValue > this.GAIN_EAT * eatWish ) {
                        this.energy += this.GAIN_EAT * eatWish;
                        this.level.foodValues[(int) this.x / 16][(int) this.y / 16] -= this.GAIN_EAT;
                    } else {
                        this.energy += foodValue;
                        this.level.foodValues[(int) this.x / 16][(int) this.y / 16] = 0;
                    }
                }
            }
        }
    }

    private void tryToGiveBirth() {
        if ( this.isAbleToGiveBirth() ) {
            this.giveBirth();
        }
    }

    private void giveBirth() {
        Creature child = new TestCreature( this.level, this );
        this.children.add( child );
        this.level.creatureFactory.addCreature( new TestCreature( this.level, this ) );
        this.energy -= this.START_ENERGY;
    }

    private boolean isAbleToGiveBirth() {
        return this.energy > this.START_ENERGY + this.MINIMUM_SURVIVAL_ENERGY * 1.1f;
    }

    private void calculateFeelerPosition() {
        float angle = this.feelerAngle + this.viewAngle;
        float x = MathUtil.sin( angle ) * 12;
        float y = MathUtil.cos( angle ) * 12;
        this.feelerX = this.x + x;
        this.feelerY = this.y + y;
    }

    private float calculateCostMultiplier( Tile creatureTile ) {
        return this.age * ( creatureTile.isGrass() ? 1 : 2 );
    }

    public abstract void update();

    public abstract void render( Graphics g, int xOffset, int yOffset );

}
