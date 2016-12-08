package com.florianwoelki.info5pk.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[120];
    public boolean up, down, left, right;

    public void update() {
        this.up = this.keys[KeyEvent.VK_UP] || this.keys[KeyEvent.VK_W];
        this.down = this.keys[KeyEvent.VK_DOWN] || this.keys[KeyEvent.VK_S];
        this.left = this.keys[KeyEvent.VK_LEFT] || this.keys[KeyEvent.VK_A];
        this.right = this.keys[KeyEvent.VK_RIGHT] || this.keys[KeyEvent.VK_D];
    }

    @Override
    public void keyPressed( KeyEvent e ) {
        if ( e.getKeyCode() < this.keys.length ) {
            this.keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased( KeyEvent e ) {
        if ( e.getKeyCode() < this.keys.length ) {
            this.keys[e.getKeyCode()] = false;
        }
    }

    @Override
    public void keyTyped( KeyEvent e ) {
    }

}
