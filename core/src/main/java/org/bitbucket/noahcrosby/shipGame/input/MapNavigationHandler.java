package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.levelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.levelData.SpaceMap;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

/**
 * Handles player input for clicking on locations of the map and moving between nodes.
 */
public class MapNavigationHandler extends InputAdapter {
    private static final float NODE_SELECT_DISTANCE = 18;
    protected MapNavManager navManager;
    protected SpaceMap currentMap;
    protected OrthographicCamera camera;

    MapNode hoveredNode = null;

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
        Vector2 touchPosition = generalUtil.returnUnprojectedInputVector2(camera);
        MapNode closestNode = generalUtil.getClosestObject(touchPosition, currentMap.getMapNodes());

        boolean isSelectable = touchPosition.dst(closestNode.getPosition()) < NODE_SELECT_DISTANCE;
        if(isSelectable){
            boolean alreadySelected = closestNode.equals(this.navManager.getSelectedNode());
            if(alreadySelected && !closestNode.playerIsHere()){
                moveToNewNode(closestNode);
            } else {
                this.navManager.selectNode(closestNode);
            }
        } else {
            clearSelections();
        }

        return false;
    }

    /**
     * Moves the player to a new location if possible.
     * @param closestNode
     */
    private void moveToNewNode(MapNode closestNode) {
        if(!this.navManager.canMoveToNode(closestNode)){
            Gdx.app.log("MapNavigationHandler", "Cannot move to new node " + closestNode.getPositionAsString());
        }else{
            // Remove references to a selected node
            Gdx.app.log("MapNavigationHandler", "Moving to new node " + closestNode.getPositionAsString());
            this.navManager.clearSelections();
            this.navManager.setCurrentNode(closestNode);
        }
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
        // issue with one node being hovered could be unhovered due to the one at a time hovering.
        // Can we track hovered nodes?
        Vector2 touch = generalUtil.returnUnprojectedInputVector2(camera);
        MapNode closestNodeToTouch = generalUtil.getClosestObject(touch, currentMap.getMapNodes());
        if(closestNodeToTouch != hoveredNode && hoveredNode != null){
            hoveredNode.setHovered(false);
            hoveredNode = null;
        }

        boolean closeEnoughToHover = closestNodeToTouch.getPosition().dst(touch) < NODE_SELECT_DISTANCE;
        if(closeEnoughToHover){
            closestNodeToTouch.setHovered(true);
            hoveredNode = closestNodeToTouch;
        } else {
            closestNodeToTouch.setHovered(false);
            hoveredNode = null;
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
