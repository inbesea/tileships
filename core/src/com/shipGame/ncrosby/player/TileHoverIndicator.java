package com.shipGame.ncrosby.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.tileShipGame;

public class TileHoverIndicator extends GameObject {

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
}
