package com.shipGame.ncrosby;

import com.shipGame.ncrosby.main.legacyGame;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas{

    /**
     * Creates the window for the game to be drawn in.
     */
    private static final long serialVersionUID = 3010486623466540351L;

    public Window(int width, int height, String title, legacyGame game) {
        JFrame frame = new JFrame(title);
        JPanel panel = new JPanel();
        //frame.getContentPane().addMouseListener(new ClickLocationListener());


        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);

        frame.setVisible(true);
        game.start();
    }

    public int[] getXY() {
        int valXY[] = {0,0};



        return valXY;
    }
}
