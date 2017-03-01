package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.level.Level;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class TestCreature extends Creature {

    public TestCreature(Level level, float x, float y, float viewAngle) {
        super(level, x, y, viewAngle);
    }

    public TestCreature(Level level, Creature mother) {
        super(level, mother);
    }

    @Override
    public void update() {
        readSensors();
        act();
    }

    @Override
    public void render(Graphics g, int xOffset, int yOffset) {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        at.scale(mouseWheelScale, mouseWheelScale);
        g2d.setTransform(at);

        g2d.setColor(Color.WHITE);
        g2d.drawLine((int) x + xOffset * 16, (int) y + yOffset * 16, (int) feelerX + xOffset * 16, (int) feelerY + yOffset * 16);

        g2d.setColor(this.color);
        g2d.fillRect((int) x + xOffset * 16 - 4, (int) y + yOffset * 16 - 4, SIZE, SIZE);
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int) feelerX + xOffset * 16 - 2, (int) feelerY + yOffset * 16 - 2, FEELER_SIZE, FEELER_SIZE);

        g2d.dispose();
    }

}
