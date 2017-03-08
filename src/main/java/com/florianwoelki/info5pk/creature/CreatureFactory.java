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

    public CreatureFactory(Level level) {
        this.level = level;
        this.creatures = new ArrayList<>();
        this.toRemoveCreatures = new ArrayList<>();
        this.toSpawnCreatures = new ArrayList<>();
    }

    public void addCreature(Creature creature) {
        toSpawnCreatures.add(creature);
    }

    public void removeCreature(Creature creature) {
        toRemoveCreatures.add(creature);
    }

    public void render(Graphics g, int xOffset, int yOffset, float mouseWheelScale) {
        for(Creature creature : creatures) {
            creature.mouseWheelScale = mouseWheelScale;
            creature.render(g, xOffset, yOffset);

        }

        this.renderGeneralStats(g);
    }

    private void renderGeneralStats(Graphics g) {
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(0, 0, 300, 600);
        g.setColor(Color.RED);
        g.drawString("Creatures: " + creatures.size(), 20, 20);
        g.drawString("Deaths: " + numberOfDeaths, 20, 40);
        g.drawString("Maximum Generation: " + Creature.maximumGeneration, 20, 60);
        g.drawString("Year: " + year, 20, 80);
        g.drawString("Oldest Creature Ever: " + Creature.oldestCreatureEver.age, 20, 100);
        g.drawString("Oldest Creature Alive: " + oldestCreatureAlive.age, 20, 120);
        if(averageAgeOfLastCreaturesAccurate) {
            float averageDeathAge = calculateAverageAgeOfLastDeadCreatures();
            averageDeathAgeRecord.add(averageDeathAge);
            g.setColor(Color.RED);
            g.drawString("Average Death Age: " + averageDeathAge, 20, 140);
        }

        if(this.selectedCreature != null) {
            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(800, 0, 500, 450);
            g.setColor(Color.RED);
            g.drawString("Selected Creature: ", 820, 20);
            g.drawString("Age: " + selectedCreature.age, 820, 40);
            g.drawString("Energy: " + selectedCreature.energy, 820, 60);
            g.drawString("Children Count: " + selectedCreature.children.size(), 820, 80);
            g.drawString("Generation: " + selectedCreature.generation, 820, 100);
            g.drawString("Alive: " + (selectedCreature.energy > 100 ? "Alive" : "Dead"), 820, 120);
            g.drawString("Birth Output: " + selectedCreature.outBirth.getValue(), 1020, 40);
            g.drawString("Eat Output: " + selectedCreature.outEat.getValue(), 1020, 60);
            g.drawString("FeelerAngle Output: " + selectedCreature.outFeelerAngle.getValue(), 1020, 80);
            g.drawString("Forward Output: " + selectedCreature.outForward.getValue(), 1020, 100);
            g.drawString("Rotate Output: " + selectedCreature.outRotate.getValue(), 1020, 120);
            selectedCreature.brain.render(g, new Rectangle(950, 160, 200, 250));
        }
    }

    public void update() {
        if(creatures.size() < 50) {
            int x;
            int y;

            do {
                x = MathUtil.random.nextInt(level.width);
                y = MathUtil.random.nextInt(level.height);
            } while(level.getTile(x, y) == Tile.water);

            TestCreature creature = new TestCreature(level, x * 16, y * 16, (float) (MathUtil.random.nextDouble() * MathUtil.PI * 2));
            addCreature(creature);
        }

        for(Creature creature : this.creatures) {
            creature.update();
        }

        numberOfDeaths += toRemoveCreatures.size();
        for(Creature creature : toRemoveCreatures) {
            addDeathAge(creature.age);
            creatures.remove(creature);
        }
        toRemoveCreatures.clear();

        for(Creature creature : toSpawnCreatures) {
            creatures.add(creature);
        }
        toSpawnCreatures.clear();

        year += level.TIME_PER_TICK;

        if(creatures.size() > 0) {
            oldestCreatureAlive = creatures.get(0);
            for(Creature creature : creatures) {
                if(creature.age > oldestCreatureAlive.age) {
                    oldestCreatureAlive = creature;
                }
            }
        }

        selectedCreature = oldestCreatureAlive;
    }

    private void addDeathAge(float age) {
        averageAgeOfLastCreatures[indexForAverageAgeOfLastCreatures++] = age;
        if(indexForAverageAgeOfLastCreatures >= averageAgeOfLastCreatures.length) {
            indexForAverageAgeOfLastCreatures = 0;
            averageAgeOfLastCreaturesAccurate = true;
        }
    }

    private float calculateAverageAgeOfLastDeadCreatures() {
        float ageAverage = 0;
        for(float averageAgeOfLastCreature : averageAgeOfLastCreatures) {
            ageAverage += averageAgeOfLastCreature;
        }
        return ageAverage / averageAgeOfLastCreatures.length;
    }

}
