package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

/**
 * Handles clicking on locations of the map and moving between nodes.
 */
public class MapNavigationHandler extends InputAdapter {
    private static final float NODE_SELECT_DISTANCE = 18;
    protected MapNavManager navManager;
    protected SpaceMap currentMap;
    protected OrthographicCamera camera;

    public MapNavigationHandler(MapNavManager navManager, OrthographicCamera camera){
        this.navManager = navManager;
        this.camera = camera;
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
        // Get touch position and closest node to that position
        Vector2 touch = generalUtil.returnUnprojectedInputVector2(camera);
        MapNode closestNode = generalUtil.getClosestObject(touch, currentMap.getMapNodes());

        // Selecting newly clicked node
        boolean closestIsSelectable = touch.dst(closestNode.getPosition()) < NODE_SELECT_DISTANCE;
        if(closestIsSelectable){
            boolean alreadySelected = closestNode.equals(currentMap.getSelectedNode());
            if(alreadySelected){
                // We want to move there if possible
                moveToNewNode(closestNode);
            } else {
                this.navManager.selectNode(closestNode);
            }
        } else {
            clearSelections();
        }

        Gdx.app.log("Tag",  touch.x + " " + touch.y + " Found this MapNode " + closestNode.getPositionAsString());
        if(touch.dst(closestNode.getPosition()) < 20){
//            temp.drawDebug();
        }
        return false;
    }

    /**
     * Moves the player to a new location if possible.
     * @param closestNode
     */
    private void moveToNewNode(MapNode closestNode) {
        if(!this.navManager.canMoveToNode(closestNode)){
            return;
        }

        // Remove references to a selected node
        this.navManager.clearSelections();
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

        Vector2 touch = generalUtil.returnUnprojectedInputVector2(camera);
        MapNode temp = generalUtil.getClosestObject(touch, currentMap.getMapNodes());

        if(temp.getPosition().dst(touch) < NODE_SELECT_DISTANCE){
            temp.setHovered(true);
        } else {
            temp.setHovered(false);
        }

        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void clearSelections() {
        this.navManager.clearSelections();
    }
}
