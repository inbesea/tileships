package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.AppPreferences;
import org.bitbucket.noahcrosby.Shapes.Line;

import java.util.Stack;

import static java.lang.Thread.sleep;

/**
 * Takes a map and draws it. Assumes the map is fine as is.
 *
 * We will keep in mind the map may be changing a lot between draws.
 */
public class MapDrawer {

    private SpaceMap map;

    public MapDrawer(SpaceMap map) {
        this.map = map;
    }
    public MapDrawer() {
    }

    /**
     * Draws the map and edges between them.
     * Tracks what it has drawn, and does not duplicate the work.
     *
     * @param transform - Projection Matrix from Orthographic Camerad
     */
    public void drawMap(Matrix4 transform) {
        Array<MapNode> unvisitedMapNodes = new Array<>(); // Move to next nodes bit by bit
        unvisitedMapNodes.addAll(map.mapNodes);
        Array<MapNode> visited = new Array<>();
        Stack<MapNode> nextNodes = new Stack<>();

        nextNodes.push(unvisitedMapNodes.pop());

        // Draw the nodes and edges
        while (unvisitedMapNodes.size + nextNodes.size() > 0) {
            separateGroupCheck(nextNodes, unvisitedMapNodes);

            MapNode currentNode = nextNodes.pop();
            for(MapNode mapNode : currentNode.edges){
                if(!mapNode.visited || !currentNode.visited){
                    currentNode.visited = true;
                    Line.DrawDebugLine(currentNode.getDrawPosition(), mapNode.getDrawPosition(), 1, Color.WHITE, transform);
                }
//                if(mapNode.drawn)continue; // Already drawn
                if(!nextNodes.contains(mapNode) && !visited.contains(mapNode, true))nextNodes.push(mapNode);
            }
            currentNode.draw(transform);
            if(AppPreferences.getAppPreferences().getIsDebug()) {
                currentNode.drawDebug(transform);
            }

            visited.add(currentNode);
            unvisitedMapNodes.removeValue(currentNode, true);
        }
        for(int i = 0; i < map.mapNodes.size; i++){
            map.mapNodes.get(i).visited = false;
        }
    }

    /**
     * Draws the map and edges between them.
     * Tracks what it has drawn, and does not duplicate the work.
     *
     * @param transform - Projection Matrix from Orthographic Camerad
     * @param millisecondDelay - Delays immediate drawing of the entire graph
     */
    public void drawMap(Matrix4 transform, int millisecondDelay) {
        if(millisecondDelay < 1){
            Gdx.app.log("MapDrawer", "DrawMap passed delay of 0, setting to 1 to avoid division by 0");
            millisecondDelay = 1;
        }
        // We want to draw another node each delay. For each millidelay we draw another. (time/millidelay)%map.mapNodes.size
        long nodesToDraw = (System.currentTimeMillis() / millisecondDelay) % map.mapNodes.size;

        System.out.println("nodesToDraw : " + nodesToDraw + " map.mapNodes.size " + map.mapNodes.size);
        Array<MapNode> unvisitedMapNodes = new Array<>(); // Move to next nodes bit by bit
        unvisitedMapNodes.addAll(map.mapNodes);
        Array<MapNode> visited = new Array<>();
        Stack<MapNode> nextNodes = new Stack<>();

        nextNodes.push(unvisitedMapNodes.pop());

        // Draw the nodes and edges
        while (unvisitedMapNodes.size + nextNodes.size() > 0 && nodesToDraw >= visited.size) {
            separateGroupCheck(nextNodes, unvisitedMapNodes);

            MapNode currentNode = nextNodes.pop();
            for(MapNode mapNode : currentNode.edges){
                if(!mapNode.visited || !currentNode.visited){
                    currentNode.visited = true;
                    Line.DrawDebugLine(currentNode.getDrawPosition(), mapNode.getDrawPosition(), 2, Color.WHITE, transform);
                }
                if(!nextNodes.contains(mapNode) && !visited.contains(mapNode, true))nextNodes.push(mapNode);
            }
            currentNode.draw(transform);

            visited.add(currentNode);
            unvisitedMapNodes.removeValue(currentNode, true);
        }
        for(int i = 0; i < map.mapNodes.size; i++){
            map.mapNodes.get(i).visited = false;
        }
    }

    /**
     * Allows a passed map to be drawn rather than a in internally set map,
     * returns to old map when done drawing if one was present.
     * @param map
     * @param transform
     * @param millisecondDelay
     */
    public void drawMap(SpaceMap map, Matrix4 transform, int millisecondDelay) {
        SpaceMap previousMap = null;
        if(this.map != null)previousMap = this.map;

        setMap(map);
        drawMap(transform, millisecondDelay);

        if(previousMap != null)setMap(previousMap);
    }

    /**
     * Checks the
     * @param nextNodes
     * @param unvisited
     */
    void separateGroupCheck(Stack<MapNode> nextNodes, Array<MapNode> unvisited){
        if(nextNodes.size() == 0){
            nextNodes.push(unvisited.pop());
        }
    }

    /**
     * Resets the map's nodes as not drawn after completing a draw
     */
    private void resetNodesDrawn() {
        for (MapNode node : map.getMapNodes()) {
            node.drawn = false;
        }
    }

    /**
     * Swaps out the currently drawn map for a new one
     * @param newMap
     */
    public void setMap(SpaceMap newMap) {
        this.map = newMap;
    }
}
