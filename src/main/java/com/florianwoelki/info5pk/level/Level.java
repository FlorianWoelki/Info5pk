package com.florianwoelki.info5pk.level;

import com.florianwoelki.info5pk.creature.CreatureFactory;
import com.florianwoelki.info5pk.level.generator.LevelGenerator;
import com.florianwoelki.info5pk.level.tile.Tile;

import java.awt.*;

/**
 * Created by Florian Woelki on 19.11.16.
 */
public class Level {

    public final float TIME_PER_TICK = 0.01f;
    public final float MAXIMUM_FOOD_PER_TILE = 100;

    public int width;
    public int height;

    public byte[] tiles;
    public float[][] foodValues;

    public CreatureFactory creatureFactory;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        creatureFactory = new CreatureFactory(this);

        LevelGenerator levelGenerator = new LevelGenerator(width, height);
        levelGenerator.startFrequencyX = 10;
        levelGenerator.startFrequencyY = 10;
        levelGenerator.calculate();
        float[][] map = levelGenerator.map;
        tiles = new byte[width * height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int i = x + y * width;

                tiles[i] = (byte) (map[x][y] > 0.5 ? 0 : 1);
            }
        }

        foodValues = new float[tiles.length][tiles.length];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                foodValues[x][y] = 0f;
            }
        }
    }

    public void render(Graphics g, int xOffset, int yOffset, float mouseWheelScale) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Tile tile = getTile(x, y);
                tile.mouseWheelScale = mouseWheelScale;

                if(tile.isGrass()) {
                    float alpha = foodValues[x][y] / 100.0f;
                    if(alpha < 0f) {
                        alpha = 0;
                    } else if(alpha > 1f) {
                        alpha = 1f;
                    }

                    Color color = new Color(0f, 1f, 0f, alpha);

                    g.setColor(color);
                }

                tile.render(g, this, x + xOffset, y + yOffset);
            }
        }

        creatureFactory.render(g, xOffset, yOffset, mouseWheelScale);
    }

    public void update() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                getTile(x, y).update();
            }
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(isFertile(x, y)) {
                    grow(x, y);
                }
            }
        }

        creatureFactory.update();
    }

    private void grow(int x, int y) {
        foodValues[x][y] += 0.2f;
        if(foodValues[x][y] > MAXIMUM_FOOD_PER_TILE) {
            foodValues[x][y] = MAXIMUM_FOOD_PER_TILE;
        }
    }

    private boolean isFertileToNeighbors(int x, int y) {
        if(x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }
        if(getTile(x, y).isWater()) {
            return true;
        }
        return getTile(x, y).isGrass() && foodValues[x][y] > 50;
    }

    private boolean isFertile(int x, int y) {
        if(getTile(x, y).isGrass()) {
            if(foodValues[x][y] > 50) {
                return true;
            }
            if(isFertileToNeighbors(x - 1, y)) {
                return true;
            }
            if(isFertileToNeighbors(x + 1, y)) {
                return true;
            }
            if(isFertileToNeighbors(x, y - 1)) {
                return true;
            }
            if(isFertileToNeighbors(x, y + 1)) {
                return true;
            }
        }
        return false;
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= width || y >= height) return Tile.water;
        return Tile.tiles[tiles[x + y * width]];
    }

}
