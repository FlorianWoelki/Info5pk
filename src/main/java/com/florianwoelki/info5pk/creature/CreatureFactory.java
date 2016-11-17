package com.florianwoelki.info5pk.creature;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class CreatureFactory {

    private static CreatureFactory instance;

    public static CreatureFactory getInstance() {
        if ( instance == null ) {
            instance = new CreatureFactory();
        }
        return instance;
    }

    private List<Creature> creatures;

    public CreatureFactory() {
        creatures = new ArrayList<>();
    }

    public void addCreature(Creature creature) {
        creatures.add( creature );
    }

    public void render(Graphics g) {
        creatures.forEach( creature -> creature.render( g ) );
    }

    public void update() {
        creatures.forEach( creature -> creature.update() );
    }

}
