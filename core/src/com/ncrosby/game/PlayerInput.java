package com.ncrosby.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ncrosby.game.generalObjects.Player;
import com.ncrosby.game.generalObjects.Ship;
import com.ncrosby.game.tiles.ShipTile;

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
     * @param player - player Rectangle to update
     */
    public static void handleKeyPressed(Player player, OrthographicCamera camera){

        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);
        Ship ship = player.getPlayerShip();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            if(ship.returnTile(playerPos.x, (playerPos.y + 10) + 200 * Gdx.graphics.getDeltaTime()) != null){
                player.setY(playerPos.y + 200 * Gdx.graphics.getDeltaTime())  ;
            }
            else {
                System.out.println("Bumping up!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            float leftMove = playerPos.x - 200 * Gdx.graphics.getDeltaTime();
            if(ship.returnTile(leftMove, playerPos.y) != null){
                player.setX(leftMove)  ;
            }
            else {
                System.out.println("Bumping left!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            if(ship.returnTile((playerPos.x + 64/* Sprite width */) + 200 * Gdx.graphics.getDeltaTime(), playerPos.y) != null){
                player.setX(playerPos.x + 200 * Gdx.graphics.getDeltaTime())  ;
            }
            else {
                System.out.println("Bumping right!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            if(ship.returnTile(playerPos.x, playerPos.y - 200 * Gdx.graphics.getDeltaTime()) != null){
                player.setY(playerPos.y - 200 * Gdx.graphics.getDeltaTime())  ;
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
