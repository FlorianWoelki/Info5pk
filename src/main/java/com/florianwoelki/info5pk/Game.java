package com.florianwoelki.info5pk;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Game extends Canvas implements Runnable {

    private Window window;

    private Thread thread;
    private boolean isRunning;

    public Game() {
        window = new Window( this );
        window.setVisible( true );
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
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

}
