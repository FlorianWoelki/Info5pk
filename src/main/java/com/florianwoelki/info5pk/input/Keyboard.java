package com.florianwoelki.info5pk.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Keyboard implements KeyListener {

    private static boolean[] keys;
    private static boolean[] pkeys;

    private static final int NUM_KEYS = 5;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int F3 = 4;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        for(int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static void setKey(int k, boolean b) {
        keys[k] = b;
    }

    public static boolean isDown(int k) {
        return keys[k];
    }

    public static boolean isPressed(int k) {
        return keys[k] && !pkeys[k];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            setKey(UP, true);
        }
        if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            setKey(LEFT, true);
        }
        if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            setKey(DOWN, true);
        }
        if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            setKey(RIGHT, true);
        }
        if(keyCode == KeyEvent.VK_F3) {
            setKey(F3, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            setKey(UP, false);
        }
        if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            setKey(LEFT, false);
        }
        if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            setKey(DOWN, false);
        }
        if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            setKey(RIGHT, false);
        }
        if(keyCode == KeyEvent.VK_F3) {
            setKey(F3, false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
