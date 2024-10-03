package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.Components.CollectorComponent;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.*;

public class Player extends GameObject {
    Ship playerShip; // Ship "owned" by player
    private TileShipGame game; // May need to remove this at some point. Only used for drawing within player, bad coding practice
    private Rectangle bounds;

    private float playerSpeed = ShipTile.TILE_SIZE * 1.5f;

    public Player(Vector2 position, Vector2 size, ID id, TileShipGame game) {
        super(position, size, id);

        this.game = game;
        this.playerShip =  game.getPlayerShip();

        add(new CollectorComponent(150));
    }

    /**
     * Returns a rectangle for updating for physics simulation
     * @return
     */
    public Rectangle getBounds() {
        Rectangle r = new Rectangle((int) position.x, (int) position.y, ShipTile.TILE_SIZE *.33f, ShipTile.TILE_SIZE * 0.5f);
        //r.intersects
        return r;
    }

    @Override
    public void setPosition(Vector2 position){
        position.set(position);
        bounds.setPosition(position);
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition) {
        bounds.setPosition(boundsPosition);
    }

    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Texture getTexture() {
        return Resources.RobotV2Texture;
    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    /**
     * Player should not be removed from game.
     * @return
     */
    @Override
    public boolean deleteFromGame() {
        return false;
    }

    /**
     * Render more complex features of the player
     * Assumes is drawn in a batch loop
     *
     * @param game - reference to the game context
     */
    public void render(TileShipGame game) {
        TileShipGame.batch.draw(getTexture(), getPosition().x, getPosition().y, getSize().x, getSize().y);
    }

    /**
     * This may get reworked so we can get the ship currently used by the player.
     * <p>
     * It doesn't work currently
     *
     * @return
     */
    public Ship getPlayerShip() {
		return playerShip;
    }

    /**
     * Player checks if a position is out of bounds of their ship.
     * @param position - Vector 2 representing a position
     */
    public boolean positionIsOffShip(Vector2 position) {
        return playerShip.isPositionOffShip(position);
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }
    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }
}
