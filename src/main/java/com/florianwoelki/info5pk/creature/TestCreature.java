package com.florianwoelki.info5pk.creature;

import com.florianwoelki.info5pk.level.Level;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class TestCreature extends Creature {

    public TestCreature( Level level, float x, float y, float viewAngle ) {
        super( level, x, y, viewAngle );
    }

    public TestCreature( Creature mother ) {
        super( mother );
    }

    @Override
    public void update() {
        this.readSensors();
        this.act();
    }

    @Override
    public void render( Graphics g, int xOffset, int yOffset ) {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        at.scale( this.mouseWheelScale, this.mouseWheelScale );
        g2d.setTransform( at );

        g2d.setColor( Color.WHITE );
        g2d.fillRect( (int) this.x + xOffset * 16, (int) this.y + yOffset * 16, this.SIZE, this.SIZE );
        g2d.setColor( Color.RED );
        g2d.fillRect( (int) this.feelerX + xOffset * 16, (int) this.feelerY + yOffset * 16, this.FEELER_SIZE, this.FEELER_SIZE );

        g2d.dispose();
    }

}
