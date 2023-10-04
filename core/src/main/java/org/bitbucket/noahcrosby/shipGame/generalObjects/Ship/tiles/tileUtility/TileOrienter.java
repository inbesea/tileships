package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileUtility;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;

/**
 * Class to handle orienting an array of tiles.
 * On init this will take an array to work with.
 * This class can get orientation, be told to reorient its array, and give the orientation of the array
 */
public class TileOrienter {

    ShipTile tileOne;
    ShipTile tileTwo;
    Array<ShipTile> tileArray;


    int currentOrientation;

    public TileOrienter(Array<ShipTile> tileArray){
        if(tileArray.size == 1 || tileArray.isEmpty())throw new IllegalArgumentException("Illegal argument passed to TileOrienter, size : " + tileArray.size);
        this.tileArray = tileArray;
        this.tileOne = tileArray.get(0);
        this.tileTwo = tileArray.get(1);
        currentOrientation = calculateOrientation(); // Init orientation
    }

    /**
     * Constructor to allow orientation without an internal array
     * @param currentOrientation - assumed orientation to move from.
     */
    public TileOrienter(int currentOrientation){
        this.tileArray = null;
        this.tileOne = null;
        this.tileTwo = null;
        this.currentOrientation = currentOrientation;
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
     *      * -1 == not enough tiles, or invalid
     */
    public int calculateOrientation() {
        int result;
        Vector2 tile0Index = new Vector2(tileOne.getXIndex(), tileOne.getYIndex());
        Vector2 tile1Index = new Vector2(tileTwo.getXIndex(), tileTwo.getYIndex());

        boolean xSame = tile0Index.x == tile1Index.x;
        boolean ySame = tile0Index.y == tile1Index.y;

        // Addition
        if(tile0Index.y + 1 == tile1Index.y && xSame){ // Up
            return setAndReturnCurrentOrientation(AdjacentTiles.UP);
        } else if(tile0Index.x + 1 == tile1Index.x && ySame) { // Right
            return setAndReturnCurrentOrientation(AdjacentTiles.RIGHT);
        } else if(tile0Index.y - 1 == tile1Index.y && xSame) { // Down
            return setAndReturnCurrentOrientation(AdjacentTiles.DOWN);
        } else if(tile0Index.x - 1 == tile1Index.x && ySame) { // Left
            return setAndReturnCurrentOrientation(AdjacentTiles.LEFT);
        } else { // Is reached if the array is too short
            System.out.println("Incomparable tiles passed to calculateOrientation()" + tile0Index + " " + tile1Index);
            result = -1;
            return result;
        }
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
     *      * -1 == not enough tiles, or invalid
     */
    public static int calculateOrientation(ShipTile tileOne, ShipTile tileTwo) {
        int result;
        Vector2 tile0Index = new Vector2(tileOne.getXIndex(), tileOne.getYIndex());
        Vector2 tile1Index = new Vector2(tileTwo.getXIndex(), tileTwo.getYIndex());

        boolean xSame = tile0Index.x == tile1Index.x;
        boolean ySame = tile0Index.y == tile1Index.y;

        // Addition
        if(tile0Index.y + 1 == tile1Index.y && xSame){ // Up
            return AdjacentTiles.UP;
        } else if(tile0Index.x + 1 == tile1Index.x && ySame) { // Right
            return AdjacentTiles.RIGHT;
        } else if(tile0Index.y - 1 == tile1Index.y && xSame) { // Down
            return AdjacentTiles.DOWN;
        } else if(tile0Index.x - 1 == tile1Index.x && ySame) { // Left
            return AdjacentTiles.LEFT;
        } else { // Is reached if the array is too short
            Gdx.app.debug("TileOrienter", "Incomparable tiles passed to calculateOrientation()" + tile0Index + " " + tile1Index);
            result = -1;
            return result;
        }
    }

    public int getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(int currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public int setAndReturnCurrentOrientation(int currentOrientation) {
        this.currentOrientation = currentOrientation;
        return currentOrientation;
    }


    /**
     * Getter for the local tile Array
     * @return - Array of tiles
     */
    public Array<ShipTile> getTileArray() {
        return tileArray;
    }

    /**
     * Coordinator function to orient the numbers.
     * <p>
     * Tired of this long process to make this work lol
     * @param aDirectionToChange - number to switch
     * @param newDirection - direction to orient to
     * @return - New oriented number if valid, -1 if problem
     */
    public int directionRemap(int aDirectionToChange, int newDirection) {
        if(currentOrientation == newDirection)return aDirectionToChange; // If new direction is same as original, then return unchanged
        int result = -1;

        // Number to change, originalDirection, newDirection
        // If original direction is opposite from new then just flip the number
        if(opposites(newDirection, currentOrientation))result = flip(aDirectionToChange);
        else if(toTheRight(currentOrientation, newDirection)){
            result = rightRotate(aDirectionToChange);
        } else if(toTheLeft(currentOrientation, newDirection)){
            result = leftRotate(aDirectionToChange);
        }

        if(result == -1) System.out.println("Orientation - A number did a stupid");
        return result;
    }

    /**
     * Checks if new direction is directly to the left of the current orientation
     * @param current
     * @param newOrientation
     * @return
     */
    private boolean toTheLeft(int current, int newOrientation) {
        boolean toTheLeft =
                (current == AdjacentTiles.UP && newOrientation == AdjacentTiles.LEFT ||
                        current == AdjacentTiles.DOWN && newOrientation == AdjacentTiles.RIGHT ||
                        current == AdjacentTiles.RIGHT && newOrientation == AdjacentTiles.UP ||
                        current == AdjacentTiles.LEFT && newOrientation == AdjacentTiles.DOWN);

        return toTheLeft;
    }

    /**
     * Checks if new direction is directly to the right of the current orientation
     * @param current
     * @param newOrientation
     * @return
     */
    private boolean toTheRight(int current, int newOrientation) {
        boolean toTheRight =
                (current == AdjacentTiles.UP && newOrientation == AdjacentTiles.RIGHT ||
                        current == AdjacentTiles.DOWN && newOrientation == AdjacentTiles.LEFT ||
                        current == AdjacentTiles.RIGHT && newOrientation == AdjacentTiles.DOWN ||
                        current == AdjacentTiles.LEFT && newOrientation == AdjacentTiles.UP);

        return toTheRight;
    }


    /**
     * Checks if points are on opposite sides
     * @param compassPoint1 - direction
     * @param compassPoint2 - another direction
     * @return - true if opposites, else false.
     */
    private boolean opposites(int compassPoint1, int compassPoint2){

        boolean areOpposite =
                (compassPoint2 == AdjacentTiles.UP && compassPoint1 == AdjacentTiles.DOWN ||
                compassPoint2 == AdjacentTiles.DOWN && compassPoint1 == AdjacentTiles.UP ||
                compassPoint2 == AdjacentTiles.RIGHT && compassPoint1 == AdjacentTiles.LEFT ||
                compassPoint2 == AdjacentTiles.LEFT && compassPoint1 == AdjacentTiles.RIGHT);

        return areOpposite;
    }

    /**
     * Returns nextTileDirection with a 90 degree turn to the right
     * @param nextTileDirection
     * @return
     */
    private int rightRotate(int nextTileDirection) {
        if(AdjacentTiles.UP == nextTileDirection)return AdjacentTiles.RIGHT;
        if(AdjacentTiles.RIGHT == nextTileDirection) return AdjacentTiles.DOWN;
        if(AdjacentTiles.DOWN == nextTileDirection) return AdjacentTiles.LEFT;
        if(AdjacentTiles.LEFT == nextTileDirection) return AdjacentTiles.UP;

        return AdjacentTiles.INVALID; // No match
    }

    /**
     * returns nextTileDirection with a 90 degree turn to the left
     * @param nextTileDirection
     * @return
     */
    private int leftRotate(int nextTileDirection) {
        if(AdjacentTiles.UP == nextTileDirection)return AdjacentTiles.LEFT;
        if(AdjacentTiles.RIGHT == nextTileDirection) return AdjacentTiles.UP;
        if(AdjacentTiles.DOWN == nextTileDirection) return AdjacentTiles.RIGHT;
        if(AdjacentTiles.LEFT == nextTileDirection) return AdjacentTiles.DOWN;

        return AdjacentTiles.INVALID; // No match
    }

    /**
     * Flips direction to opposite on the compass directions
     * @param nextTileDirection - direction to reorient
     * @return - oriented direction
     */
    private int flip(int nextTileDirection) {
        if(AdjacentTiles.UP == nextTileDirection)return AdjacentTiles.DOWN;
        if(AdjacentTiles.RIGHT == nextTileDirection) return AdjacentTiles.LEFT;
        if(AdjacentTiles.DOWN == nextTileDirection) return AdjacentTiles.UP;
        if(AdjacentTiles.LEFT == nextTileDirection) return AdjacentTiles.RIGHT;

        return AdjacentTiles.INVALID; // No match
    }
}
