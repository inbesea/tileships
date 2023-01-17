package com.shipGame.ncrosby.generalObjects.Ship.tiles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.AdjacentTiles;

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
     *      * -1 == not enough tiles, or invalid
     */
    public int calculateOrientation() {
        int result;
        Vector2 tile0Index = new Vector2(tileOne.getxIndex(), tileOne.getyIndex());
        Vector2 tile1Index = new Vector2(tileTwo.getxIndex(), tileTwo.getyIndex());

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
     * @return - newly oriented array, null if no orientation can be done.
     */
    public Array<ShipTile> orientArray(int direction){
        if(4 > currentOrientation && currentOrientation > -1){ // Check that current orientation is valid

            if(currentOrientation == direction)return tileArray; // Check for no rotation needed

            /* If current orientation and direction are valid and do not match then we can begin orientation */
            else if(direction == AdjacentTiles.UP){ // Check needed direction
                return orientUpwards();
            } else if(direction == AdjacentTiles.RIGHT) {
                return orientRight();
            } else if (direction == AdjacentTiles.DOWN){
                return orientDown();
            } else if (direction == AdjacentTiles.LEFT){
                return orientLeft();
            } else {
                return null;
            }

        }
        return null;
    }

    private Array<ShipTile> orientLeft() {
        return null;
    }

    private Array<ShipTile> orientDown() {
        return null;
    }

    private Array<ShipTile> orientRight() {
        return null;
    }

    private Array<ShipTile> orientUpwards() {
        return null;
    }


    private void flipXYIndexes(){

    }

    /**
     * Will multiply the x or y indexes to the opposite
     * @param xOrY
     */
    private void switchXYPolarity(int xOrY){

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

//    /**
//     * Method to create a new array of tiles with different orientation and return it while setting the tileOrienter's
//     * local array to the new value.
//     * @param newOrientation
//     * @return
//     */
//    public Array<ShipTile> rotateAndReturnArrayOrientation(int newOrientation) {
//        this.currentOrientation = newOrientation;
//        Array<ShipTile> newArray = new Array<>();
//        ShipTile temp;
//
//        for(int i = 0 ; i < tileArray.size ; i++){
//            temp = tileArray.get(i);
//            newArray.add(new ShipTile()); // I need to turn each tile's location.
//            // We are using neighbors to figure out what the relationships are, but that requires the Ship object to do.
//            // Actually this is a better reason to just rotate each individually. for the string...
//        }
//        return null;
//    }

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
        int result = -1;

        // Number to change, originalDirection, newDirection
        // If original direction is opposite from new then just flip the number
        if(opposites(newDirection, currentOrientation))result = flip(aDirectionToChange);
        else if(toTheRight(currentOrientation, newDirection)){
            result = rightRotate(aDirectionToChange);
        } else if(toTheLeft(currentOrientation, newDirection)){
            result = leftRotate(aDirectionToChange);
        }

        if(result == -1) Gdx.app.debug("Orientation","A number did a stupid");
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
