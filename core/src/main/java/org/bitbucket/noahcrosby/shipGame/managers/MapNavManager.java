package org.bitbucket.noahcrosby.shipGame.managers;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapDrawer;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;


/**
 * Handles the navigation of the maps, and the drawing of the maps
 */
public class MapNavManager {
    Array<SpaceMap> mapList;
    public SpaceMap currentMap;
    MapNode currentNode;
    boolean drawingCurrentMap;
    protected MapDrawer mapDrawer;
    protected MapNode selectedNode;
    protected Signal<MapNode> publisher;


    public MapNavManager(){
        mapList = new Array<>();
        mapDrawer = new MapDrawer();
        publisher = new Signal<>();
    }

    public void addMap(SpaceMap map){
        mapList.add(map);
        if(currentMap == null){
            setCurrentMap(map);
            setCurrentNode(map.getEntryNode());
        }
    }

    public void removeMap(SpaceMap map){
        mapList.removeValue(map, true);
    }

    public SpaceMap getCurrentMap(){
        return currentMap;
    }

    public void setCurrentMap(SpaceMap map){
        currentMap = map;
        mapDrawer.setMap(currentMap);
    }

    /**
     * Sets the given node to where the player is, and replaces the old one.
     * @param node
     */
    public void setCurrentNode(MapNode node){
        if(currentNode != null){
            currentNode.setPlayerIsHere(false);
        }
        node.setPlayerIsHere(true);
        currentNode = node;
        publishNewNode();
    }

    /**
     * Notifies listeners that a new node has been arrived.
     */
    private void publishNewNode(){
        publisher.dispatch(currentNode);
    }

    /**
     * Convenience method to get the current node.
     * @return
     */
    public Signal<MapNode> getPublisher() {
        return publisher;
    }

    public void setDrawing(boolean drawMap) {
            drawingCurrentMap = drawMap;
    }

    public void drawMap(Matrix4 transform) {
        if(mapList.size < 1 || currentMap.getMapNodes().size < 1)return;

        mapDrawer.drawMap(transform);
    }

    /**
     * Informs the node it has been selected and keeps the number of selected nodes to one
     * @param newNode - node that is selected
     */
    public MapNode selectNode(MapNode newNode) {
        if(newNode == null){
            Gdx.app.error("Null Error", "newNode is null MapNavManager.highlightNewNode()");
            return null;
        }
        if(!this.currentMap.getMapNodes().contains(newNode, true)){
            Gdx.app.error("Missing Node", "Cannot find selected node in current SpaceMap");
            return null;
        }
        if(this.selectedNode != null)this.selectedNode.clicked(false); // Check the highlit node is not null

        newNode.clicked(true);
        this.selectedNode = newNode;
        return newNode;
    }

    /**
     * Unhighlights the nodes, removes selections, etc. when clicked off of everything
     */
    public void clearSelections() {
        // Removes selected node and de-clicks it.
        if(this.selectedNode != null){
            this.selectedNode.clicked(false);
            this.selectedNode = null;
        }
    }

    /**
     * Determines if a node can be moved to
     * @param closestNode
     * @return
     */
    public boolean canMoveToNode(MapNode closestNode) {
        Gdx.app.debug("Unimplemented Method", "MapNavManager.canMoveToNode()");
        return true;
    }

    public MapNode getSelectedNode() {
        return this.selectedNode;
    }
}
