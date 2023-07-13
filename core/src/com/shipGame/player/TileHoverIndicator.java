package com.shipGame.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.ID;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.TileShipGame;

/**
 * Method to hold reference to the hover indicator so it logically sits in the stack manager during the collapse process.
 */
public class TileHoverIndicator extends GameObject {
    boolean drawHover = false;
    public TileHoverIndicator(Vector2 position, Vector2 size) {
        super(position, size);

        // Hover Indicator will always use the same texture
        this.id = ID.Hover;
        this.textureRef = id.getSprite();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(TileShipGame game) {

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

    /**
     * Belongs to the tileManager, cannot be directly removed from the game.
     * @return
     */
    @Override
    public boolean deleteFromGame() {
        return false;
    }

    public boolean isHoverDrawing() {
        return drawHover;
    }

    public void setDrawHover(boolean drawHover) {
        this.drawHover = drawHover;
    }
}
