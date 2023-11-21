package org.bitbucket.noahcrosby.shipGame.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import org.bitbucket.noahcrosby.LevelData.MapNode;
import org.bitbucket.noahcrosby.LevelData.SpaceMap;


/**
 * Handles the navigation of the maps
 */
public class MapNavManager {
    Array<SpaceMap> mapList;
    public SpaceMap currentMap;
    MapNode currentNode;

    public MapNavManager(){
        mapList = new Array<>();

        addMap(new SpaceMap());
    }

    public void addMap(SpaceMap map){
        mapList.add(map);
        if(currentMap == null){
            setCurrentMap(map);
        }
    }

    public SpaceMap getCurrentMap(){
        return currentMap;
    }

    public void setCurrentMap(SpaceMap map){
        // TODO implement and check if the map is adjacent to the current map
//        if()
        currentMap = map;
    }


}
