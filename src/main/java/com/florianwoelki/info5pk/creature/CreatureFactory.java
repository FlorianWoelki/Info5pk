package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.level.Level;
import com.florianwoelki.info5pk.level.tile.Tile;
import com.florianwoelki.info5pk.math.MathUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class CreatureFactory {

    public static float[] averageAgeOfLastCreatures = new float[128];
    private int indexForAverageAgeOfLastCreatures = 0;
    private boolean averageAgeOfLastCreaturesAccurate = false;

    private List<Creature> creatures;
    private List<Creature> toRemoveCreatures;
    private List<Creature> toSpawnCreatures;

    public List<Float> aliveCreaturesRecord = new ArrayList<>();
    public List<Float> averageDeathAgeRecord = new ArrayList<>();

    private Creature oldestCreatureAlive;
    private Creature selectedCreature;

    private int numberOfDeaths;
    private float year;

    private Level level;

    public CreatureFactory( Level level ) {
        this.level = level;
        this.creatures = new ArrayList<>();
        this.toRemoveCreatures = new ArrayList<>();
        this.toSpawnCreatures = new ArrayList<>();
    }

    public void addCreature( Creature creature ) {
        this.toSpawnCreatures.add( creature );
    }

    public void removeCreature( Creature creature ) {
        this.toRemoveCreatures.add( creature );
    }

    public void render( Graphics g, int xOffset, int yOffset, float mouseWheelScale ) {
        for ( Creature creature : this.creatures ) {
            creature.mouseWheelScale = mouseWheelScale;
            creature.render( g, xOffset, yOffset );

        }

        this.renderGeneralStats( g );
    }

    private void renderGeneralStats( Graphics g ) {
        g.setColor( new Color( 0, 0, 0, 0.5f ) );
        g.fillRect( 0, 0, 300, 600 );
        g.setColor( Color.RED );
        g.drawString( "Creatures: " + this.creatures.size(), 20, 20 );
        g.drawString( "Deaths: " + this.numberOfDeaths, 20, 40 );
        g.drawString( "Maximum Generation: " + Creature.maximumGeneration, 20, 60 );
        g.drawString( "Year: " + this.year, 20, 80 );
        g.drawString( "Oldest Creature Ever: " + Creature.oldestCreatureEver.age, 20, 100 );
        g.drawString( "Oldest Creature Alive: " + this.oldestCreatureAlive.age, 20, 120 );
        if ( this.averageAgeOfLastCreaturesAccurate ) {
            float averageDeathAge = calculateAverageAgeOfLastDeadCreatures();
            this.averageDeathAgeRecord.add( averageDeathAge );
            g.setColor( Color.RED );
            g.drawString( "Average Death Age: " + averageDeathAge, 20, 140 );
        }

        if ( this.selectedCreature != null ) {
            g.setColor( new Color( 0, 0, 0, 0.5f ) );
            g.fillRect( 800, 0, 500, 450 + this.selectedCreature.amountOfMemory * this.selectedCreature.SIZE );
            g.setColor( Color.RED );
            g.drawString( "Selected Creature: ", 820, 20 );
            g.drawString( "Age: " + this.selectedCreature.age, 820, 40 );
            g.drawString( "Energy: " + this.selectedCreature.energy, 820, 60 );
            g.drawString( "Children Count: " + this.selectedCreature.children.size(), 820, 80 );
            g.drawString( "Generation: " + this.selectedCreature.generation, 820, 100 );
            g.drawString( "Alive: " + ( this.selectedCreature.energy > 100 ? "Alive" : "Dead" ), 820, 120 );
            this.selectedCreature.brain.render( g, new Rectangle( 950, 160, 200, 250 + this.selectedCreature.amountOfMemory * this.selectedCreature.SIZE ) );
        }
    }

    public void update() {
        if ( this.creatures.size() < 50 ) {
            int x;
            int y;

            do {
                x = MathUtil.random.nextInt( this.level.width );
                y = MathUtil.random.nextInt( this.level.height );
            } while ( this.level.getTile( x, y ) == Tile.water );

            TestCreature creature = new TestCreature( this.level, x * 16, y * 16, (float) ( MathUtil.random.nextDouble() * MathUtil.PI * 2 ) );
            this.addCreature( creature );
        }

        for ( Creature creature : this.creatures ) {
            creature.update();
        }

        this.numberOfDeaths += this.toRemoveCreatures.size();
        for ( Creature creature : this.toRemoveCreatures ) {
            this.addDeathAge( creature.age );
            this.creatures.remove( creature );
        }
        this.toRemoveCreatures.clear();

        for ( Creature creature : this.toSpawnCreatures ) {
            this.creatures.add( creature );
        }
        this.toSpawnCreatures.clear();

        this.year += this.level.TIME_PER_TICK;

        if ( this.creatures.size() > 0 ) {
            this.oldestCreatureAlive = this.creatures.get( 0 );
            for ( Creature creature : this.creatures ) {
                if ( creature.age > this.oldestCreatureAlive.age ) {
                    this.oldestCreatureAlive = creature;
                }
            }
        }

        this.selectedCreature = this.oldestCreatureAlive;
    }

    private void addDeathAge( float age ) {
        CreatureFactory.averageAgeOfLastCreatures[this.indexForAverageAgeOfLastCreatures++] = age;
        if ( this.indexForAverageAgeOfLastCreatures >= CreatureFactory.averageAgeOfLastCreatures.length ) {
            this.indexForAverageAgeOfLastCreatures = 0;
            this.averageAgeOfLastCreaturesAccurate = true;
        }
    }

    private float calculateAverageAgeOfLastDeadCreatures() {
        float ageAverage = 0;
        for ( int i = 0; i < averageAgeOfLastCreatures.length; i++ ) {
            ageAverage += averageAgeOfLastCreatures[i];
        }
        return ageAverage / averageAgeOfLastCreatures.length;
    }

}
