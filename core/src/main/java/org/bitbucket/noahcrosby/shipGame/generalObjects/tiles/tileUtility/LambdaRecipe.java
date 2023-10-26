package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

public interface LambdaRecipe {
    ShipTile run(Array<ShipTile> tiles);
}
