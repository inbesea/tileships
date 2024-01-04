package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.apache.commons.lang3.tuple.Pair;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;

import java.util.*;

public class MapUtils {
    public static ArrayList<ArrayList<MapNode>> getMapGroups(Array<MapNode> mapNodes){
        // Loop nodes and check for orphaned groups
        Array<MapNode> unvisitedMapNodes = new Array<>(); // Move to next nodes bit by bit
        unvisitedMapNodes.addAll(mapNodes);
        Array<MapNode> visited = new Array<>();
        Stack<MapNode> nextNodes = new Stack<>();

        // Give return array a group
        ArrayList<MapNode> aGroupOfNodes = new ArrayList<>();
        ArrayList<ArrayList<MapNode>> groups = new ArrayList<>();
        groups.add(aGroupOfNodes);

        if(unvisitedMapNodes.size == 0){
            Gdx.app.debug("MapUtils.getMapGroups()","No groups found");
            return groups;
        }
        // Get the first node
        nextNodes.push(unvisitedMapNodes.pop());

        // Draw the nodes and edges
        int uncountedNodeCount = unvisitedMapNodes.size + nextNodes.size();

        while (uncountedNodeCount > 0) { // There are unvisited nodes
            if(nextNodes.size() == 0){ // If no nodes left we have more than one group.
                // If this doesn't happen the return array will have a single group
                aGroupOfNodes = new ArrayList<>();
                groups.add(aGroupOfNodes); // Reset the array and add the new array.

                nextNodes.push(unvisitedMapNodes.pop());
            }
            MapNode currentNode = nextNodes.pop();// Get the next node
            for(int i = 0 ; i < currentNode.getEdges().size ; i++){ // Check all edges
                MapNode mapNode = currentNode.getEdges().get(i);

                if(!mapNode.getVisited() || !currentNode.getVisited()){
                    currentNode.setVisited(true); // Marking so we don't loop forever
                }
                // Making sure we haven't queued this or visited completely before queuing it
                if(!nextNodes.contains(mapNode) && !visited.contains(mapNode, true))nextNodes.push(mapNode);
            }

            // Move current node from unvisited to visited
            visited.add(currentNode);
            aGroupOfNodes.add(currentNode);
            unvisitedMapNodes.removeValue(currentNode, true);
            // Update count
            uncountedNodeCount = unvisitedMapNodes.size + nextNodes.size();
        }

        // Reset visited status
        for(int i = 0; i < mapNodes.size; i++){
            mapNodes.get(i).setVisited(false);
        }

        return groups;
    }

    /**
     * Takes a map and checks for multiple groups of interconnected nodes. If there are orphaned groups they are connected.
     * @param map
     * @return
     */
    public static SpaceMap fixOrphanedMapGroups(SpaceMap map){
        ArrayList<ArrayList<MapNode>> groups = getMapGroups(map.getMapNodes());

        while(groups.size() > 1) {
            // Just fix the first group lol
            ArrayList<MapNode> orphanedGroup = groups.get(0);

            // Get all other nodes
            ArrayList<MapNode> otherNodes = new ArrayList<>();
            for(int n = 1; n < groups.size(); n++){
                otherNodes.addAll(groups.get(n));
            }

            // Connect the two closest nodes between the first group and second lol.
            connectClosestNodes(orphanedGroup, otherNodes);

            // Update condition
            groups = getMapGroups(map.getMapNodes());
        }
        return map;
    }

    /**
     * Iterates through two lists of nodes and connects the closest two nodes, one from each list.
     * @param nodes - a list of nodes
     * @param otherNodes - another list of nodes
     */
    public static void connectClosestNodes(ArrayList<MapNode> nodes, ArrayList<MapNode> otherNodes){
        MapNode firstNode;
        MapNode secondNode;
        Pair<MapNode, MapNode> theClosestNodes = Pair.of(null, null);
        float minDistance = Integer.MAX_VALUE;

        // Find the closest two nodes from the two lists
        for(int i = 0; i < nodes.size(); i++){
            // Check agaist all other nodes, noting the least distance
            firstNode = nodes.get(i);
            for(int n = 0; n < otherNodes.size(); n++){
                secondNode = otherNodes.get(n);
                if(firstNode.getPosition().dst(secondNode.getPosition()) < minDistance){
                    minDistance = firstNode.getPosition().dst(secondNode.getPosition());
                    theClosestNodes = Pair.of(firstNode, secondNode);
                }
            }
        }
        // Call SpaceMap to statically connect the two nodes
        SpaceMap.connectNodes(theClosestNodes.getLeft(), theClosestNodes.getRight());
    }
}
