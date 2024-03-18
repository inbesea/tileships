package org.bitbucket.noahcrosby.shipGame.levelData;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

/**
 * Simple class to draw in the background.
 */
public class ForegroundObject extends GameObject {
    Texture texture;

    public ForegroundObject(Vector2 postion, Vector2 size, Texture texture){
        super(postion, size, ID.ForegroundObject);
        this.texture = texture;
    }

    @Override
    public void render(TileShipGame game) {
        game.batch.draw(texture, position.x, position.y, (size.x / 2), (size.y / 2),  size.x, size.y,
            1f, 1f, rotation ,0 ,0 ,texture.getWidth(), texture.getHeight(), false, false);
        rotation += 1;
        /* x – the x-coordinate in screen space
        y – the y-coordinate in screen space
        originX – the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
        originY – the y-coordinate of the scaling and rotation origin relative to the screen space coordinates
        width – the width in pixels
        height – the height in pixels
        scaleX – the scale of the rectangle around originX/originY in x
        scaleY – the scale of the rectangle around originX/originY in y
        rotation – the angle of counterclockwise rotation of the rectangle around originX/originY
        srcX – the x-coordinate in texel space
        srcY – the y-coordinate in texel space
        srcWidth – the source with in texels
        srcHeight – the source height in texels
        flipX – whether to flip the sprite horizontally
        flipY – whether to flip the sprite vertically */
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

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    public void update() {

    }
}
