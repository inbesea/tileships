package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.InputAdapter;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;

public class MapNavigationHandler extends InputAdapter {
    protected MapNavManager navManager;
    protected SpaceMap currentMap;

    public MapNavigationHandler(MapNavManager navManager){
        this.navManager = navManager;
        if(this.navManager.currentMap != null){
            this.currentMap = this.navManager.currentMap;
        }
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
