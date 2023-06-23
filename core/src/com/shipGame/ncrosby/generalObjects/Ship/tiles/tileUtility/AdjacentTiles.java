package com.shipGame.ncrosby.generalObjects.Ship.tiles.tileUtility;

import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;

/**
 * Manages adjacent ShipTiles to a shiptile
 * Is a part of the ShipTile class, not the Ship.
 */
public class AdjacentTiles {
    // Number associated with adjacency.
    // No magic numbers!
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int INVALID = -1;
    private ShipTile Up;
    private ShipTile Left;
    private ShipTile Right;
    private ShipTile Down;

    public AdjacentTiles (){
    }

    public ShipTile getUp() {
        return Up;
    }

    public void setUp(ShipTile up) {
        Up = up;
    }

    public ShipTile getLeft() {
        return Left;
    }

    public void setLeft(ShipTile left) {
        Left = left;
    }

    public ShipTile getRight() {
        return Right;
    }

    public void setRight(ShipTile right) {
        Right = right;
    }

    public ShipTile getDown() {
        return Down;
    }

    public void setDown(ShipTile down) {
        Down = down;
    }

    /**
     * Returns number of non-null neighbors
     * @return - number of neighbors
     */
    public int numberOfNeighbors(){
        int result = 0;
        if(Up != null)result++;
        if(Down != null)result++;
        if(Right != null)result++;
        if(Left != null)result++;
        return result;
    }

    /**
     * Checks if a tile is present in neighbors.
     * @param possibleNeighbor
     * @return
     */
    public boolean isNeighbor(ShipTile possibleNeighbor) {
        if(Up == possibleNeighbor ||
        Right == possibleNeighbor ||
        Left == possibleNeighbor ||
        Down == possibleNeighbor){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns Int representing neighbor status
     * @param possibleNeighbor
     * @return
     */
    public int isWhichNeighbor(ShipTile possibleNeighbor) {
        if(Up == possibleNeighbor)return UP;
        else if (Right == possibleNeighbor)return RIGHT;
        else if(Left == possibleNeighbor)return LEFT;
        else if(Down == possibleNeighbor)return DOWN;
        else return INVALID;
    }

    /**
     * Returns all non-null neighbor values.
     * @return
     */
    public Array<ShipTile> allNeighbors(){
        Array<ShipTile> results = new Array<ShipTile>();
        if(Up != null) results.add(Up);
        if(Down != null)results.add(Down);
        if(Right != null)results.add(Right);
        if(Left != null)results.add(Left);
        return results;
    }
}
