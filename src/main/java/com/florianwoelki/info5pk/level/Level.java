package com.florianwoelki.info5pk.level;

import com.florianwoelki.info5pk.creature.Creature;
import com.florianwoelki.info5pk.level.generator.LevelGenerator;
import com.florianwoelki.info5pk.level.tile.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    private List<Creature> creatues = new ArrayList<>();

    public Level( int width, int height, int level, Level parentLevel ) {
        this.width = width;
        this.height = height;
        byte[][] maps;

        this.dirtColor = 222;

        maps = LevelGenerator.createAndValidateTopMap( width, height );

        this.tiles = maps[0];
        this.data = maps[1];
    }

    public void render( Graphics g, int xOffset, int yOffset, float mouseWheelScale ) {
        for ( int y = 0; y <= this.height; y++ ) {
            for ( int x = 0; x <= this.width; x++ ) {
                Tile tile = this.getTile( x, y );
                tile.mouseWheelScale = mouseWheelScale;
                tile.render( g, this, x + xOffset, y + yOffset );
            }
        }
    }

    public void update() {
        for ( int y = 0; y <= this.height; y++ ) {
            for ( int x = 0; x <= this.width; x++ ) {
                this.getTile( x, y ).update();
            }
        }
    }

    public Tile getTile( int x, int y ) {
        if ( x < 0 || y < 0 || x >= this.width || y >= this.height ) return Tile.water;
        return Tile.tiles[this.tiles[x + y * this.width]];
    }

}
