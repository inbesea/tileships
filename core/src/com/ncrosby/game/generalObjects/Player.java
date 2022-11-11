package com.ncrosby.game.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ncrosby.game.ID;
import com.ncrosby.game.generalObjects.GameObject;
import com.ncrosby.game.generalObjects.Ship;
import com.ncrosby.game.tileShipGame;
import com.ncrosby.game.tiles.ShipTile;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.*;

public class Player extends GameObject {
    Ship playerShip;
    Random r = new Random();
    private tileShipGame game;
    private OrthographicCamera cam;
    private int index[] = {0, 0};
    private int lookAhead = 155;
    public boolean godMode = false;

    // Tiles the player holds - This should be reflected on the player's sprite
    public Array<ShipTile> heldShipTiles = new Array<>();
    private int heldTileLimit;
    // Circle to render held tiles on.
    private Circle heldTileDisplay;
    private float tileCircleRotationSpeed = 0.01f;
    private float circleRotation = 0;
    private final int INIT_HELD_RADIUS = 32; // one Radius

    public Player(Vector2 position, ID id, OrthographicCamera cam, tileShipGame game) {
        super(position, new Vector2(64, 64), id);

        heldTileDisplay = new Circle(position.x, position.y, INIT_HELD_RADIUS);
        this.cam = cam;
        this.game = game;
        this.heldTileLimit = 5;
        this.playerShip =  game.getPlayerShip();
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle((int) playerPosition.x, (int) playerPosition.y, 32, 32);
        //r.intersects
        return r;

    }

    public void tick() {
    }

    /**
     * Render more complex features of the player
     *
     * @param game - reference to the game context
     */
    public void render(tileShipGame game) {
        renderCircleOfHeldTiles(); // Moved this logic out to allow render to be more flexible
    }

    /**
     * Changes the radius of the held tile circle slowly over time.
     *
     * @param ROC - Rate of change to the radius
     */
    private void slowRadiusChange(float ROC) {

        // targetRadius - current

        float targetRadius = (INIT_HELD_RADIUS *  (((8.0f * heldShipTiles.size)) /
                (heldShipTiles.size + 5.0f)));  // Target based on size of held tiles

        float radius = heldTileDisplay.radius;

        heldTileDisplay.radius += (
                // need to fiddle with this exp. so the value stablizes and goes to 0.
                (targetRadius - (radius)) * // display radius changes based on the size of the hand of tiles.
                        ROC * Gdx.graphics.getDeltaTime()
        );
    }

    /**
     * Increases the circleRotation float used to display held tiles
     */
    private void rotateCircle(){
        circleRotation += tileCircleRotationSpeed;
        if(circleRotation > PI2) circleRotation = 0;
    }

    public void mouseover(int[] index) {
        this.index = index;
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

    public Array<ShipTile> getHeldShipTiles() {
        return heldShipTiles;
    }

    public void setHeldShipTiles(Array<ShipTile> heldShipTiles) {
        this.heldShipTiles = heldShipTiles;
    }

    /**
     * Switches god mode to opposite state when called.
     */
    public void toggleGodMode() {
        System.out.println("godMode is set to : " + godMode);
        if (this.godMode == false) {
            this.godMode = true;
        } else this.godMode = false;
    }

    /**
     * Adds a shipTile to the players' stack
     *
     * @param st
     * @return
     */
    public boolean pickupTile(ShipTile st) {
        if (heldShipTiles.size == heldTileLimit) {
            System.out.println("Held tiles at limit!");
            return false;
        } else if (heldShipTiles.size > heldTileLimit) {
            throw new RuntimeException("Holding more tiles than is possible to hold : " +
                    Arrays.toString(Thread.currentThread().getStackTrace()));
        } else {
            heldShipTiles.add(st);
            return true;
        }
    }

    /**
     * Method that returns the currently held tile and removes it from the player.
     *
     * @return - Returns tile from stack if holding tile, else return NULL
     */
    public ShipTile popTile() {
        if (heldShipTiles.size == 0) {
            System.out.println("Holding (" + heldShipTiles.size + ") tiles!");
            return null;
        } else if (heldShipTiles.size > 0) {
            System.out.println("Holding (" + heldShipTiles.size + ") tiles!\n" +
                    " Returning tile with ID : " + heldShipTiles.peek().getID());
            return heldShipTiles.pop();
        } else {
            throw new RuntimeException("Error with placeTile" + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
    }

    /**
     * Renders a circle of tiles around the playerCharacter
     */
    private void renderCircleOfHeldTiles(){

        // Update radius and position of circle
        slowRadiusChange(2.5f);
        heldTileDisplay.setPosition(playerPosition);
        Texture tempTexture;
        rotateCircle();

        // Get radians based on fractionalized circle
        float radianFractions = (PI*2)/heldShipTiles.size;
        // Draw tiny tiles from player stack
        for (int i = 0; i < heldShipTiles.size; i++) {
            ShipTile t = heldShipTiles.get(i);
            tempTexture = new Texture(Gdx.files.internal(t.getID().getTexture()));

            game.batch.draw(tempTexture,
                    (heldTileDisplay.x + 32) + (sin((i * (radianFractions)) + circleRotation) * heldTileDisplay.radius),
                    (heldTileDisplay.y + 32) + (cos((i * (radianFractions)) + circleRotation) * heldTileDisplay.radius),
                    5 ,5);

        }
    }
}
