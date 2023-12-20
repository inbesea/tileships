package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;

public class MapUtils {
    public static SpaceMap getDefaultMap() {
        SpaceMap map = new SpaceMap();
        map.setEntryNode(map.addNode(200, 300));
        map.addNode(300, 300, 1);
        map.addNode(new MapNode(new Vector2(400, 300)), new int[]{1});
        return map;
    }
}
