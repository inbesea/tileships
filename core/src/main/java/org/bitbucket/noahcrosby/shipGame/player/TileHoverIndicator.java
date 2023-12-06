package org.bitbucket.noahcrosby.shipGame.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

/**
 * Method to hold reference to the hover indicator so it logically sits in the stack manager during the collapse process.
 */
public class TileHoverIndicator extends GameObject {
    boolean drawHover = false;
    public TileHoverIndicator(Vector2 position, Vector2 size) {
        super(position, size);

        // Hover Indicator will always use the same texture
        this.id = ID.Hover;
    }

    @Override
    public void render(TileShipGame game) {
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition) {
        return;
    }

    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Texture getTexture() {
        return Resources.HoverIndicatorTexture;
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
