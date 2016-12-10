package com.florianwoelki.info5pk.level.generator;

import com.florianwoelki.info5pk.level.tile.Tile;
import com.florianwoelki.info5pk.math.MathUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Florian Woelki on 19.11.16.
 */
public class LevelGenerator {

    public double[] values;
    private int w, h;

    public LevelGenerator( int w, int h, int featureSize ) {
        this.w = w;
        this.h = h;

        this.values = new double[w * h];

        for ( int y = 0; y < w; y += featureSize ) {
            for ( int x = 0; x < w; x += featureSize ) {
                this.setSample( x, y, MathUtil.random.nextFloat() * 2 - 1 );
            }
        }

        int stepSize = featureSize;
        double scale = 1.0 / w;
        double scaleMod = 1;
        do {
            int halfStep = stepSize / 2;
            for ( int y = 0; y < w; y += stepSize ) {
                for ( int x = 0; x < w; x += stepSize ) {
                    double a = this.sample( x, y );
                    double b = this.sample( x + stepSize, y );
                    double c = this.sample( x, y + stepSize );
                    double d = this.sample( x + stepSize, y + stepSize );

                    double e = ( a + b + c + d ) / 4.0 + ( MathUtil.random.nextFloat() * 2 - 1 ) * stepSize * scale;
                    this.setSample( x + halfStep, y + halfStep, e );
                }
            }
            for ( int y = 0; y < w; y += stepSize ) {
                for ( int x = 0; x < w; x += stepSize ) {
                    double a = this.sample( x, y );
                    double b = this.sample( x + stepSize, y );
                    double c = this.sample( x, y + stepSize );
                    double d = this.sample( x + halfStep, y + halfStep );
                    double e = this.sample( x + halfStep, y - halfStep );
                    double f = this.sample( x - halfStep, y + halfStep );

                    double H = ( a + b + d + e ) / 4.0 + ( MathUtil.random.nextFloat() * 2 - 1 ) * stepSize * scale * 0.5;
                    double g = ( a + c + d + f ) / 4.0 + ( MathUtil.random.nextFloat() * 2 - 1 ) * stepSize * scale * 0.5;
                    this.setSample( x + halfStep, y, H );
                    this.setSample( x, y + halfStep, g );
                }
            }
            stepSize /= 2;
            scale *= ( scaleMod + 0.8 );
            scaleMod *= 0.3;
        } while ( stepSize > 1 );
    }

    private double sample( int x, int y ) {
        return this.values[( x & ( this.w - 1 ) ) + ( y & ( this.h - 1 ) ) * this.w];
    }

    private void setSample( int x, int y, double value ) {
        values[( x & ( this.w - 1 ) ) + ( y & ( this.h - 1 ) ) * this.w] = value;
    }

    public static byte[][] createAndValidateTopMap( int w, int h ) {
        do {
            byte[][] result = LevelGenerator.createTopMap( w, h );

            int[] count = new int[256];

            for ( int i = 0; i < w * h; i++ ) {
                count[result[0][i] & 0xff]++;
            }
            if ( count[Tile.grass.id & 0xff] < 100 ) continue;

            return result;

        } while ( true );
    }

    private static byte[][] createTopMap( int w, int h ) {
        LevelGenerator noise1 = new LevelGenerator( w, h, 32 );
        LevelGenerator noise2 = new LevelGenerator( w, h, 32 );

        byte[] map = new byte[w * h];
        byte[] data = new byte[w * h];
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                int i = x + y * w;

                double val = Math.abs( noise1.values[i] - noise2.values[i] ) * 3 - 2;

                double xd = x / ( w - 1.0 ) * 2 - 1;
                double yd = y / ( h - 1.0 ) * 2 - 1;
                if ( xd < 0 ) xd = -xd;
                if ( yd < 0 ) yd = -yd;
                double dist = xd >= yd ? xd : yd;
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1 - dist * 20;

                if ( val < -0.5 ) {
                    map[i] = Tile.water.id;
                } else {
                    map[i] = Tile.grass.id;
                }
            }
        }

        return new byte[][]{ map, data };
    }

    public static void main( String[] args ) {
        while ( true ) {
            int w = 128;
            int h = 128;

            byte[] map = LevelGenerator.createAndValidateTopMap( w, h )[0];

            BufferedImage img = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
            int[] pixels = new int[w * h];
            for ( int y = 0; y < h; y++ ) {
                for ( int x = 0; x < w; x++ ) {
                    int i = x + y * w;

                    if ( map[i] == Tile.water.id ) pixels[i] = 0x000080;
                    if ( map[i] == Tile.grass.id ) pixels[i] = 0x208020;
                }
            }
            img.setRGB( 0, 0, w, h, pixels, 0, w );
            JOptionPane.showMessageDialog( null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon( img.getScaledInstance( w * 4, h * 4, Image.SCALE_AREA_AVERAGING ) ) );
        }
    }

}
