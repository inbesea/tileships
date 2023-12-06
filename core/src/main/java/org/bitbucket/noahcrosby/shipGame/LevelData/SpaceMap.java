package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Holds the map graph, draws the map graph
 */
public class SpaceMap {
    Array<MapNode> mapNodes;
    MapNode entryNode;
    MapNode exitNode;
    private MapNode selectedNode;


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
                connectNodes(node, temp);
            }catch (IndexOutOfBoundsException e){
                System.out.println("Trying to connect MapNodes with out of bounds indexes : " + otherNodes[i]);
            }
        }
    }

    /**
     * Connects two nodes without duplication
     *
     * Will fix one way connections, by checking for missing connections before adding to edges.
     * @param n1
     * @param n2
     * @return
     */
    public void connectNodes(MapNode n1, MapNode n2){
        if(!n1.edges.contains(n2, true)){
            n1.edges.add(n2);
        }
        if(!n2.edges.contains(n1, true)){
            n2.edges.add(n1);
        }
    }

    /**
     * Disconnects two nodes from each other
     * @param n1
     * @param n2
     * @return
     */
    public void disconnectNodes(MapNode n1, MapNode n2){
        // Will remove connections from both, one, or neither
        if(n1.edges.contains(n2, true))n1.edges.removeValue(n2, true);
        if(n2.edges.contains(n1, true))n2.edges.removeValue(n1, true);
    }

    public Array<MapNode> getMapNodes() {
        return mapNodes;
    }

    public void setMapNodes(Array<MapNode> mapNodes) {
        this.mapNodes = mapNodes;
    }

    public MapNode getEntryNode() {
        if(this.mapNodes.size == 0){
            Gdx.app.error("SpaceMap", "No MapNodes found");
            return null;
        }else if(entryNode == null){ // Is nodes, but no entry
            Gdx.app.log("SpaceMap", "No EntryNode found... setting default");
            setEntryNode(mapNodes.get(0)); // set entry to default
            return mapNodes.get(0);
        } else { // Has entry, and nodes
            return entryNode;
        }
    }

    /**
     * Returns the exit node and sets last node to exit node if no exit node is found
     * @return
     */
    public MapNode getExitNode() {
        if(this.mapNodes.size == 0){
            Gdx.app.error("SpaceMap", "No MapNodes found");
            return null;
        }else if(exitNode == null){ // Is nodes, but no entry
            Gdx.app.log("SpaceMap", "No EntryNode found... setting default");
            setExitNode(mapNodes.get(mapNodes.size - 1)); // set entry to default (last node)
            if(entryNode.equals(exitNode)){
                Gdx.app.log("SpaceMap", "EntryNode and ExitNode are the same!!!");
            }
            return exitNode;
        } else { // Has entry, and nodes
            return entryNode;
        }
    }

    private void setExitNode(MapNode mapNode) {
        this.exitNode = mapNode;
    }

    public void setEntryNode(MapNode entryNode) {
        this.entryNode = entryNode;
    }

    public void selectNode(MapNode newSelectedNode) {
        if(!this.mapNodes.contains(newSelectedNode, true)){
            Gdx.app.error("Missing Node", "Cannot find selected node in current SpaceMap");
            return;
        }
        newSelectedNode.clicked(true);
        this.selectedNode = newSelectedNode;
    }

    public MapNode getSelectedNode() {
        return this.selectedNode;
    }
}
