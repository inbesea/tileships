package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;

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
}
