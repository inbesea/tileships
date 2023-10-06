package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;

public class ZoomHandler extends InputAdapter {

    private final OrthographicCamera camera;

    public ZoomHandler(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float newValue = camera.zoom + ((amountY * TileShipGame.zoomSpeed) * Gdx.graphics.getDeltaTime());

        if (newValue >= TileShipGame.zoomMax) newValue = TileShipGame.zoomMax;

        else newValue = Math.max(newValue, TileShipGame.zoomMin);

        camera.zoom = newValue;

        return false;
    }
}