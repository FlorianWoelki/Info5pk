package com.florianwoelki.info5pk;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian Woelki on 16.11.16.
 */
public class Window extends JFrame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Info 5. pk";

    public Window(Game game) {
        super(Window.TITLE);

        Dimension size = new Dimension(Window.WIDTH, Window.HEIGHT);
        game.setPreferredSize(size);
        game.setMaximumSize(size);
        game.setMinimumSize(size);

        add(game);
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
