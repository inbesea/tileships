package com.shipGame.ncrosby.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.tileShipGame;

/**
 * Method to hold reference to the hover indicator so it logically sits in the stack manager during the collapse process.
 */
public class TileHoverIndicator extends GameObject {
    boolean drawHover = false;
    public TileHoverIndicator(Vector2 position, Vector2 size) {
        super(position, size);

        // Hover Indicator will always use the same texture
        this.id = ID.Hover;
        this.textureRef = id.getTexture();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(tileShipGame game) {

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    public boolean isHoverDrawing() {
        return drawHover;
    }

    public void setDrawHover(boolean drawHover) {
        this.drawHover = drawHover;
    }
}
