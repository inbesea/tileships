package com.shipGame.generalObjects.Ship;

import com.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.javapoet.Resources;
import com.shipGame.ID;
import com.shipGame.TileShipGame;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.player.TileHoverIndicator;

public class Ship extends GameObject {

    /**
     * Ship is a class of modules meant to simulate the core mechanics of a ship.
     * This includes adding/removing tiles from the ship, keeping track of the ship's grid, and producing new tiles from the existing tiles.
     * <p>
     * Ship has many methods to manage tiles internally.
     * Edge management happens on I/O of tiles so we have a reference to the tile being updated.
     * Ships manage shipTiles. Tiles don't know about their relationship with other tiles, the ship manages that.
     * <p>
     * SOLID
     * S - Represents the ship and it's underlying systems.
     * O -
     */
    private ShipTile draggedTile;
    public int destroyedTileCount = 0;
    private final CollectionManager collectionManager;
    private final TileCondenser tileCondenser;
    private final ShipTilesManager shipTilesManager;
    public boolean mute;

    /**
     * Ship keeps track of the tiles of the ship and has methods for
     * managing removing and adding tiles.
     */
    public Ship(Vector2 position) {
        super(position, new Vector2(0, 0), ID.Ship);

        shipTilesManager = new ShipTilesManager(this);
        collectionManager = new CollectionManager();

        UnlockTracker unlockTracker = new UnlockTracker();
        tileCondenser = new TileCondenser(unlockTracker);

        // Give new ship default tiles.
        /* TODO : Create more flexible init tile placements. Possibly a setInitTiles(<ShipTiles> st)
         *   that creates tiles based on a list of tile instances */
        mute = false;
    }

    /**
     * Loops the list of existing tiles and renders them
     *  TODO : Scale tile locations by the ship position to allow ship movement.
     */
    public void render(TileShipGame game) {
        Array<ShipTile> existing = shipTilesManager.getExistingTiles();

        // Draw tiles
        for (int i = 0; i < existing.size; i++) {
            ShipTile tempTile = existing.get(i);
            TileShipGame.batch.draw(tempTile.getTexture(),
                    tempTile.getX(), tempTile.getY(),
                    tempTile.getSize().x, tempTile.getSize().y);
            tempTile.render(game);
        }
        // Draw dragged tile
        if (draggedTile != null) {
            TileShipGame.batch.draw(draggedTile.getTexture(),
                    draggedTile.getX(), draggedTile.getY(), draggedTile.getSize().x, draggedTile.getSize().y);
        }
        // Draw collected tile overlay
        if (collectionManager.isCollectingTiles()) {
            Array<ShipTile> tiles = collectionManager.getTileArray();
            for (int i = 0; tiles.size > i; i++) {
                ShipTile tile = tiles.get(i);
                TileShipGame.batch.draw(Resources.ToBeCollapsedTexture,
                        tile.getX(), tile.getY(),
                        ShipTile.TILE_SIZE, ShipTile.TILE_SIZE);
            }
        }
    }

    /**
     * Sets the initial tiles.
     * TODO : This can be another class that takes an argument to determine what ship will be initialized. MOVE IT OUT
     */
    public void initialize() {
        addTileToShip(position.x, position.y, ID.CoreTile);
        addTileToShip(position.x + ShipTile.TILE_SIZE, position.y, ID.StandardTile);
        addTileToShip(position.x + ShipTile.TILE_SIZE, position.y + ShipTile.TILE_SIZE, ID.StandardTile);
        addTileToShip(position.x, position.y + ShipTile.TILE_SIZE, ID.StandardTile);
        addTileToShip(position.x + ShipTile.TILE_SIZE * 2, position.y + ShipTile.TILE_SIZE, ID.StandardTile);
    }

    /**
     * This Method adds a tile to the ship with a reference to the tile's x and y
     * positions , the color, ID, and camera object for updating
     * the render location
     *
     * @param x  - The unaligned x coordinate this tile will be added to
     * @param y  - The unaligned y coordinate this tile will be added to
     * @param id - The ID of the tile
     * @return shipTile - this will be null if the space added to is not occupied, else will return the tile blocking
     */
    public ShipTile addTileToShip(float x, float y, ID id) {
        ShipTile tile = shipTilesManager.addTile(x, y, id);
        return tile;
    }

    /**
     * Rounds an x/y position to align with the ships' tile grid
     * <p>
     * TODO : Should update ship so the grid is alligned with the shipposition, allowing us to move the ship and keep grid.
     *
     * @param x - Coordinate to align with ship's grid
     * @param y - Coordinate to align with ship's grid
     * @return - Vector2 that is aligned with the ship's grid.
     */
    public Vector2 getGridAlignedPosition(float x, float y) {
        return shipTilesManager.getGridAlignedPosition(x, y);
    }

    /**
     * Removes a tile by reference to the tile instance
     * Should only be used when a tile instance is found. Tile manager handles flushing the adjacency relationships between tiles.
     *
     * @param tile - Tile to remove from ship
     */
    public void removeTileFromShip(ShipTile tile) {
        shipTilesManager.removeTileFromShip(tile);
    }

    /**
     * Uses removeTileFromShip to remove all instances of an array of tiles in the ship
     *
     * @param tiles - Tiles to remove from ship
     */
    public void removeTilesFromShip(Array<ShipTile> tiles) {
        ShipTile tile;
        int size = tiles.size; // Array shrinks as time goes on, must use var
        for (int i = 0; i < size; i++) {
            tile = tiles.get(0); // Use first array as values are removed
            if (tile.getID() == ID.CoreTile) continue; // Skip Core tile, don't remove core tiles
            removeTileFromShip(tile);
        }
    }

    /**
     * Attempts to return a tile using a Vector2 position
     *
     * @param position
     * @return
     */
    public ShipTile returnTile(Vector2 position) {
        return shipTilesManager.returnTile(position);
    }

    /**
     * Returns reference to a tile
     *
     * @param x - horizontal position of tile
     * @param y - vertical position of tile
     * @return - tile found, else returns null
     */
    public ShipTile returnTile(float x, float y) {
        return shipTilesManager.returnTile(new Vector2(x, y));
    }

    /**
     * Returns number of tiles, taking dragged tile into account.
     *
     * @return
     */
    public int shipSize() {
        return shipTilesManager.size();
    }

    public int sizeOfShipEdge() {
        return shipTilesManager.getEdgeTiles().size;
    }

    /**
     * Returns true if there are more tiles than edge tiles
     *
     * @return - Boolean representing if ship has non-edge tiles
     */
    public boolean hasCenterTiles() {
        return shipTilesManager.hasCenterTiles();
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
    }

    @Override
    public Rectangle getBounds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition) {
        return;
    }

    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        shipTilesManager.deleteSelf();
        return true;
    }

    public void setDraggedTile(ShipTile draggedTile) {
        this.draggedTile = draggedTile;
    }

    /**
     * Checks both bottom corners of a gameObject using size and position,
     * will also check intermediary points if size > TILESIZE
     * <p>
     * If a single point is off the ship this returns true.
     *
     * @return - true if any points checked are off-ship, else returns false
     */
    public boolean isGameObjectOffShip(GameObject gameObject) {
        float objectSize = gameObject.getWidth();
        Vector2 leftCorner = gameObject.getPosition();
        Vector2 rightCorner = new Vector2(leftCorner.x + objectSize, leftCorner.y); // Same as left with adjusted x

        return isHorizontalSpanOffShip(leftCorner, rightCorner);
    }

    /**
     * Checks a span defined by two Vector2 points within a ship.
     * will also check intermediary points if size > TILESIZE
     * If a single point is off the ship this returns true.
     *
     * @param leftPoint
     * @param rightPoint
     * @return - true if any points checked are off-ship, else returns false
     */
    public boolean isHorizontalSpanOffShip(Vector2 leftPoint, Vector2 rightPoint) {
        float objectSize = leftPoint.x - rightPoint.y; // get point span TODO : rename to spanSize

        // Check left and right corner
        if (isPositionOffShip(leftPoint) || isPositionOffShip(rightPoint)) {
            return true;
        }

        // Check if additional points need to be checked.
        if (objectSize > ShipTile.TILE_SIZE) { // objectSize > TILESIZE ( Check for size is bigger than tilesize )
            // Want to find number of internal points.
            // size / Shiptile.TILESIZE
            // round up and use to find x
            int objectInternalPoints = (int) Math.ceil(objectSize / ShipTile.TILE_SIZE);
            for (int i = 1; i > objectInternalPoints - 1; i++) { // dont need to check i=objectInternalPoints since this = rightCorner
                if (isPositionOffShip(new Vector2(leftPoint.x + (i * ShipTile.TILE_SIZE), rightPoint.y))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * Returns true if a position does not match a tile on the ship.
     * Any checks should ping this method instead of handling the logic of returned value checking.
     * Ships should be responsible for checking their own tiles.
     *
     * @param position - Vector2 position to check
     * @return - Boolean true if no tile found - else return false
     */
    public boolean isPositionOffShip(Vector2 position) {
        ShipTile tile = shipTilesManager.returnTile(position);
        return tile == null;
    }

    public Array<ShipTile> getExistingTiles() {
        return shipTilesManager.getExistingTiles();
    }

    /**
     * Method to count destroyed tiles
     */
    public void increaseDestroyedTile(int destroyedTiles) {
        destroyedTileCount += destroyedTiles;
    }

    public boolean isCollectingTiles() {
        return collectionManager.isCollectingTiles();
    }

    /**
     * Kick off collecting tiles in the stack manager
     */
    public void startCollapseCollect() {
        collectionManager.startCollect();
    }

    /**
     * Clears and returns a stack of tiles collected during a collapse action
     */
    public Array<ShipTile> finishCollapseCollect() {
        return collectionManager.endCollect();
    }

    /**
     * Standard getter
     *
     * @return
     */
    public Array<ShipTile> getCollapseCollect() {
        return collectionManager.getTileArray();
    }

    /**
     * Adds an element to the CollapseCollection and returns a bool representing success.
     *
     * @param tile - Tile to add to manager stack
     * @return boolean signifying success
     */
    public boolean updateCollect(ShipTile tile) {
        boolean result = collectionManager.addTile(tile);
        return result;
    }

    /**
     * Adds an element to the CollapseCollection using a positional reference
     *
     * @param vector3 -  a position in space.
     * @return - boolean signifying success
     */
    public boolean updateCollect(Vector3 vector3) {
        if (collectionManager.isCollectingTiles()) {
            ShipTile tile = shipTilesManager.returnTile(new Vector2(vector3.x, vector3.y));
            if (tile != null) {
                Sound collectTileSound;
//				collectTileSound = Gdx.audio.newSound( Gdx.files.internal("Sound Effects/zapsplat_science_fiction_robot_tiny_fast_mechanical_motorised_whirr_movement_003_72910.mp3"));
//				collectTileSound.play();
                return collectionManager.addTile(tile);
            } else {
                System.out.println("updateCollect getting null tile reference");
                return false;
            }
        } else {
            throw new RuntimeException("CollectTiles is false : " + collectionManager.isCollectingTiles());
        }
    }

    /**
     * Returns reference to the hovering indicator reference
     *
     * @return - Hover indication instance
     */
    public TileHoverIndicator getTileHoverIndicator() {
        return collectionManager.getTileHoverIndicator();
    }

    /**
     * Set hover indicator position
     *
     * @param x - x-position
     * @param y - y-position
     */
    public void setHoverIndicator(float x, float y) {
        collectionManager.setHoverIndicator(x, y);
    }

    /**
     * Checks if the hovers should draw
     *
     * @return - true if is drawing, false if not drawing
     */
    public boolean isHoverDrawing() {
        return collectionManager.isHoverDrawing();
    }

    /**
     * Sets if the hover position should draw.
     *
     * @param shouldDraw - new boolean value for drawing the hover layover
     */
    public void setHoverShouldDraw(boolean shouldDraw) {
        collectionManager.setDrawHover(shouldDraw);
    }

    /**
     * Check if manager has collected the limit of tiles.
     *
     * @return false if more tiles can be collected, else returns false
     */
    public boolean collapseStackIsFull() {
        return collectionManager.isFull();
    }


    public void cancelCurrentCollectArray() {
        collectionManager.cancelCurrentCollectArray();
    }

    /**
     * Attempts to build a new tile.
     * If one can be made it adds it to the ship.
     *
     * @param collectedTileArray - list of tiles to produce from
     * @return - ShipTile resulting from build action.
     */
    public ShipTile buildNewTile(Array<ShipTile> collectedTileArray) {
        ID newTileID = tileCondenser.determineNewTileID(collectedTileArray);

        if (newTileID == null) {
            collectionManager.cancelCurrentCollectArray(); // Reset the stack due to failed production
            return null;
        } else { // if Tile produced then swap the tiles used out of existence and return the new one.
            Resources.sfxBuildTileSound.play(AppPreferences.getAppPreferences().getSoundVolume());
            Vector2 vector2 = collectedTileArray.get(collectedTileArray.size - 1).getPosition(); // Use last tile in line as new tile position
            vector2.y += ShipTile.TILE_SIZE / 2f;
            vector2.x += ShipTile.TILE_SIZE / 2f;
            removeTilesFromShip(collectedTileArray);
            ShipTile result = addTileToShip(vector2.x, vector2.y, newTileID);
            System.out.println("Building new tile " + result.getID());
            return result;
        }
    }

    public ShipTilesManager getTileManager() {
        return shipTilesManager;
    }

    public Array<ShipTile> getEdgeTiles() {
        return shipTilesManager.getEdgeTiles();
    }

    public ShipTile getDraggedTile() {
        return draggedTile;
    }

}
