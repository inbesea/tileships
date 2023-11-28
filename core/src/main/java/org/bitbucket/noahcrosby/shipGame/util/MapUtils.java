package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.LevelData.MapNode;
import org.bitbucket.noahcrosby.LevelData.SpaceMap;

public class MapUtils {
    public static SpaceMap getDefaultMap() {
        SpaceMap map = new SpaceMap();
        map.addNode(new MapNode(new Vector2(200, 300)));
        map.addNode(new MapNode(new Vector2(300, 300)), new int[]{0});
        map.addNode(new MapNode(new Vector2(400, 300)), new int[]{1});
        return map;
    }
}
