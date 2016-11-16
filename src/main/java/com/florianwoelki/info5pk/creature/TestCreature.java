package com.florianwoelki.info5pk.creature;

import java.awt.*;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class TestCreature extends Creature {

    public TestCreature(float x, float y) {
        super( x, y );
    }

    @Override
    public void update() {
        int anim1 = (int) (Math.sin( System.currentTimeMillis() % 1000.0 / 1000 * Math.PI * 2 ) * 3);
        int anim2 = (int) (Math.cos( System.currentTimeMillis() % 1000.0 / 1000 * Math.PI * 2 ) * 3);

        x += anim1;
        y += anim2;
    }

    @Override
    public void render(Graphics g) {
        g.setColor( Color.WHITE );
        g.fillRect( (int) x, (int) y, SIZE, SIZE );
    }

}
