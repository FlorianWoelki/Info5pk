package com.florianwoelki.info5pk.map;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Tile {

    public float food;
    public TileType type;
    public int x, y;

    public Tile(int x, int y, TileType type, float food) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.food = food;
    }

    public boolean isLand() {
        return type == TileType.LAND;
    }

    public boolean isWater() {
        return type == TileType.WATER;
    }

}
