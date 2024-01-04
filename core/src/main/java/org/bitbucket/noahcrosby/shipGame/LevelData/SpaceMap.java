package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import java.util.Comparator;

/**
 * Holds the map graph, relates to other map graphs
 *
 * Is NOT responsible for where the player is, or what the nodes are doing. Just relates the nodes and the edges to other maps.
 */
public class SpaceMap {
    Array<MapNode> mapNodes;
    MapNode entryNode;
    MapNode exitNode;

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

    public MapNode addNode(Vector2 position){
        MapNode mapNode = new MapNode(position);
        mapNodes.add(mapNode);
        return mapNode;
    }

    /**
     * Adds basic node to a map location and returns it
     * @param x - x position
     * @param y - y position
     * @return - the new node
     */
    public MapNode addNode(int x, int y){
        MapNode mapNode = new MapNode(new Vector2(x, y));
        mapNodes.add(mapNode);
        return mapNode;
    }

    /**
     * Add node setting closest node connections
     * @param x -  x position
     * @param y - y position
     * @param connections - number of connections
     * @return - the new node
     */
    public MapNode addNode(int x, int y, int connections){
        MapNode mapNode = new MapNode(new Vector2(x, y));
        mapNodes.add(mapNode);

        if(connections == 0){
            return mapNode;
        }else {
            for(int i = 1; i < connections + 1; i++){ // Starts at one to not connect to itself
                connectNodes(mapNode, getNthClosestNode(new Vector2(x,y),i));
            }
        }
        return mapNode;

        //mapNode.setConnections(connections); // Meant to give a *connections* number of connections
        // I think the AI suggested this. Not sure it's a valid way to connect nodes, but we want to fix this later.
    }

    public MapNode getClosestNode(Vector2 position){
        return generalUtil.getClosestObject(position, mapNodes);
    }

    /**
     * Returns the Nth closest node where 0 is the closest node, 1 is the second closest, etc.
     * @param position - The position to find the closest node
     * @param n - The placement of the returned node
     * @return - The Nth closest node
     */
    public MapNode getNthClosestNode(Vector2 position, int n){
        sortByDistance(mapNodes, position);
        return mapNodes.get(n);
    }

    public static Array<MapNode> sortByDistance(Array<MapNode> nodes, Vector2 position) {
        nodes.sort(Comparator.comparingDouble(vector -> calculateDistance(vector.getPosition(), position)));
        return nodes;
    }

    // Function to calculate the distance between two points
    public static double calculateDistance(Vector2 point1, Vector2 point2) {
        double dx = point1.x - point2.x;
        double dy = point1.y - point2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public MapNode addNode(int x, int y, float radius){
        MapNode mapNode = new MapNode(new Vector2(x, y));
        mapNodes.add(mapNode);
        return mapNode;
    }

    /**
     * Connect a node up to the others nearby.
     * @param node - The node to connect
     * @param radius - The radius around the node to limit connections by
     */
    public void connectNode(MapNode node, int radius) {
        Array<MapNode> nearbyNodes = getNodesInArea(node.getPosition(), radius);
        for (MapNode nearbyNode : nearbyNodes) {
            connectNodes(node, nearbyNode);
        }
    }

    /**
     * Gets all nodes in an area
     * @param center
     * @param radius
     * @return
     */
    public Array<MapNode> getNodesInArea(Vector2 center,int radius){
        Array<MapNode> mapNodes = this.getMapNodes();
        Array<MapNode> nodes = new Array<>();

        for(MapNode node : mapNodes){
            if(node.getPosition().dst(center) < radius){
                nodes.add(node);
            }
        }

        return nodes;
    }

    /**
     * Uses explicit array indexes to connect nodes.
     * Can also connect nodes if exists.
     *
     * @param node
     * @param otherNodes - Array indexes of nodes to connect.
     */
    public void addNode(MapNode node, int[] otherNodes) {
        if(!this.mapNodes.contains(node, true)){
            mapNodes.add(node);
        }

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
     * Connects two nodes
     * Can be used to fix one way connections, by checking for missing connections before adding to edges.
     *
     * @param n1 - First node
     * @param n2 - Second node
     */
    public static void connectNodes(MapNode n1, MapNode n2){
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

    public void setExitNode(MapNode mapNode) {
        this.exitNode = mapNode;
    }

    public MapNode setEntryNode(MapNode entryNode) {
        this.entryNode = entryNode;
        return entryNode;
    }

    /**
     * Remove a node from the map, disconnects all edges from the node
     */
    public void removeNode(MapNode mapNode){
        if(!mapNodes.contains(mapNode, true)){
            Gdx.app.debug("SpaceMap", "RemoveNode : MapNode not found");
            return;
        }
        this.disconnectNode(mapNode);
        mapNodes.removeValue(mapNode, true);
    }

    /**
     * Disconnects all edges from a node, and removes its references from other nodes
     * @param mapNode
     */
    private void disconnectNode(MapNode mapNode) {
        for(MapNode node : mapNode.edges){
            this.disconnectNodes(mapNode, node);
        }
    }
}
