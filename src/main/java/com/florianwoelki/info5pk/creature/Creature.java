package com.florianwoelki.info5pk.creature;

import java.awt.*;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public abstract class Creature {

    protected final int SIZE = 16;

    protected float x;
    protected float y;

    public Creature(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update();

    public abstract void render(Graphics g);

}
