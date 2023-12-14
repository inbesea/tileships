package org.bitbucket.noahcrosby.shipGame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

public class PlayerInput {

    /**
     * Holy cow pie this is a terrible class.
     * We could move out the camera lerping, and updating the player location pretty easily.
     * We've just kept it this way for convenience. If it becomes a problem, we can change it.
     */

    public static float cameraFollow = 0.95f;

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
                    playerPos.y  + (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()));
            Vector2 possibleRightCorner = new Vector2(possibleUpPosition.x + player.getWidth(),possibleUpPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleUpPosition, possibleRightCorner);

            if(!isOffShip){
                player.setY(possibleUpPosition.y)  ;
            } else {
//                System.out.println("Bumping up!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            Vector2 possibleLeftPosition = new Vector2(
                    (playerPos.x) - (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleLeftPosition.x + player.getWidth(),possibleLeftPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleLeftPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleLeftPosition.x)  ;
            }
            else {
//                System.out.println("Bumping left!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            // Lets check if the new position is off the ship. Get possible position and opposite corner
            Vector2 possibleRightPosition = new Vector2(
                    (playerPos.x) + (player.getPlayerSpeed() * Gdx.graphics.getDeltaTime()),
                    playerPos.y);
            Vector2 possibleRightCorner = new Vector2(possibleRightPosition.x + player.getWidth(),possibleRightPosition.y);
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleRightPosition, possibleRightCorner);

            if(!isOffShip){
                player.setX(possibleRightPosition.x)  ;
            }
            else {
//                System.out.println("Bumping right!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            Vector2 possibleDownPosition = new Vector2(playerPos.x,playerPos.y - player.getPlayerSpeed() * Gdx.graphics.getDeltaTime());
            Vector2 possibleDownCorner = new Vector2(possibleDownPosition.x + player.getWidth(),possibleDownPosition.y); //
            boolean isOffShip = ship.isHorizontalSpanOffShip(possibleDownPosition, possibleDownCorner);

            if(!isOffShip){
                player.setY(possibleDownPosition.y);
            }
            else {
//                System.out.println("Bumping down!");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            localZoomClamp(false, camera);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            localZoomClamp(true, camera);
        }
    }

    /**
     * Updates zoom level and clamps within acceptable clamp levels
     * Meant for player use
     * @param zoomIn
     * @param camera
     */
    private static void localZoomClamp(boolean zoomIn, OrthographicCamera camera){
        int zoomAmount;
        if(zoomIn){zoomAmount = TileShipGame.zoomSpeed * -1;}
        else{zoomAmount = TileShipGame.zoomSpeed;}

        float newValue = camera.zoom + (zoomAmount * Gdx.graphics.getDeltaTime());

        if (newValue >= TileShipGame.zoomMax)
            newValue = TileShipGame.zoomMax;
        else newValue = Math.max(newValue, TileShipGame.zoomMin);

        camera.zoom = newValue;
    }

    /**
     * This algo updates the camera on the player based on the difference between
     * the player and the camera
     *
     * @param player - Player object
     * @param camera - Reference to camera context
     */
    public static void updateCameraOnPlayer(Player player, OrthographicCamera camera){
        // TODO : Migrate this and fix the camera flying off into nowhere when the screen is not focused.
        float lerp = 0.8f;
        Vector3 cameraPos = camera.position;
        Vector3 playerPos = new Vector3(player.getX(), player.getY(), 0);

        // Give the position of the camera no update if player is close enough to camera.
        Vector3 diff = new Vector3(playerPos);
        Vector3 diff2 = new Vector3(cameraPos);
        diff = diff.sub(diff2);

        // Issue is probably with this deltaTime check? If the delta is too big what happens?
        if(Math.abs(diff.x) > cameraFollow){
            cameraPos.x += (playerPos.x - cameraPos.x) *
                    lerp * Gdx.graphics.getDeltaTime();
        }
        if(Math.abs(diff.y) > cameraFollow) {
            cameraPos.y += (playerPos.y - cameraPos.y) *
                    lerp * Gdx.graphics.getDeltaTime();
        }
    }
}
