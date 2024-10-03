package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

public class ShipTileUtility {

    public static Vector2 getRandomPointOnTile(ShipTile tile){
        Vector2 tilePosition = tile.getPosition();
        Vector2 randomPointOnTile = new Vector2();


        randomPointOnTile.x = generalUtil.getRandomNumber(tilePosition.x, tilePosition.x + ShipTile.TILE_SIZE);
        randomPointOnTile.y = generalUtil.getRandomNumber(tilePosition.y, tilePosition.y + ShipTile.TILE_SIZE);


        return randomPointOnTile;
    }

}
