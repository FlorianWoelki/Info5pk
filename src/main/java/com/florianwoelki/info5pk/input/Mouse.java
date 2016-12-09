package com.florianwoelki.info5pk.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private float mouseWheelScale = 0.5f;

    @Override
    public void mouseClicked( MouseEvent e ) {
    }

    @Override
    public void mousePressed( MouseEvent e ) {
    }

    @Override
    public void mouseReleased( MouseEvent e ) {
    }

    @Override
    public void mouseEntered( MouseEvent e ) {
    }

    @Override
    public void mouseExited( MouseEvent e ) {
    }

    @Override
    public void mouseDragged( MouseEvent e ) {
    }

    @Override
    public void mouseMoved( MouseEvent e ) {
    }

    @Override
    public void mouseWheelMoved( MouseWheelEvent e ) {
        double delta = 0.005f * e.getPreciseWheelRotation();
        this.mouseWheelScale += delta;
    }

    public float getMouseWheelScale() {
        return this.mouseWheelScale;
    }

}
