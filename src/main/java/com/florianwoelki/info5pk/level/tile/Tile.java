package com.florianwoelki.info5pk.level.tile;

import java.awt.*;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Tile {

    public static Tile[] tiles = new Tile[ 256 ];
    public static Tile grass = new GrassTile( 0 );
    public static Tile water = new WaterTile( 1 );

    public final byte id;

    public Tile(int id) {
        this.id = (byte) id;
        if ( tiles[ id ] != null ) throw new RuntimeException( "Duplicate tile ids!" );
        tiles[ id ] = this;
    }

    public void render(Graphics g, int x, int y) {
    }

    public void update() {
    }

}
