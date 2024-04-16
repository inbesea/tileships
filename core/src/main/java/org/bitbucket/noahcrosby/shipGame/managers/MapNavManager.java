package org.bitbucket.noahcrosby.shipGame.managers;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.levelData.MapDrawer;
import org.bitbucket.noahcrosby.shipGame.levelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.levelData.SpaceMap;
import org.bitbucket.noahcrosby.utils.DijkstrasAlgo;

import java.util.HashMap;
import java.util.Map;


/**
 * Handles the navigation of the maps, and the drawing of the maps
 */
public class MapNavManager {
    Array<SpaceMap> mapList;
    public SpaceMap currentMap;
    MapNode currentNode;
    MapNode previousNode;
    boolean drawingCurrentMap;
    protected MapDrawer mapDrawer;
    protected MapNode selectedNode;
    protected Signal<MapNode> publisher;
    Ship ship;


    public MapNavManager(Ship ship){
        mapList = new Array<>();
        mapDrawer = new MapDrawer();
        publisher = new Signal<>();
        this.ship = ship;
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
            previousNode = currentNode;
        }
        node.setPlayerIsHere(true);
        currentNode = node;

        checkForRefuel();

        publishNewNode();
    }

    /**
     * Checks if the player is at the entry node and refuels if true
     */
    private void checkForRefuel() {
        if(currentNode.equals(currentMap.getEntryNode())){
            ship.fuelTank.fill();
        }
    }

    /**
     * Notifies listeners that a new node has been arrived.
     * Using the observer/watcher design pattern
     */
    private void publishNewNode(){
        // Notify the game screen that a new node has arrived
        publisher.dispatch(currentNode); // This is passed to the GameScreen. Only relevant if you're using the GameScreen receive()
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
     * Selects the given node and updates the selected node to be the new one
     * @param node - the node to be selected
     * @return the selected node
     */
    public MapNode selectNode(MapNode node) {
        if(node == null){
            return null;
        }
        Boolean notInMap = !this.currentMap.getMapNodes().contains(node, true);
        if(notInMap){
            return null;
        }
        if(this.selectedNode != null) {
            this.selectedNode.clicked(false);
        }

        node.clicked(true);
        this.selectedNode = node;
        return node;
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
        Boolean canMove = currentNode.isNeighborsWith(closestNode) &&
            moveCost(currentNode, closestNode) <= ship.fuelTank.getFuel();

        if(canMove){
            ship.fuelTank.consumeFuel(moveCost(currentNode, closestNode));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns shortest distance between two nodes
     * @param currentNode
     * @param closestNode
     * @return
     */
    private Integer moveCost(MapNode currentNode, MapNode closestNode) {
        Map<MapNode, Integer> shortestPath = DijkstrasAlgo.shortestPath(currentNode, closestNode, currentMap.getMapEdges());
        return shortestPath.size() - 1; // Subtract one, DijkstrasAlgo starts at 1
    }

    public MapNode getSelectedNode() {
        return this.selectedNode;
    }


    public MapNode getCurrentNode() {
        return currentNode;
    }

    public MapNode getPreviousNode() {
        return previousNode;
    }


}
