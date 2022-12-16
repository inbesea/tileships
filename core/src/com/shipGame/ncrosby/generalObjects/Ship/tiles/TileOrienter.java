package com.shipGame.ncrosby.generalObjects.Ship.tiles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Class to handle getting the orientation of two tiles.
 */
public class TileOrienter {

    ShipTile tileOne;
    ShipTile tileTwo;

    public TileOrienter(ShipTile tileOne, ShipTile tileTwo){
        this.tileOne = tileOne;
        this.tileTwo = tileTwo;
    }

    /**
     * review a pair of tiles to parse their relative orientation and return an int representing orientation.
     *
     * This is based on the index's relationship to one another, and they must be orthogonally adjacent
     * The result is only meant to be a way to match the shape to a recipe so orientation of the containing ship should not matter.
     *
     * @return Returns 0-3 representing orientation between the two tiles. -1 if invalid inputs.
     */
    public int getOrientation() {
        int result;
        Vector2 tile0Index = new Vector2(tileOne.getxIndex(), tileOne.getyIndex());
        Vector2 tile1Index = new Vector2(tileTwo.getxIndex(), tileTwo.getyIndex());

        boolean xSame = tile0Index.x == tile1Index.x;
        boolean ySame = tile0Index.y == tile1Index.y;

        // Addition
        if(tile0Index.y + 1 == tile1Index.y && xSame){ // Up
            result = 0;
            return result;
        } else if(tile0Index.x + 1 == tile1Index.x && ySame) { // Right
            return 1;
        } else if(tile0Index.y - 1 == tile1Index.y && xSame) { // Down
            return 2;
        } else if(tile0Index.x - 1 == tile1Index.x && ySame) { // Left
            return 3;
        } else {
            Gdx.app.debug("CondenserMessage", "Incomparable tiles passed to GetOrientation()" + tile0Index + " " + tile1Index);
            result = -1;
            return result;
        }
    }
}
