package org.bitbucket.noahcrosby.shipGame.physics.collisions;

import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;

/**
 * Handles collisions in Classic mode
 */
public class ClassicCollisionHandler extends CollisionHandler {

    AsteroidManager asteroidManager;
    GameObject a;
    GameObject b;

    ShipTilesManager tilesManager;
    /**
     * Collision Handler will need a lot of access to other managers to control what happens in the game.
     * @param asteroidManager
     */
    public ClassicCollisionHandler(AsteroidManager asteroidManager, ShipTilesManager tilesManager){
        this.asteroidManager = asteroidManager;
        this.tilesManager = tilesManager;
    }


    public void handleCollision(Collision collision){
        a = collision.colliderA;
        b = collision.colliderB;
        if(numberOfCollisionsTiles(a,b) > 0){ //
            if(numberOfAsteroidsInCollision(a,b) > 0){
                asteroidTileCollision(a, b);
            }
        } else { // there are no tiles

        }

        // Finish handling Collision. Would like to know if there's an assignment problem  here.
        a = null;
        b = null;
    }

    /**
     * Checks if a or b has an asteroid
     * @param a
     * @param b
     * @return
     */
    private int numberOfAsteroidsInCollision(GameObject a, GameObject b) {
        int i = 0;
        if(a.getID() == ID.Asteroid) i++;
        if(b.getID() == ID.Asteroid) i++;
        return i;
    }

    private int numberOfCollisionsTiles(GameObject a, GameObject b) {
        int i = 0;
        if(a.getID().isTileType()) i++;
        if(b.getID().isTileType()) i++;
        return i;
    }

    /**
     * Handles a tile/asteroid collision
     * @param a
     * @param b
     */
    private void asteroidTileCollision(GameObject a, GameObject b) {

        Asteroid asteroid;
        ShipTile tile;

        // Assign based on what the classes are
        if(a.getID() == ID.Asteroid){
            asteroid = (Asteroid) a;
            tile = (ShipTile) b;
        } else {
            asteroid = (Asteroid) b;
            tile = (ShipTile) a;
        }

        // Check for core first
        if(tile.getID() == ID.CoreTile){
            // Turn the asteroid into a standardTile.
            // remove the asteroid and add a tile.
            System.out.println("CoreTile/Asteroid creation event. ~~~ Creating new tile "+ asteroid.getPosition().toString() +" and marking Asteroid for deletion!");
            asteroid.physicsDelete();

            tilesManager.addTile(asteroid.getX() + asteroid.getWidth() / 2,
                asteroid.getY() + asteroid.getHeight() / 2,
                asteroid.getTileType());

        } else if (tile.isInvulnerable()){
            return; // Do nothing, the tile cannot be destroyed
        } else {
            tile.setIsDeadTrue();
            // destroy the tile
            // This means the tile is not a core tile or an invulnerable tile.
            // We will then conclude it can be destroyed.
        }
    }
}
