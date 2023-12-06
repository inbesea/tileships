package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.Shapes.Line;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

/**
 * Handles clicking on locations of the map and moving between nodes.
 */
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
        currentMap.getMapNodes();

        Vector2 touch = new Vector2(screenX, screenY);
        MapNode temp = generalUtil.getClosestObject(touch, currentMap.getMapNodes());
        Line.drawHollowCircle(temp.getPosition(), 10, Color.WHITE, TileShipGame.batch.getProjectionMatrix());

        Gdx.app.log("Tag",  touch.x + " " + touch.y + " Found this MapNode " + temp.getPositionAsString());
        if(touch.dst(temp.getPosition()) < 20){
//            temp.drawDebug();
        }
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
