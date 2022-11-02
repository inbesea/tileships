package com.ncrosby.game;

import java.awt.*;

import static com.ncrosby.game.util.generalUtil.clamp;

public class HUD {


    public HUD() {
    }

    public static int HEALTH = 100;

    public void tick() {
        HEALTH = clamp(HEALTH, 0, 100);



    }

    /**
     * Draws the HUD on the screen relative to the camera.
     * @param g
     */
    public void render (Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(15, 15, 200, 32);
        g.setColor(Color.green);
        g.fillRect(17, 17, HEALTH * 2 - 4, 28);
        g.setColor(Color.white);
        g.drawRect(15, 15, 200, 32);
    }
}
