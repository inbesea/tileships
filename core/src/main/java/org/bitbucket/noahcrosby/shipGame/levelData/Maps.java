package org.bitbucket.noahcrosby.shipGame.levelData;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.AsteroidSpawner;
import org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.AsteroidTable;
import org.bitbucket.noahcrosby.shipGame.util.MapUtils;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;
import org.bitbucket.noahcrosby.shipGame.levelData.SpaceMap;

import java.util.ArrayList;


public class Maps {

    public static org.bitbucket.noahcrosby.shipGame.levelData.SpaceMap getTestMap(){
        SpaceMap map = new SpaceMap();



        return map;
    }

    public static SpaceMap getDefaultMap() {
        SpaceMap map = new SpaceMap();
        map.setEntryNode(map.addNode(200, 300))
            .setAsteroids(AsteroidSpawner.generateRandomAsteroidsStatic(AsteroidTable.EntryNodeStd(), AsteroidTable.SOME()));
        map.addNode(300, 300, 1);
        map.addNode(new MapNode(new Vector2(400, 300)), new int[]{1});
        return map;
    }

    /**
     * Generates a basic spacemap
     * @param nodes
     * @return
     */
    public static SpaceMap getBasicSpacemap(int nodes){
        SpaceMap map = new SpaceMap();
        map.setEntryNode(map.addNode(10,10)).setAsteroids(AsteroidSpawner.generateRandomAsteroidsStatic(AsteroidTable.EntryNodeStd(), AsteroidTable.SOME()));
        // Generate a bunch of nodes
        for(int i = 0; i < nodes; i++){
            map.addNode(generalUtil.getRandomNumber(10, 500), generalUtil.getRandomNumber(10, 500))
                .setAsteroids(AsteroidSpawner.generateRandomAsteroidsStatic(AsteroidTable.EntryNodeStd(), AsteroidTable.FEW()));// The map should be Zoomable.
            // We can have four produce some key asteroids and set the furthest one as a endpoint node later
        }

        // Connect nodes that are close enough
        for(int i = 0; i < map.getMapNodes().size; i++){
            MapNode node = map.getMapNodes().get(i);
            for(int n = 0; n < map.getMapNodes().size; n++){
                MapNode otherNode = map.getMapNodes().get(n);

                boolean notTheSame = (node != otherNode);
                boolean notTooFar = node.getPosition().dst(otherNode.getPosition()) < 100;
                if(notTheSame && notTooFar){
//                    map.addEdge(node, otherNode);
                    // Connect close nodes
                    map.connectNodes(node, otherNode);
                }
            }
        }

        // Fix groups of disconnected nodes in the map
        ArrayList<ArrayList<MapNode>> mapNodes = MapUtils.getMapGroups(map.getMapNodes());
        while(mapNodes.size() > 1){
            // Do something idk
            // Get the two nodes closest to each other and from the same group to connect up.

            MapUtils.fixOrphanedMapGroups(map);

            mapNodes = MapUtils.getMapGroups(map.getMapNodes());
        }

        // Connect up orphaned nodes with the closest node lol
        for(MapNode node : map.getMapNodes()){
            if(node.getEdges().size == 0){
                map.connectNodes(map.getClosestNode(node.getPosition()), node);
            }
        }

        return map;
    }
}
