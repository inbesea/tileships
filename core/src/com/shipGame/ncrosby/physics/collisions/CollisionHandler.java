package com.shipGame.ncrosby.collisions;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Asteroid;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.ncrosby.managers.AsteroidManager;

import java.util.ArrayList;

public class CollisionHandler {

    AsteroidManager asteroidManager;
    World world;
    GameObject a;
    GameObject b;
    ArrayList<Collision> collisions = new ArrayList<>();

    /**
     * Collision Handler will need a lot of access to other managers to control what happens in the game.
     * @param asteroidManager
     */
    public CollisionHandler(AsteroidManager asteroidManager, World world){
        this.asteroidManager = asteroidManager;
        this.world = world;
    }

    public void handleCollision(Collision collision){
        a = collision.colliderA;
        b = collision.colliderB;
        if(numberOfCollisionsTiles(a,b) > 0){ //
            if(numberOfAsteroidsInCollision(a,b) > 0){
                asteroidTileBounce(a, b);
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
    private void asteroidTileBounce(GameObject a, GameObject b) {
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
            asteroid.physicsDelete();
            tile.getManager().addTile(asteroid.getX(), asteroid.getY(), ID.StandardTile);
        } else if (tile.isInvulnerable()){
            return; // Do nothing, the tile cannot be destroyed
        } else {
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

    /**
     * Reviews and removes dead physics bodies
     * */
    public void sweepForDeadBodies(Array<Body> bodies) {
        for (Body body : bodies) {
            if (body != null) {
                GameObject gameObject = (GameObject) body.getUserData();
                if (gameObject.isDead()) {
                    // We have not removed the object from the game.
                    // We might need to just bite the bullet and create a gameobject call for deleting the object from an agnostic point of view.
                    world.destroyBody(body);
                    body.setUserData(null);
                    body = null;
                }
            }
        }
    }
}
