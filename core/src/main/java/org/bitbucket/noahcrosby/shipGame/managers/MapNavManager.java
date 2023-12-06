package org.bitbucket.noahcrosby.shipGame.managers;

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
    protected MapNode hightLightNode;


    public MapNavManager(){
        mapList = new Array<>();
        mapDrawer = new MapDrawer();
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

    public void setCurrentNode(MapNode node){
        currentNode = node;
    }


    public void setDrawing(boolean drawMap) {
            drawingCurrentMap = drawMap;
    }

    public void drawMap(Matrix4 transform) {
        if(mapList.size < 1 || currentMap.getMapNodes().size < 1)return;

        mapDrawer.drawMap(transform);
    }

    public MapNode highLightNewNode(MapNode newNode) {
        if(newNode == null){
            Gdx.app.error("Null Error", "newNode is null MapNavManager.highlightNewNode()");
            return null;
        }
        if(this.hightLightNode != null)this.hightLightNode.clicked(false); // Check the highlit node is not null

        this.hightLightNode = newNode;
        this.hightLightNode.clicked(true);
        return newNode;
    }

    /**
     * Unhighlights the nodes, removes selections, etc. when clicked off of everything
     */
    public void clearSelections() {
        // Removes selected node and de-clicks it.
        if(this.hightLightNode != null){
            this.hightLightNode.clicked(false);
            this.hightLightNode = null;
        }
    }
}
