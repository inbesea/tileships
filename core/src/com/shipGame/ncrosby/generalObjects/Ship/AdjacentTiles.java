package com.shipGame.ncrosby.generalObjects.Ship;

import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;

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
}
