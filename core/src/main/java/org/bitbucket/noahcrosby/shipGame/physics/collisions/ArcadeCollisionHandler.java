package org.bitbucket.noahcrosby.shipGame.physics.collisions;

import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.ColorAsteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ColorTile;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.ArcadeMode.ArcadeModeScreen;

public class ArcadeCollisionHandler extends  CollisionHandler {

    ArcadeModeScreen arcadeModeScreen;
    AsteroidManager asteroidManager;
    ShipTilesManager tilesManager;

    public ArcadeCollisionHandler(ArcadeModeScreen arcadeModeScreen, ShipTilesManager tilesManager) {
        this.arcadeModeScreen = arcadeModeScreen;
        this.asteroidManager = arcadeModeScreen.getAsteroidManager();
        this.tilesManager = tilesManager;
    }

    @Override
    public void handleCollision(Collision collision) {
        if(collision.colliderA.getID() == ID.Asteroid || collision.colliderB.getID() == ID.Asteroid){
            if(collision.colliderA.getID() == ID.ColorTile || collision.colliderB.getID() == ID.ColorTile){
                ColorAsteroid asteroid = getAsteroid(collision);
                asteroid.physicsDelete();
                ColorTile tile = getColorTile(collision);
                ColorTile newTile = (ColorTile) tilesManager.addTile(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getHeight() / 2, ID.ColorTile);
                newTile.setColor(asteroid.getColor());
            }
        }
    }

    private ColorAsteroid getAsteroid(Collision collision) {
        if(collision.colliderA.getID() == ID.Asteroid){
            return (ColorAsteroid) collision.colliderA;
        } else {
            return (ColorAsteroid) collision.colliderB;
        }
    }

    private ColorTile getColorTile(Collision collision) {
        if(collision.colliderA.getID() == ID.ColorTile){
            return (ColorTile) collision.colliderA;
        } else {
            return (ColorTile) collision.colliderB;
        }
    }
}
