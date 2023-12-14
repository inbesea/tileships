package org.bitbucket.noahcrosby.shipGame.generalObjects.HUD;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

/**
 * Use this class to add and manage UI elements
 *
 * TODO : Add possible animation support for texture swapping. (maybe?)
 */
public class UIElement extends GameObject {

    protected Texture texture;

    public UIElement(Vector2 position, Vector2 size, Texture texture) {
        super(position, size);

        setTexture(texture);
    }

    @Override
    public void render(TileShipGame game) {
        game.batch.draw(getTexture(), getPosition().x, getPosition().y, getSize().x, getSize().y);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition) {

    }

    @Override
    public void collision(GameObject gameObject) {
        // No collision for UI
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }
}
