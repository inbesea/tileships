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

    /**
     * Adds an orphan node to the map with no edges.
     * @param node
     */
    public void addNode(MapNode node) {
        mapNodes.add(node);
    }

    /**
     * Uses explicit array indexes to connect nodes.
     *
     * @param node
     * @param otherNodes - Array indexes of nodes to connect.
     */
    public void addNode(MapNode node, int[] otherNodes) {
        mapNodes.add(node);
        for(int i = 0 ; i < otherNodes.length; i++){
            MapNode temp;
            try {
                // Add each other node to new node if found
                temp = mapNodes.get(otherNodes[i]);
                if(temp.equals(node)){
                    System.out.println("Trying to connect MapNodes with same indexes : " + otherNodes[i] + " Skipping...");
                    continue;
                }
                node.edges.add(temp);
                temp.edges.add(node);
            }catch (IndexOutOfBoundsException e){
                System.out.println("Trying to connect MapNodes with out of bounds indexes : " + otherNodes[i]);
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
