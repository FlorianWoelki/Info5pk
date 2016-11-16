package com.florianwoelki.info5pk;

import com.florianwoelki.info5pk.creature.CreatureFactory;
import com.florianwoelki.info5pk.creature.TestCreature;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Game extends Canvas implements Runnable {

    private Window window;

    private Thread thread;
    private boolean isRunning;

    private CreatureFactory creatureFactory;

    public Game() {
        window = new Window( this );
        window.setVisible( true );

        creatureFactory = new CreatureFactory();
        for ( int i = 0; i < 5; i++ ) {
            creatureFactory.addCreature( new TestCreature( (float) (Math.random() * getWidth() - 8), (float) (Math.random() * getHeight() - 8) ) );
        }
    }

    private synchronized void start() {
        if ( isRunning ) return;

        isRunning = true;
        thread = new Thread( this, "Game Window" );
        thread.start();
    }

    private synchronized void stop() {
        if ( !isRunning ) return;

        isRunning = false;
        try {
            thread.join();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        double ns = 1000000000d / 60d;
        long lastTimer = System.currentTimeMillis();

        int fps = 0, ups = 0;

        while ( isRunning ) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while ( delta >= 1 ) {
                delta--;
                update();
                ups++;
            }

            render();
            fps++;

            if ( System.currentTimeMillis() - lastTimer > 1000 ) {
                lastTimer += 1000;
                System.out.println( "FPS: " + fps + ", UPS: " + ups );
                ups = fps = 0;
            }
        }

        stop();
    }

    private void update() {
        creatureFactory.update();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if ( bs == null ) {
            createBufferStrategy( 3 );
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor( Color.BLACK );
        g.fillRect( 0, 0, getWidth(), getHeight() );
        creatureFactory.render( g );
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

}
