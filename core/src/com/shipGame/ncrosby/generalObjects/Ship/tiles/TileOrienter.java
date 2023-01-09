package com.shipGame.ncrosby.generalObjects.Ship.tiles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;

/**
 * Class to handle getting the orientation of two tiles.
 */
public class TileOrienter {

    ShipTile tileOne;
    ShipTile tileTwo;
    Array<ShipTile> tileArray;


    int currentOrientation;

    public TileOrienter(Array<ShipTile> tileArray){
        this.tileArray = tileArray;
        this.tileOne = tileArray.get(0);
        this.tileTwo = tileArray.get(1);
        currentOrientation = calculateOrientation(); // Init orientation
    }

    /**
     * reviews the first pair of tiles to parse their relative orientation and return an int representing orientation.
     *
     * This is based on the index's relationship to one another, and they must be orthogonally adjacent
     * The result is only meant to be a way to match the shape to a recipe so orientation of the containing ship should not matter.
     *
     * @return Returns 0-3 representing orientation between the two tiles. -1 if invalid inputs.
     *      * 0 == Up
     *      * 1 == Right
     *      * 2 == Down
     *      * 3 == Left
     */
    public int calculateOrientation() {
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

    /**
     * Method to rotate the currently set array around it's first tile to point in a direction.
     * The first two tiles are where the direction is initially.
     * Rotating them in relationship to one another determines the overall new tile positions.
     *      * 0 == Up
     *      * 1 == Right
     *      * 2 == Down
     *      * 3 == Left
     *
     * @param direction - int describing the new direction to point the tile array
     * @return - newly oriented array
     */
    public Array<ShipTile> orientArray(int direction){
        return null;
    }
}
