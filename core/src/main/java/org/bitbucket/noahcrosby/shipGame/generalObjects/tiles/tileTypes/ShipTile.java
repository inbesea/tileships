package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.AdjacentTiles;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;
import org.bitbucket.noahcrosby.shipGame.physics.PhysicsObject;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

public abstract class ShipTile extends GameObject implements PhysicsObject {

    private final AdjacentTiles neighbors = new AdjacentTiles();
    private int xIndex;
    private int yIndex;

    public boolean isEdge;
    public final static float TILE_SIZE = 64f;
    private final Rectangle bounds;
    private final TileTypeData typeData; // Need for unique platonic form data

    private final ShipTilesManager manager;

    /**
     * ShipTiles are the basic unit of a Ship. They are boxes of data, and can be extended to do more.
     * The position needs to a multiple of TILE_SIZE or the index will be wrong.
     */
    public ShipTile(Vector2 position, ID id, TileTypeData typeData, ShipTilesManager manager) {
        // vector is not adjusted, so tiles can be independently created anywhere
        super(position, new Vector2(TILE_SIZE, TILE_SIZE), id);

        setPosition(position);

        this.typeData = typeData;

        this.manager = manager;

        bounds = new Rectangle(this.position.x, this.position.y, ShipTile.TILE_SIZE, ShipTile.TILE_SIZE);
        // Need to knit together the shiptile to adjacent tiles connectAdjacent();
    }

    public abstract boolean isInvulnerable();
    //These methods are for updating tiles when they're grabbing and placing a tile back.
    public abstract void replaced();
    public abstract void pickedUp();

    /**
     * TODO : This index determination needs to be updated to allow for ship displacement/rotation.
     * Determines an index value for the ShipTile based on the float passed in.
     *
     * @param position
     * @return
     */
    private int determineIndex(float position) {
        return (int) (position / TILE_SIZE);
    }

    /**
     * sets the position and index of the shiptile.
     * @param position
     */
    @Override
    public void setPosition(Vector2 position) {
        this.position = position;
        this.xIndex = determineIndex(position.x);
        this.yIndex = determineIndex(position.y);
    }

    public void tick() {
        // TODO Auto-generated method stub
    }

    /**
     * Returns the indices of the ShipTile as a string
     *
     * @return - String of ShipTile location
     */
    public String getPositionAsString() {
        return "(" + xIndex + ", " + yIndex + ")";
    }

    /**
     * Gets specific shiptile Abbreviation from unique tile type asserted during construction
     */
    public String getAbbreviation() {
        return typeData.getAbbreviation();
    }

    ;

    /**
     * Renders text specifying the shipTile's indices
     *
     * @param game
     */
    public void render(TileShipGame game) {
        // Debug position declaration
        if (AppPreferences.getAppPreferences().getIsDebug()) {
            game.font.draw(
                    TileShipGame.batch,
                    getPositionAsString(),
                    getX() + 2,
                    getY() + (size.y / 4));
        }
    }

    /**
     * Returns the collision box for the shipTile
     *
     * @return - Rectangle object representing the collision box
     */
    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Method to handle collisions
     *
     * @param gameObject
     */
    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    /**
     * Returns neighbors of ShipTile
     *
     * @return - AdjacentTile object holding references to neighbors
     */
    public AdjacentTiles getNeighbors() {
        return neighbors;
    }

    public boolean isEdge() {
        return isEdge;
    }

    public void setIsEdge(boolean isEdge) {
        this.isEdge = isEdge;
    }

    /**
     * Method that handles setting the neighbors of a newly placed tile.
     * Keeps the edge calculations down in the tiles instead of on the ship level to keep the logic cleaner on the ship level
     * <p>
     * Ship still determines the context by passing the adjacent tiles
     *
     * @param up
     * @param right
     * @param down
     * @param left
     */
    public int setNeighbors(ShipTile up, ShipTile right, ShipTile down, ShipTile left) {

        // Set tile in neighbors - Stitch newTile to neighbor tiles
        if (up != null) {
            up.setDown(this); // Set up neighbor to this (down)
            up.setIsEdge(up.calculateIsEdge()); // Since new tile has been placed, check and see if tile is edge.
        }
        if (right != null) {
            right.setLeft(this);
            right.setIsEdge(right.calculateIsEdge());
        }
        if (down != null) {
            down.setUp(this);
            down.setIsEdge(down.calculateIsEdge());
        }
        if (left != null) {
            left.setRight(this);
            left.setIsEdge(left.calculateIsEdge());
        }

        // set this's each direction with appropriate relative tile.
        this.neighbors.setUp(up);
        this.neighbors.setRight(right);
        this.neighbors.setDown(down);
        this.neighbors.setLeft(left);

        setIsEdge(calculateIsEdge());// Check if self is edge now.

        return neighbors.numberOfNeighbors();
    }


    public void setUp(ShipTile up) {
        neighbors.setUp(up);
    }

    public void setRight(ShipTile right) {
        neighbors.setRight(right);
    }

    public void setDown(ShipTile down) {
        neighbors.setDown(down);
    }

    public void setLeft(ShipTile left) {
        neighbors.setLeft(left);
    }

    /**
     * Checks if there are 4 neighbors.
     * If there are return false, else return true
     *
     * @return - Returns  false if all sides have tiles, else returns false.
     */
    private boolean calculateIsEdge() {
        return numberOfNeighbors() != 4;
    }

    /**
     * Returns number of neighbors based on method in AdjacentTiles
     *
     * @return
     */
    public int numberOfNeighbors() {
        return neighbors.numberOfNeighbors();
    }

    /**
     * Returns a list of Vectors corresponding to the null sides.
     * Should give center of the side being referenced
     *
     * @return - array of vectors corresponding to null sides
     */
    public Array<Vector2> emptySideVectors() {
        Array<Vector2> results = new Array<>();

        // Get each
        if (up() == null) results.add(new Vector2(getX() + (ShipTile.TILE_SIZE / 2.0f), getY() + ShipTile.TILE_SIZE * 1.5f));
        if (right() == null) results.add(new Vector2(getX() + ShipTile.TILE_SIZE * 1.5f, getY() + (ShipTile.TILE_SIZE / 2.0f)));
        if (down() == null) results.add(new Vector2(getX() + (ShipTile.TILE_SIZE / 2.0f), getY() - (ShipTile.TILE_SIZE / 2.0f)));
        if (left() == null) results.add(new Vector2(getX() - (ShipTile.TILE_SIZE / 2.0f), getY() + (ShipTile.TILE_SIZE / 2.0f)));

        return results;
    }

    /**
     * Delegate method to get the directly up neighbor
     *
     * @return
     */
    public ShipTile up() {
        return neighbors.getUp();
    }

    public ShipTile right() {
        return neighbors.getRight();
    }

    public ShipTile down() {
        return neighbors.getDown();
    }

    public ShipTile left() {
        return neighbors.getLeft();
    }

    /**
     * Checks if possibleNeighbor is a neighbor.
     * Returns true if is neighbor, else returns false
     *
     * @param possibleNeighbor - tile to check for adjacency
     * @return -  Returns true if is neighbor, else returns false
     */
    public boolean isNeighbor(ShipTile possibleNeighbor) {
        return getNeighbors().isNeighbor(possibleNeighbor);
    }

    /**
     * Returns an int representing the relationship between the passed tile and this tile.
     *
     * @param tile
     * @return An int showing where tile is in relationship to this tile.
     * * 0 = UP
     * * 1 = RIGHT
     * * 2 = DOWN
     * * 3 = LEFT
     */
    public int getAdjacency(ShipTile tile) {
        return neighbors.isWhichNeighbor(tile);
    }

    public ShipTilesManager getManager() {
        return manager;
    }

    /**
     * Call to remove this tile from it's manager.
     */
    public boolean destroySelf() {
        boolean tileBelongsToItsManager = manager.returnTile(this.getPosition()) != null;

        if (tileBelongsToItsManager) {
            manager.removeTileFromShip(this);
            return true;
        } else {
            System.out.println("ERROR : ShipTile " + this.getID() + " at " + this.getPositionAsString() + " does not belong to its manager.");
            return false;
        }
    }

    /**
     * Returns center of tile based on this.size
     *
     * @return
     */
    public Vector2 getCenter() {
        return new Vector2(position.x + this.size.x / 2f, position.y + this.size.y / 2f);
    }

    public Body setPhysics(World world) {
        // Adjust init position to center the box on the tile
        BodyDef bodyDef = generalUtil.newStaticBodyDef(
                (position.x / ShipTile.TILE_SIZE) + ShipTile.TILE_SIZE / 2f, // Position and size scaled up to game size.
                (position.y / ShipTile.TILE_SIZE) + ShipTile.TILE_SIZE / 2f
        );
        Body body = world.createBody(bodyDef);

        PolygonShape tileShape = new PolygonShape();

        // Specific to tiles. This is too specific.
        tileShape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = tileShape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        tileShape.dispose();

        return body;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition){
        this.bounds.setPosition(boundsPosition);
    }

    /**
     * Returns a string representation of the tile's position.
     * Will be overridden by specific tile types.
     * @return - String representation of tile
     */
    public String getTileInfoString() {
        return this.getAbbreviation() + " at " + getPositionAsString();
    }
}
