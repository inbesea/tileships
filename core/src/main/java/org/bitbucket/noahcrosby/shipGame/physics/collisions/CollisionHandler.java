package org.bitbucket.noahcrosby.shipGame.physics.collisions;

import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

import java.util.ArrayList;

public class CollisionHandler {

    AsteroidManager asteroidManager;
    GameObject a;
    GameObject b;
    ArrayList<Collision> collisions = new ArrayList<>();

    /**
     * Collision Handler will need a lot of access to other managers to control what happens in the game.
     * @param asteroidManager
     */
    public CollisionHandler(AsteroidManager asteroidManager){
        this.asteroidManager = asteroidManager;
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
            tile.getManager().addTile(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getHeight() / 2, ID.StandardTile);
        } else if (tile.isInvulnerable()){
            return; // Do nothing, the tile cannot be destroyed
        } else {
            tile.destroySelf();
            // destroy the tile
            // This means the tile is not a core tile or a invulnerable tile.
            // We will then conclude it can be destroyed.
        }
    }

    public void handleCollisions() {
        Collision c;
        for (int i = 0 ; i < collisions.size() ; i++){
            c = collisions.get(i);
            handleCollision(c);
        }
        collisions.clear();
    }

    public void push(Collision newCollision) {
        collisions.add(newCollision);
    }

}
