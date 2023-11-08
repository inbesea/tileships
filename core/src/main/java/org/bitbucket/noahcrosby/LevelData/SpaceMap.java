package org.bitbucket.noahcrosby.LevelData;

import com.badlogic.gdx.utils.Array;

/**
 * Holds the map graph, draws the map graph
 */
public class SpaceMap {
    Array<MapNode> mapNodes;

    public SpaceMap() {
        mapNodes = new Array<>();
    }

    public void addNode(MapNode node) {
        // Need to add logic to connect nodes.
    }

    public void addNode(MapNode node, int[] otherNodes) {
        mapNodes.add(node);
        for(int i = 0 ; i < otherNodes.length; i++){
            try {
                // Add each other node to new node if found
                MapNode temp = mapNodes.get(otherNodes[i]);
                node.edges.add(temp);
                temp.edges.add(node);
            }catch (IndexOutOfBoundsException e){
                System.out.println("Index out of bounds : " + otherNodes[i]);
            }
        }
    }

    public Array<MapNode> getMapNodes() {
        return mapNodes;
    }

    public void setMapNodes(Array<MapNode> mapNodes) {
        this.mapNodes = mapNodes;
    }
}
