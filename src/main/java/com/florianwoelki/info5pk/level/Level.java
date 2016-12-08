package com.florianwoelki.info5pk.level;

import com.florianwoelki.info5pk.creature.CreatureFactory;
import com.florianwoelki.info5pk.creature.TestCreature;
import com.florianwoelki.info5pk.level.generator.LevelGenerator;
import com.florianwoelki.info5pk.level.tile.Tile;
import com.florianwoelki.info5pk.math.MathUtil;

import java.awt.*;

/**
 * Created by Florian Woelki on 19.11.16.
 */
public class Level {

    public int width;
    public int height;

    public byte[] tiles;
    public byte[] data;

    public int grassColor = 141;
    public int dirtColor = 322;
    public int sandColor = 550;

    private CreatureFactory creatureFactory;

    public Level( int width, int height, int level, Level parentLevel ) {
        this.width = width;
        this.height = height;
        this.creatureFactory = new CreatureFactory();
        byte[][] maps;

        this.dirtColor = 222;

        maps = LevelGenerator.createAndValidateTopMap( width, height );

        this.tiles = maps[0];
        this.data = maps[1];

        this.spawnCreatures( 5 );
    }

    private void spawnCreatures( int amount ) {
        for ( int i = 0; i < amount; i++ ) {
            int x;
            int y;

            do {
                x = MathUtil.random.nextInt( this.width );
                y = MathUtil.random.nextInt( this.height );
            } while ( this.getTile( x, y ) == Tile.water );

            this.creatureFactory.addCreature( new TestCreature( this, x * 16, y * 16, 0 ) );
        }
    }

    public void render( Graphics g, int xOffset, int yOffset, float mouseWheelScale ) {
        for ( int y = 0; y <= this.height; y++ ) {
            for ( int x = 0; x <= this.width; x++ ) {
                Tile tile = this.getTile( x, y );
                tile.mouseWheelScale = mouseWheelScale;
                tile.render( g, this, x + xOffset, y + yOffset );
            }
        }

        this.creatureFactory.render( g, xOffset, yOffset, mouseWheelScale );
    }

    public void update() {
        for ( int y = 0; y <= this.height; y++ ) {
            for ( int x = 0; x <= this.width; x++ ) {
                this.getTile( x, y ).update();
            }
        }

        this.creatureFactory.update();
    }

    public Tile getTile( int x, int y ) {
        if ( x < 0 || y < 0 || x >= this.width || y >= this.height ) return Tile.water;
        return Tile.tiles[this.tiles[x + y * this.width]];
    }

    public CreatureFactory getCreatureFactory() {
        return this.creatureFactory;
    }

}
