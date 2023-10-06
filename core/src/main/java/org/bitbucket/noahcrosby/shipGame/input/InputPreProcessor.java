package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputPreProcessor extends InputMultiplexer {

    private final OrthographicCamera camera;
    private final Vector3 unprojected;

    public InputPreProcessor(OrthographicCamera camera) {
        this.camera = camera;
        unprojected = new Vector3();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        camera.unproject(unprojected.set(screenX, screenY, 0f));
        return super.mouseMoved((int) unprojected.x, (int) unprojected.y);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return super.scrolled(amountX, amountY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer != 0) return false;
        camera.unproject(unprojected.set(screenX, screenY, 0f));
        return super.touchDown((int) unprojected.x, (int) unprojected.y, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer != 0) return false;
        camera.unproject(unprojected.set(screenX, screenY, 0f));
        return super.touchDragged((int) unprojected.x, (int) unprojected.y, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer != 0) return false;
        camera.unproject(unprojected.set(screenX, screenY, 0f));
        return super.touchUp((int) unprojected.x, (int) unprojected.y, pointer, button);
    }
}