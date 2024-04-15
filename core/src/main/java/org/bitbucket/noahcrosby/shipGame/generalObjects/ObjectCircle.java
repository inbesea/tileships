package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

import static com.badlogic.gdx.math.MathUtils.*;

public class ObjectCircle {
    // TODO convert this to a generic class that can handle object with positions.

    // Tiles the player holds - This should be reflected on the player's sprite
    public Array<ShipTile> heldShipTiles = new Array<>();
    private int heldTileLimit = 5;
    // Circle to render held tiles on.
    private Circle heldTileDisplay;
    private float tileCircleRotationSpeed = 0.01f;
    private float circleRotation = 0;
    private final int INIT_HELD_RADIUS = 32; // one Radius
    private Vector2 position;

    public ObjectCircle(Vector2 position){
        heldTileDisplay = new Circle(position.x, position.y, INIT_HELD_RADIUS);
        this.position = position;
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

    public Array<ShipTile> getHeldShipTiles() {
        return heldShipTiles;
    }

    public void setHeldShipTiles(Array<ShipTile> heldShipTiles) {
        this.heldShipTiles = heldShipTiles;
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
            throw new RuntimeException("Holding more tiles than is possible to hold! HeldTilesSize : " + heldShipTiles.size
                + ", limit : " + heldTileLimit);
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
            throw new RuntimeException("Stack of tiles has negative size!");
        }
    }

    /**
     * Renders a circle of tiles around the playerCharacter
     */
    private void renderCircleOfHeldTiles(){

        // Update radius and position of circle
        slowRadiusChange(2.5f);
        heldTileDisplay.setPosition(position);
        Texture tempTexture;
        rotateCircle();

        // Get radians based on fractionalized circle
        float radianFractions = (PI*2)/heldShipTiles.size;
        // Draw tiny tiles from player stack
        for (int i = 0; i < heldShipTiles.size; i++) {
            ShipTile t = heldShipTiles.get(i);
            tempTexture = null;
//            tempTexture = new Texture(Gdx.files.internal(t.getID().getTexture()));

            TileShipGame.batch.draw(tempTexture,
                (heldTileDisplay.x + 32) + (sin((i * (radianFractions)) + circleRotation) * heldTileDisplay.radius),
                (heldTileDisplay.y + 32) + (cos((i * (radianFractions)) + circleRotation) * heldTileDisplay.radius),
                5 ,5);

        }
    }
}
