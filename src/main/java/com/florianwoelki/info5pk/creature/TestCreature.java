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

    }

    @Override
    public void render(Graphics g) {
        int anim1 = (int) (Math.sin( System.currentTimeMillis() % 2000.0 / 2000 * Math.PI * 2 ) * 20);
        int anim2 = (int) (Math.cos( System.currentTimeMillis() % 2000.0 / 2000 * Math.PI * 2 ) * 20);

        g.setColor( Color.WHITE );
        g.fillRect( (int) x + anim1, (int) y + anim2, SIZE, SIZE );
    }

}
