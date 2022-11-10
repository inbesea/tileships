package com.ncrosby.game.tiles;

/**
 * Manages adjacent ShipTiles to a shiptile
 */
public class AdjacentTiles {
    private ShipTile Up;
    private ShipTile Left;
    private ShipTile Right;
    private ShipTile Down;

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
}
