package com.florianwoelki.info5pk;

import com.florianwoelki.info5pk.input.Keyboard;
import com.florianwoelki.info5pk.input.Mouse;
import com.florianwoelki.info5pk.level.Level;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Game extends Canvas implements Runnable {

    private Window window;
    private Keyboard keyboard;
    private Mouse mouse;

    private Thread thread;
    private boolean isRunning;

    private Level level;
    private Runnable levelRunnable;

    public Game() {
        this.keyboard = new Keyboard();
        this.mouse = new Mouse();

        this.requestFocus();
        this.setFocusable( true );
        this.addKeyListener( this.keyboard );
        this.addMouseListener( this.mouse );
        this.addMouseMotionListener( this.mouse );
        this.addMouseWheelListener( this.mouse );

        this.level = new Level( 128, 128 );

        this.window = new Window( this );
        this.window.setVisible( true );
    }

    private synchronized void start() {
        if ( this.isRunning ) return;

        this.isRunning = true;
        this.thread = new Thread( this, "Game Window" );
        this.thread.start();
    }

    private synchronized void stop() {
        if ( !this.isRunning ) return;

        this.isRunning = false;
        try {
            this.thread.join();
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
            boolean shouldRender = false;
            long now = System.nanoTime();
            delta += ( now - lastTime ) / ns;
            lastTime = now;
            if ( delta >= 1 ) {
                delta--;
                this.update();
                ups++;
                shouldRender = true;
            }

            try {
                Thread.sleep( 3 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }

            if ( shouldRender ) {
                this.render();
                fps++;
            }

            if ( System.currentTimeMillis() - lastTimer > 1000 ) {
                lastTimer += 1000;
                System.out.println( "FPS: " + fps + ", UPS: " + ups );
                ups = fps = 0;
            }
        }

        this.stop();
    }

    private void update() {
        this.level.update();
        this.keyboard.update();

        if ( this.keyboard.right ) {
            this.x--;
        } else if ( this.keyboard.left ) {
            this.x++;
        } else if ( this.keyboard.up ) {
            this.y++;
        } else if ( this.keyboard.down ) {
            this.y--;
        }
    }

    int x = 0;
    int y = 0;

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if ( bs == null ) {
            this.createBufferStrategy( 3 );
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor( Color.BLACK );
        g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
        this.level.render( g, x, y, this.mouse.getMouseWheelScale() );
        g.dispose();
        bs.show();
    }

    public static void main( String[] args ) {
        Game game = new Game();
        game.start();
    }

}
