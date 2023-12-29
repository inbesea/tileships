package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.AsteroidSpawner;

public class Maps {

    public static SpaceMap getTestMap(){
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
}
