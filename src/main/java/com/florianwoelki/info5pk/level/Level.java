package com.florianwoelki.info5pk.level;

import com.florianwoelki.info5pk.creature.CreatureFactory;
import com.florianwoelki.info5pk.level.generator.LevelGenerator;
import com.florianwoelki.info5pk.level.tile.Tile;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Florian Woelki on 19.11.16.
 */
public class Level {

    public final float TIME_PER_TICK = 0.01f;
    public final float MAXIMUM_FOOD_PER_TILE = 100;

    public int width;
    public int height;

    public byte[] tiles;
    public byte[] data;
    public float[][] foodValues;

    public int grassColor = 141;
    public int dirtColor = 322;
    public int sandColor = 550;

    public java.util.List<Float> foodRecord = new ArrayList<>();

    public CreatureFactory creatureFactory;

    public Level( int width, int height, int level, Level parentLevel ) {
        this.width = width;
        this.height = height;
        this.creatureFactory = new CreatureFactory( this );
        byte[][] maps;

        this.dirtColor = 222;

        maps = LevelGenerator.createAndValidateTopMap( width, height );

        this.tiles = maps[0];
        this.data = maps[1];
        this.foodValues = new float[this.tiles.length][this.tiles.length];

        for ( int y = 0; y < height; y++ ) {
            for ( int x = 0; x < width; x++ ) {
                this.foodValues[x][y] = 0f;
            }
        }
    }

    public void render( Graphics g, int xOffset, int yOffset, float mouseWheelScale ) {
        for ( int y = 0; y < this.height; y++ ) {
            for ( int x = 0; x < this.width; x++ ) {
                Tile tile = this.getTile( x, y );
                tile.mouseWheelScale = mouseWheelScale;

                if ( tile.isGrass() ) {
                    float alpha = this.foodValues[x][y] / 100.0f;
                    if ( alpha < 0f ) {
                        alpha = 0;
                    } else if ( alpha > 1f ) {
                        alpha = 1f;
                    }

                    Color color = new Color( 0f, 1f, 0f, alpha );

                    g.setColor( color );
                }

                tile.render( g, this, x + xOffset, y + yOffset );
            }
        }

        this.creatureFactory.render( g, xOffset, yOffset, mouseWheelScale );
    }

    public void update() {
        for ( int y = 0; y < this.height; y++ ) {
            for ( int x = 0; x < this.width; x++ ) {
                this.getTile( x, y ).update();
            }
        }

        for ( int y = 0; y < this.height; y++ ) {
            for ( int x = 0; x < this.width; x++ ) {
                if ( this.isFertile( x, y ) ) {
                    this.grow( x, y );
                }
            }
        }

        this.creatureFactory.update();

        this.foodRecord.add( this.calculateFoodAvailable() );
    }

    private void grow( int x, int y ) {
        this.foodValues[x][y] += 0.2f;
        if ( this.foodValues[x][y] > this.MAXIMUM_FOOD_PER_TILE ) {
            this.foodValues[x][y] = this.MAXIMUM_FOOD_PER_TILE;
        }
    }

    private boolean isFertileToNeighbors( int x, int y ) {
        if ( x < 0 || y < 0 || x >= this.width || y >= this.height ) {
            return false;
        }
        if ( this.getTile( x, y ).isWater() ) {
            return true;
        }
        if ( this.getTile( x, y ).isGrass() && this.foodValues[x][y] > 50 ) {
            return true;
        }
        return false;
    }

    private boolean isFertile( int x, int y ) {
        if ( this.getTile( x, y ).isGrass() ) {
            if ( this.foodValues[x][y] > 50 ) {
                return true;
            }
            if ( this.isFertileToNeighbors( x - 1, y ) ) {
                return true;
            }
            if ( this.isFertileToNeighbors( x + 1, y ) ) {
                return true;
            }
            if ( this.isFertileToNeighbors( x, y - 1 ) ) {
                return true;
            }
            if ( this.isFertileToNeighbors( x, y + 1 ) ) {
                return true;
            }
        }
        return false;
    }

    public float calculateFoodAvailable() {
        float food = 0;
        for ( int y = 0; y < this.height; y++ ) {
            for ( int x = 0; x < this.width; x++ ) {
                food += this.foodValues[x][y];
            }
        }
        return food;
    }

    public void setTile( int x, int y, Tile tile ) {
        Tile.tiles[this.tiles[x + y * this.width]] = tile;
        if ( !tile.isGrass() ) {
            this.foodValues[x][y] = 0;
        }
    }

    public Tile getTile( int x, int y ) {
        if ( x < 0 || y < 0 || x >= this.width || y >= this.height ) return Tile.water;
        return Tile.tiles[this.tiles[x + y * this.width]];
    }

    public boolean setFoodValue( int x, int y, float foodValue ) {
        if ( x >= 0 && x < this.width && y >= 0 && y < this.height ) {
            if ( Tile.tiles[this.tiles[x + y * this.width]].isGrass() ) {
                this.foodValues[x][y] = foodValue;
                return true;
            }
        }
        return false;
    }

}
