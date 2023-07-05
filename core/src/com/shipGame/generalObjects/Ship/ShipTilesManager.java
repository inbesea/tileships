package com.shipGame.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ID;
import com.shipGame.generalObjects.Ship.tiles.tileUtility.AdjacentTiles;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.generalObjects.Ship.tiles.tileUtility.TileTypeFactory;
import com.shipGame.physics.box2d.Box2DWrapper;

import java.util.Arrays;
import java.util.Stack;

import static com.shipGame.util.generalUtil.*;

/**
 * Class to handle logic for placing/removing tiles to allow the ship to handle holding the pieces together rather than working the logic directly.
 */
public class ShipTilesManager {
    private Array<ShipTile> existingTiles;
    private Array<ShipTile> edgeTiles;
    Ship ship;

    /**
     * Constructor method
     * @param ship
     */
    public ShipTilesManager(Ship ship) {
        this.ship = ship;
        this.existingTiles = new Array<>();
        this.edgeTiles = new Array<>();
    }

    /**
     * This Method adds a tile to the ship with a reference to the tile's x and y
     * positions , the color, ID, and camera object for updating
     * the render location
     *
     * @param x - The unaligned x coordinate this tile will be added to
     * @param y - The unaligned y coordinate this tile will be added to
     * @param id - The ID of the tile
     * @return shipTile - this will be null if the space added to is not occupied, else will return the tile blocking
     */
    public ShipTile addTile(float x, float y, ID id) {

        // Use vector to set new tile
        Vector3 tileLocation = new Vector3();
        tileLocation.set(x,y,0);
        Vector2 tileLocation2 = new Vector2(tileLocation.x, tileLocation.y);
        ShipTile closestTile;
        ShipTile tempTile;

        ShipTile destinationTile = returnTile(tileLocation2);
        if(destinationTile == null){ // Released on empty space
            if(ship.getExistingTiles().size == 0) return gridAlignedxyTilePlacement(x, y , id); // Can add anywhere

            closestTile = closestTile(tileLocation2, ship.getExistingTiles());
            Vector2 closestExternalVacancy = getVectorOfClosestSide(closestTile, tileLocation);
            tempTile = gridAlignedxyTilePlacement(closestExternalVacancy.x, closestExternalVacancy.y, id);
        } else { // Released on Shiptile
            Vector2 nearestEmptySpace = closestInternalVacancy(tileLocation2); // Get closest Vacancy on edge tiles
            tempTile = gridAlignedxyTilePlacement(nearestEmptySpace.x, nearestEmptySpace.y, id);
        }

        if(existingTiles.size < edgeTiles.size){
            throw new RuntimeException("More edgeTiles than existing!" +
                    "\nexistingTiles.size : " + existingTiles.size +
                    "\nedgeTiles.size : " + edgeTiles.size);
        }
        return tempTile;

    }

    /**
     * Method to find the closest tile to a point given an Array of ShipTiles
     *
     * @param location - Vector to check prox. to
     * @param tiles - Array of Tiles to search in
     * @return - closest tile
     */
    public ShipTile closestTile(Vector2 location , Array<ShipTile> tiles) {
        if(tiles.size == 0)return null;
        if(tiles.size == 1)return tiles.get(0);

        double minDistance = Double.POSITIVE_INFINITY; // First check will always be true
        ShipTile tempT;
        ShipTile closestTile = null;
        Vector3 location3 = new Vector3(location.x, location.y, 0);
        Vector3 tileP;

        //Loop through ship to find the closest tile
        for (int i = 0 ; i < tiles.size ; i++){
            tempT = tiles.get(i);
            // Get middle of tile to check distance
            tileP = new Vector3(tempT.getPosition().x + ShipTile.TILESIZE/2.0f, tempT.getPosition().y  + ShipTile.TILESIZE/2.0f , 0);
            Float distance = location3.dst(tileP);

            // Check if distance between position and current tile is shorter
            if(distance < minDistance){
                minDistance = distance;
                closestTile = tempT;
            }
        }
        return closestTile;
    }

    /**
     * Adds a tile expecting the x,y to be a valid placement location
     */
    private ShipTile gridAlignedxyTilePlacement(float x, float y, ID id){
        int indexXY[];
        ShipTile tempTile;

        indexXY = calculateIndex(x, y); // Get index corresponding to x, y position

        System.out.println("Create " + id + " at [" + indexXY[0] + ", " + indexXY[1] + "] (" + x + "," + y + ")" +
                "\n(All tiles, Edge) -> (" + existingTiles.size + ", " + edgeTiles.size + ")");

        // Create tile subtype based on ID using factory static call.
        Vector2 vector2 = new Vector2(getGameSpacePositionFromIndex(indexXY[0]), getGameSpacePositionFromIndex(indexXY[1]));
        tempTile = TileTypeFactory.getShipTileTypeInstance(vector2, id, this);
        validateNewTileIndex(tempTile);
        this.existingTiles.add(tempTile);
        setNeighbors(tempTile); // Setting tile neighbors within ship

        Box2DWrapper.getInstance().setObjectPhysics(tempTile);


        //setTilePhysics(tempTile);
        if(existingTiles.size < edgeTiles.size){
            throw new RuntimeException("More edgeTiles than existing! " +
                    " existingTiles.size : " + existingTiles.size +
                    "edgeTiles.size : " + edgeTiles.size);
        }
        return tempTile;
    }

    private void validateNewTileIndex(ShipTile newTile) {
        ShipTile existingTile;
        for(int i = 0 ; i < existingTiles.size ; i++){
            existingTile = existingTiles.get(i);
            if(newTile.getxIndex() == existingTile.getxIndex() && newTile.getyIndex() == existingTile.getyIndex()){
                throw new RuntimeException("New tile " + newTile.getPositionAsString() + " id: " + newTile.getID() + " is being placed into existing tile location " +
                        existingTile.getPositionAsString() + " id : " + existingTile.getID());
            }
        }
    }

    /**
     * Returns a Vector position with respect to the grid.
     *
     * TODO : Should update ship so the grid is alligned with the shipposition, allowing us to move the ship and keep grid.
     * @param x
     * @param y
     * @return
     */
    public Vector2 getGridAlignedPosition(float x, float y){
        int[] indexes = calculateIndex(x, y);
        Vector2 vector2 = new Vector2(getGameSpacePositionFromIndex(indexes[0]), getGameSpacePositionFromIndex(indexes[1]));
        return vector2;
    }

    /**
     * Returns an index scaled up by ShipTile.TILESIZE
     * @return
     */
    public float getGameSpacePositionFromIndex(int index){
        return index * ShipTile.TILESIZE;
    }

    /**
     * Sets neighbors and checks if edge values change.
     * <p></p>
     * Gets contextual tiles via the ship's context, and calls delegate method within tile, passing the possible neighbor context.
     * <p></p>
     * Sets tile neighbors equal to adjacent tiles, or to null if they don't exist
     * @param tile - tile to initialize
     * @return - number of neighbor tiles
     */
    private int setNeighbors(ShipTile tile){

        // Init vars
        float x = tile.getX();
        float y = tile.getY();

        // Get adjacent tile references
        ShipTile up = returnTile(new Vector2(x,y + ShipTile.TILESIZE*1.5f));
        ShipTile right = returnTile(new Vector2(x + ShipTile.TILESIZE*1.5f, y));
        ShipTile down = returnTile(new Vector2(x,y - ShipTile.TILESIZE/2.0f));
        ShipTile left = returnTile(new Vector2(x - ShipTile.TILESIZE/2.0f, y));

        // tie the tiles together
        int numberOfNeighbors = tile.setNeighbors(up, right, down, left);

        checkIfAdjustEdgeArray(up);
        checkIfAdjustEdgeArray(right);
        checkIfAdjustEdgeArray(down);
        checkIfAdjustEdgeArray(left);
        checkIfAdjustEdgeArray(tile);

        if(existingTiles.size < edgeTiles.size){
            throw new RuntimeException("More edgeTiles than existing!" +
                    "\nexistingTiles.size : " + existingTiles.size +
                    "\nedgeTiles.size : " + edgeTiles.size);
        }

        return numberOfNeighbors;
    }

    /**
     * Checks if tile should be removed or added to edge array.
     *
     * Assumes that the neighbors have been updated and reflect the current shipstate
     */
    private void checkIfAdjustEdgeArray(ShipTile tile) {
        if(tile != null && existingTiles.size>1 && tile.numberOfNeighbors()==0){
            throw new RuntimeException("Tile was placed that has no neighbors despite other tiles existing");
        }else if(tile != null){
            boolean tileExistsAndShouldBeEdge = tile.isEdge && !edgeTiles.contains(tile, true) && existingTiles.contains(tile, true); // Gotta confirm the tile exists lol

            if(tileExistsAndShouldBeEdge){
                edgeTiles.add(tile);
                if(existingTiles.size < edgeTiles.size){
                    throw new RuntimeException("More edgeTiles than existing!" +
                            "\nexistingTiles.size : " + existingTiles.size +
                            "\nedgeTiles.size : " + edgeTiles.size);
                }
            } else if (!tile.isEdge && edgeTiles.contains(tile, true) && existingTiles.contains(tile, true)){
                edgeTiles.removeValue(tile, true);
            }  else  {
                // The
            }
        }
    }

    /**
     * Lazy bad way to get a tile with a different passed in value, but I don't feel like creating another method for both of these to call.
     * @param position
     * @return
     */
    public ShipTile returnTile(Vector2 position){
        return findTile(position);
    }

    /**
     * Returns reference to a tile based on x, y of Vector2
     *
     * @param position - click coordinates
     * @return  - Can return a ShipTile object. Will return null on spaces without tiles.
     */
    private ShipTile findTile(Vector2 position){
        int indexXY[] = calculateIndex(position.x, position.y);

        ShipTile temp;
        Stack<ShipTile> resultTiles = new Stack<>();

        // Searching ship tiles for matches based on position
        for(int i = 0; i < existingTiles.size; i++) {
            //Assign current tile to temp
            temp = existingTiles.get(i);
            // Check if x matches
            if(temp.getxIndex() == indexXY[0]) {
                // If yes then check y as well
                if(temp.getyIndex() == indexXY[1]) {
                    resultTiles.push(temp);
                }
            }
        }

        // Handle result array
        if(resultTiles.size() > 1) {
            throw new ArithmeticException("Multiple Tiles Found in ReturnTile() for " + position.x + "," + position.y);
        }
        else if (resultTiles.size() == 0) {
            return null;
        }
        else {
            return resultTiles.pop();
        }
    }

    /**
     * Returns the index of a tilelocation from an x,y location.
     *
     * @param x - The x location
     * @param y - The y location
     * @return - int array of 2 numbers. Index is a whole number
     */
    public int[] calculateIndex(float x, float y) {

        // A locations' index is determined by a range of 0 == x where x is between tilesize*0 -> tilesize*1 - 0.01

        // -1 shifting wont cause issues because the flow will subtract one from it either way
        // -64 - -1 will return index -1 yayy

//        x = Math.round(x);
//        y = Math.round(y);

        boolean yNegative = y <= -0.1;
        boolean xNegative = x <= -0.1;


        int XYresult[] = new int[2];
        if (xNegative) {
            if(yNegative) {
                // x, y negative
                // get index and subtract one.
                XYresult[0] = (int) Math.floor(( (x + 1) / ShipTile.TILESIZE));
                XYresult[1] = (int) Math.floor(( (y + 1) / ShipTile.TILESIZE));
            }
            else {
                // only x negative
                XYresult[0] = (int) Math.floor(( (x + 1) / ShipTile.TILESIZE));
                XYresult[1] = (int) Math.floor( (y) / ShipTile.TILESIZE);
            }
        }
        else if (yNegative) {
            // only Y negative
            XYresult[0] = (int) Math.floor((x) / ShipTile.TILESIZE);
            XYresult[1] = (int) Math.floor(( (y+ 1) / ShipTile.TILESIZE));
        }
        else {
            XYresult[0] = (int) Math.floor((x) / ShipTile.TILESIZE);
            XYresult[1] = (int) Math.floor((y) / ShipTile.TILESIZE);
        }

        // TODO : Unit test
        if(XYresult.length == 2) {
            return XYresult;
        }
        else{
            throw new ArithmeticException("Unexpected number of indexes : " + XYresult.length );
        }
    }

    /**
     * Takes in a tile, location, and closest tile and determines which side to place the tile.
     *
     * @param closestTile
     * @param mousePosition
     */
    public Vector2 getVectorOfClosestSide(ShipTile closestTile, Vector3 mousePosition) {

        // Get the closest side of the tile from the mouse position
        int closestSide = getQuadrant(closestTile.getCenter(), new Vector2(mousePosition.x, mousePosition.y));


        // We can conceptualize this as as a four triangles converging in the center of the "closest tile"
        // We can use this framing to decide the side to place the tile.
        if (closestSide == 0) { // North
            return  new Vector2(closestTile.getX()+(ShipTile.TILESIZE/2.0f), closestTile.getY() + ShipTile.TILESIZE*1.5f);
        } else if (closestSide == 3) { // West
            return  new Vector2(closestTile.getX() - (ShipTile.TILESIZE/2.0f), closestTile.getY()+(ShipTile.TILESIZE/2.0f));
        } else if (closestSide == 1) { // East
            return  new Vector2(closestTile.getX() + (ShipTile.TILESIZE * 1.5f), closestTile.getY() + (ShipTile.TILESIZE/2.0f));
        } else if (closestSide == 2) { // South
            return  new Vector2(closestTile.getX()+(ShipTile.TILESIZE/2.0f), closestTile.getY() - (ShipTile.TILESIZE/2.0f));
        } else {
            throw new RuntimeException("Something went wrong while running setTileOnClosestSide()");
        }
    }

    /**
     * Opposite of find ClosestTile,
     * expected to be used within the ship's tiles to find a vector on the nearest vacancy
     * Returns a Vector2 based on a vacancy closest to a point within the ship
     *
     * @param centerOfSearch
     * @return
     */
    public Vector2 closestInternalVacancy(Vector2 centerOfSearch) {
        ShipTile underVector = returnTile(centerOfSearch);

        if(underVector != null){
            // Get all Shiptiles that are on edge
            ShipTile nearestEdge = closestTile(centerOfSearch, edgeTiles);

            Array<Vector2> emptySideVectors = nearestEdge.emptySideVectors();
            emptySideVectors.addAll(getConnectedEmptyDiagonalPositions(nearestEdge));

            int nullSidesCount = emptySideVectors.size;
            // We have the closest tile (yay!) We need to act differently depending on the number of null sides.
            // or do we? Can we just add a vector to a list and the check which is closest?
            if(nullSidesCount == 0) {
                throw new RuntimeException("Something went wrong when getting nearest edge vectors, None Found");
            } else if (nullSidesCount == 1) {
                System.out.println("Only one side : " + emptySideVectors.peek().x + ", " + emptySideVectors.peek().y);
                return emptySideVectors.pop();
            } else if (8 > nullSidesCount) {
                Vector2 vector2 = closestVector2(centerOfSearch, emptySideVectors);
                return vector2;
            } else {
                throw new RuntimeException("Too many null sides!");
            }
        } else {// Handle trivial case - vector is on vacancy
            return centerOfSearch;
        }
    }

    /**
     * Takes a tile and returns the positions for adjacent corners that are empty and adjacent to the tile's neighbors
     * @param nearestEdge
     * @return
     */
    private Array<Vector2> getConnectedEmptyDiagonalPositions(ShipTile nearestEdge) {
        Array<Vector2> results = new Array<>();

        // Check if corners are adjacent to a neighbor.
        boolean UR_isConnected = false, UL_isConnected = false, DR_isConnected = false, DL_isConnected = false;
        if(nearestEdge.up() != null){
            UR_isConnected = UL_isConnected = true;
        }
        if(nearestEdge.right() != null){
            UR_isConnected = DR_isConnected = true;
        }
        if(nearestEdge.down() != null){
            DR_isConnected = DL_isConnected = true;
        }
        if(nearestEdge.left() != null){
            DL_isConnected = UL_isConnected = true;
        }

        // get diagonal locations
        // Get tilesize dependant offsets
        float bigOffset = ShipTile.TILESIZE * 1.5f;
        float smallOffset = ShipTile.TILESIZE/2f;

        Vector2 upRight;
        Vector2 upLeft;
        Vector2 downRight;
        Vector2 downLeft;

        // Take connected and empty spaces and add to the results.
        if(UR_isConnected){ // If space is connecected add to list if empty
            upRight = new Vector2(nearestEdge.getX() + bigOffset, nearestEdge.getY() + bigOffset);
            if(returnTile(upRight) == null) results.add(upRight);
        }
        if(UL_isConnected){
            upLeft = new Vector2(nearestEdge.getX() - smallOffset, nearestEdge.getY() + bigOffset);
            if(returnTile(upLeft) == null) results.add(upLeft);
        }
        if(DR_isConnected){
            downRight = new Vector2(nearestEdge.getX() + bigOffset, nearestEdge.getY() - smallOffset);
            if(returnTile(downRight) == null) results.add(downRight);
        }
        if(DL_isConnected){
            downLeft = new Vector2(nearestEdge.getX() - smallOffset, nearestEdge.getY() - smallOffset);
            if(returnTile(downLeft) == null) results.add(downLeft);
        }

        return results;
    }

    /**
     * Removes a tile by reference to the tile instance
     * Should only be used when a tile instance is found. Handles flushing the adjacency relationships between tiles.
     *
     * @param tile - Tile to remove from ship
     */
    public void removeTileFromShip(ShipTile tile){
        if(!this.existingTiles.removeValue(tile, true)){ // If not in existing tiles
            throw new RuntimeException("Error: Tile was not present in ship!\n-> " + tile.getAbbreviation() +
                    " - " + tile.getPositionAsString());
        } else {
            if(ship.isCollectingTiles()){ // Delete tile if being collected. Has to use the ship reference here.
                ship.getCollapseCollect().removeValue(tile, true);
            }
            removeNeighbors(tile);
            logRemovedTile(tile);

            // Remove from simulation
            Box2DWrapper.getInstance().removeObjectBody(tile.getBody());
        }
    }

    /**
     * Uses removeTileFromShip to remove all instances of an array of tiles in the ship
     *
     * @param tiles - Tiles to remove from ship
     */
    public void removeTilesFromShip(Array<ShipTile> tiles){
        for (int i = 0 ; i <= tiles.size ; i++){
            removeTileFromShip(tiles.get(i));
        }
    }

    /**
     * Decouples the passed tile from the adjacent tiles
     *
     * Responsible for removing the tile from the edge tiles if present.
     * @param shipTile - Tile to decouple
     */
    private void removeNeighbors(ShipTile shipTile){
        AdjacentTiles neighbors = shipTile.getNeighbors();

        // Remember to remove the tile from the existing tiles
        if(shipTile.isEdge()){ // If was an edge then try to remove
            if(!edgeTiles.removeValue(shipTile, true)){
                throw new RuntimeException("Removed tile was not found in edge");
            }
        }

        // Return if tile has no neighbors
        if(neighbors.numberOfNeighbors() == 0)return;

        float x = shipTile.getX();
        float y = shipTile.getY();


        // Get references
        ShipTile up = returnTile(new Vector2(x,y + ShipTile.TILESIZE));
        ShipTile right = returnTile(new Vector2(x + ShipTile.TILESIZE, y));
        ShipTile down = returnTile(new Vector2(x,y - ShipTile.TILESIZE));
        ShipTile left = returnTile(new Vector2(x - ShipTile.TILESIZE, y));

        // Remove reference to removed tile with null and set each to edge since an adjacent tile is removed
        if(up != null){
            up.setDown(null);
            if(!up.isEdge){ // if not already in the edge party add it
                up.setIsEdge(true);
                if(edgeTiles.contains(up,true)){
                    throw new RuntimeException("Non-edge tile present in edge tile array");
                } else {
                    edgeTiles.add(up);
                }
            }
        }
        if(right != null){
            right.setLeft(null);
            if(!right.isEdge){ // if not already in the edge party add it
                right.setIsEdge(true);
                if(edgeTiles.contains(right,true)){
                    throw new RuntimeException("Non-edge tile present in edge tile array");
                } else {
                    edgeTiles.add(right);
                }
            }
        }
        if(down != null){
            down.setUp(null);
            if(!down.isEdge){ // if not already in the edge party add it
                down.setIsEdge(true);
                if(edgeTiles.contains(down,true)){
                    throw new RuntimeException("Non-edge tile present in edge tile array");
                } else {
                    edgeTiles.add(down);
                }
            }
        }
        if(left != null){
            left.setRight(null);
            if(!left.isEdge){ // if not already in the edge party add it
                left.setIsEdge(true);
                if(edgeTiles.contains(left,true)){
                    throw new RuntimeException("Non-edge tile present in edge tile array");
                } else {
                    edgeTiles.add(left);
                }
            }
        }

        if(existingTiles.size < edgeTiles.size){
            throw new RuntimeException("More edgeTiles than existing! " +
                    " existingTiles.size : " + existingTiles.size +
                    "edgeTiles.size : " + edgeTiles.size);
        }
    }

    /**
     * Prints removed tile data to console
     */
    private void logRemovedTile(ShipTile tile) {
        System.out.println("Removing tile (" + tile.getxIndex() + ", " +
                tile.getyIndex() + ") of type " + tile.getID().name() +  " from ship : " + ship.getID());
    }

    public int size() {
        if(ship.getDraggedTile() != null){
            return existingTiles.size + 1;
        } else {
            return existingTiles.size;
        }
    }

    /**
     * Returns true if there are more tiles than edge tiles
     *
     * @return - Boolean representing if ship has non-edge tiles
     */
    public boolean hasCenterTiles() {
        return existingTiles.size > edgeTiles.size;
    }

    public Array<ShipTile> getExistingTiles() {
        return existingTiles;
    }


    public Array<ShipTile> getEdgeTiles() {
        return edgeTiles;
    }

    public void deleteSelf() {
        this.existingTiles.clear();
    }

    private void removeAllTilesFromGame(){

    }
}
