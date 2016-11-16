package com.florianwoelki.info5pk;

import java.awt.*;

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

    @Override
    public void run() {
        while ( isRunning ) {
            
        }

        stop();
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

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

}
