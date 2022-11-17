package com.shipGame.ncrosby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shipGame.ncrosby.generalObjects.Player;
import com.shipGame.ncrosby.generalObjects.Ship;
import com.shipGame.ncrosby.tiles.ShipTile;

public class PlayerInput {

    /**
     * This handles the player's default action when clicking on the screen.
     * currently it lacks handling clicking on UI elements, but we need to make this work simply for now.
     * Instead of clicking on UI, clicking can only move the robot.
     *
     * Ultimately this will need to move blocks around, not move the robot.
     *
     * @return - ShipTile of affected tile if any
     */
    public static ShipTile clickPlayerShipTiles(OrthographicCamera camera, Ship playerShip, Player player){
        // Gets the click location
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos); // Moves clicked point to camera location

        // Remove a tile or place a tile?
        Vector2 index = new Vector2();
        ShipTile tile = playerShip.returnTile(touchPos.x, touchPos.y);

        // Extract if checks
        boolean noFoundTileButHeldTile = (tile == null && player.heldShipTiles.size > 0);
        boolean noFoundTile = (tile == null);
        boolean holdingTiles = player.heldShipTiles.size > 0;

        if(noFoundTile){
            if(holdingTiles){
                // Remove tile from held tiles
                ShipTile placedTile = player.popTile();
                // Add tile to location giving tile ID
                playerShip.addTileByCoord(touchPos.x, touchPos.y, placedTile.getID());
                return placedTile;
            } else { // No tiles to place
                System.out.println("Not holding any tiles to place!");
                return null;
            }
        } else { // Found tile
            if(tile.getID() == ID.CoreTile){
                return null;
            } else if (playerShip.returnTile(player.getX(), player.getY()) == tile) { // Can't grab tile under player
                return null;
            } else {
                if(player.pickupTile(tile)){ // If can pickup tile
//                    playerShip.removeTile(tile); // Remove from ship and
                    // Return the tile grabbed
                    return tile;
                } else {
                    return null; // Else, nothing is changed, return null
                }
            }
        }
    }

    /**
     * Handle key presses from GameScreen
     * This will update the player's position based on keys pressed.
     *
     * TODO : Migrate this to SimpleTouch.java
     *
     * @param player - player Rectangle to update
     */
    public static void handleKeyPressed(Player player, OrthographicCamera camera){

        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);
        Ship ship = player.getPlayerShip();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            // Get vector with position of moving up position
            Vector2 possibleUpPosition = new Vector2(
                    (playerPos.x),
                    playerPos.y  + (200 * Gdx.graphics.getDeltaTime()));
            Vector2 possibleRightCorner = new Vector2(possibleUpPosition.x + player.getWidth(),possibleUpPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleUpPosition, possibleRightCorner);

            if(!isOffShip){
                player.setY(possibleUpPosition.y)  ;
            } else {
                System.out.println("Bumping up!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            Vector2 possibleLeftPosition = new Vector2(
                    (playerPos.x) - (200 * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleLeftPosition.x + player.getWidth(),possibleLeftPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleLeftPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleLeftPosition.x)  ;
            }
            else {
                System.out.println("Bumping left!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            // Lets check if the new position is off the ship. Get possible position and opposite corner
            Vector2 possibleRightPosition = new Vector2(
                    (playerPos.x) + (200 * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleRightPosition.x + player.getWidth(),possibleRightPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleRightPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleRightPosition.x)  ;
            }
            else {
                System.out.println("Bumping right!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            Vector2 possibleDownPosition = new Vector2(playerPos.x,playerPos.y - 200 * Gdx.graphics.getDeltaTime());
            Vector2 possibleDownCorner = new Vector2(possibleDownPosition.x + player.getWidth(),possibleDownPosition.y); //
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleDownPosition, possibleDownCorner);

            if(!isOffShip){
                player.setY(possibleDownPosition.y);
            }
            else {
                System.out.println("Bumping down!");
            }
        }
    }


    /**
     * This algo updates the camera on the player based on the difference between
     * the player and the camera
     *
     * @param player - Player object
     * @param camera - Reference to camera context
     */
    public static void updateCameraOnPlayer(Player player, OrthographicCamera camera){
        float lerp = 0.8f;
        Vector3 cameraPos = camera.position;
        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);

        // Give the position of the camera no update if player is close enough to camera.
        Vector3 diff = new Vector3(playerPos);
        Vector3 diff2 = new Vector3(cameraPos);
        diff = diff.sub(diff2);

        if(Math.abs(diff.x) > 60){
            cameraPos.x += (playerPos.x - cameraPos.x) *
                    lerp * Gdx.graphics.getDeltaTime();
        }
        if(Math.abs(diff.y) > 60) {
            cameraPos.y += (playerPos.y - cameraPos.y) *
                    lerp * Gdx.graphics.getDeltaTime();
        }

//                    cameraPos.y += (playerPos.y - cameraPos.y) *
//                    lerp * Gdx.graphics.getDeltaTime();
//                    cameraPos.x += (playerPos.x - cameraPos.x) *
//                    lerp * Gdx.graphics.getDeltaTime();
    }
}
